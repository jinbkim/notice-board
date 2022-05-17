package com.jinkim.noticeboard.service;

import com.jinkim.noticeboard.entity.UploadFile;
import com.jinkim.noticeboard.repository.UploadFileRepository;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    /**
     * 첨부파일 리스트 저장
     * @param postId 파일을 저장할 게시물 아이디
     * @param multipartFiles 저장할 첨부파일 리스트
     */
    public void addFiles(Integer postId, List<MultipartFile> multipartFiles) throws IOException {
        // 첨부파일을 올리지 않아도 MultipartFile 한개가 담겨져 있더라
        // 첫번째 파일의 originalFilename이 비어있으면 첨부파일을 안담은걸로 간주
        if (multipartFiles==null || multipartFiles.get(0).getOriginalFilename().isEmpty())
            return ;
        deleteUploadFileByPostId(postId);  // 첨부파일을 수정했으면 기존에 있던 첨부파일 삭제

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty())
                continue;
            saveUploadFile(addFile(postId, multipartFile));
        }
    }

    /**
     * DB에 첨부파일 저장
     * @param uploadFile 저장할 파일
     * @return 저장된 파일
     */
    private UploadFile saveUploadFile(UploadFile uploadFile) {
        Integer maxId;
        if (uploadFile.getId() == null) {  // UploadFile 생성시
            maxId = uploadFileRepository.findUploadFileMaxId();
            if (maxId == null)  // upload_file 테이블이 비어 있을 경우
                maxId = 0;

            uploadFileRepository.createUploadFile(++maxId);
            uploadFile.setId(maxId);
        }

        uploadFileRepository.updateUploadFile(uploadFile.getId(), uploadFile.getPostId(), uploadFile.getUploadFilename(), uploadFile.getStoreFilename());
        return uploadFile;
    }

    /**
     * 첨부파일 저장
     * @param postId 파일을 저장할 게시물 아이디
     * @param multipartFile 저장할 파일
     * @return 저장된 파일
     */
    private UploadFile addFile(Integer postId ,MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename)));
        return new UploadFile(postId, originalFilename, storeFilename);
    }

    /**
     * UUID를 이용하여 첨부파일 이름 생성
     * @param originalFilename 기존의 파일 이름
     * @return 생성한 파일 이름
     */
    private String createStoreFilename(String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." +extension;
    }

    /**
     * 첨부파일 이름에서 확장자만 추출
     * @param originalFilename 기존의 파일 이름
     * @return 파일의 확장자
     */
    private String getExtension (String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".")+1);
    }



    /**
     * 첨부파일의 절대 경로 조회
     * @param fileName 조회할 파일 이름
     * @return 파일의 절대 경로
     */
    public String getFullPath(String fileName) {
        return fileDir+fileName;
    }

    /**
     * 첨부파일 다운로드
     * @param id 다운로드 할 파일 아이디
     * @return 다운로드할 파일
     */
    public ResponseEntity<Resource> downloadUploadFiles(Integer id) throws MalformedURLException {
        UploadFile uploadFile = uploadFileRepository.findUploadFileById(id).orElse(null);
        UrlResource urlResource = new UrlResource("file:" + getFullPath(uploadFile.getStoreFilename()));
        String encodedUploadFilename = UriUtils.encode(uploadFile.getUploadFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(urlResource);
    }

    /**
     * 첨부파일 리스트 조회
     * @param postId 조회할 파일이 담긴 게시물 아이디
     * @return 조회할 첨부파일 리스트
     */
    public List<UploadFile> getUploadFiles(Integer postId) {
        List<UploadFile> uploadFiles = uploadFileRepository.findUploadFileAll();

        return uploadFiles.stream()
            .filter(file -> file.getPostId() == postId)
            .collect(Collectors.toList());
    }



    /**
     * 첨부파일 삭제
     * @param postId 삭제할 파일이 담긴 게시물 아이디
     */
    public void deleteUploadFileByPostId(Integer postId) {
        findUploadFileByPostId(postId).stream()
            .forEach(uploadFile -> {
                try {
                    uploadFileRepository.deleteUploadFileById(uploadFile.getId());
                    Files.delete(Paths.get(getFullPath(uploadFile.getStoreFilename())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    /**
     * 첨부파일 리스트 조회
     * @param postId 조회할 파일들이 담긴 게시물 아이디
     * @return 파일 리스트
     */
    private List<UploadFile> findUploadFileByPostId(Integer postId) {
       return uploadFileRepository.findUploadFileAll().stream()
           .filter(uploadFile -> uploadFile.getPostId() == postId)
           .collect(Collectors.toList());
    }
}
