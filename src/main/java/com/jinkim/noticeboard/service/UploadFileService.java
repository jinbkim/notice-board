package com.jinkim.noticeboard.service;

import com.jinkim.noticeboard.entity.UploadFile;
import com.jinkim.noticeboard.repository.UploadFileRepository;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public void addFiles(Integer postId, List<MultipartFile> multipartFiles) throws IOException {
        // 첨부파일을 올리지 않아도 MultipartFile 한개가 담겨져 있기 때문에, 첫번째 파일의 originalFilename이 비어있으면 첨부파일을 안담은걸로 간주
        if (multipartFiles.get(0).getOriginalFilename().isEmpty())
            return ;
        deleteUploadFileByPostId(postId);  // 첨부파일을 수정했으면 기존에 있던 첨부파일 삭제

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty())
                continue;
            uploadFileRepository.save(addFile(postId, multipartFile));
        }
    }



    public String getFullPath(String fileName) {
        return fileDir+fileName;
    }

    public ResponseEntity<Resource> findUploadFiles(Integer postId) throws MalformedURLException {
        UploadFile uploadFile = uploadFileRepository.findById(postId).orElse(null);
        UrlResource urlResource = new UrlResource("file:" + getFullPath(uploadFile.getStoreFilename()));
        String encodedUploadFilename = UriUtils.encode(uploadFile.getUploadFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(urlResource);
    }

    public List<UploadFile> getUploadFiles(Integer postId) {
        List<UploadFile> uploadFiles = uploadFileRepository.findAll();

        return uploadFiles.stream()
            .filter(file -> file.getPostId() == postId)
            .collect(Collectors.toList());
    }



    public void deleteUploadFileByPostId(Integer postId) {
        findUploadFileByPostId(postId).stream()
            .forEach(uploadFile -> { uploadFileRepository.delete(uploadFile); });
    }

    public List<UploadFile> findUploadFileByPostId(Integer postId) {
       return uploadFileRepository.findAll().stream()
           .filter(uploadFile -> uploadFile.getPostId() == postId)
           .collect(Collectors.toList());
    }



    private UploadFile addFile(Integer postId ,MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename)));
        return new UploadFile(postId, originalFilename, storeFilename);
    }

    private String createStoreFilename(String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." +extension;
    }

    private String getExtension (String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".")+1);
    }
}
