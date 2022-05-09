package com.jinkim.noticeboard.controller;

import com.jinkim.noticeboard.dto.PostDto;
import com.jinkim.noticeboard.entity.Post;
import com.jinkim.noticeboard.entity.UploadFile;
import com.jinkim.noticeboard.service.PostService;
import com.jinkim.noticeboard.service.UploadFileService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UploadFileService uploadFileService;

    /**
     * 게시물 생성 페이지 조회
     * @param model 게시물 생성 폼을 저장한 뒤 뷰에 전달
     * @return 게시물 생성 페이지
     */
    @GetMapping("/create")
    public String findCreatePage(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "/post/form";
    }

    /**
     * 사용자가 입력한 데이터가 담긴 게시물 생성 폼으로 게시물 생성
     * @param postDto 사용자가 입력한 데이터가 담긴 게시물 생성 폼
     * @param redirectAttributes 게시물 아이디를 뷰에 전달
     * @return 게시물 조회 페이지 주소
     */
    @PostMapping("/create")
    public String createPost(@ModelAttribute PostDto postDto, RedirectAttributes redirectAttributes) throws IOException {
        Post post = postService.createPost(postDto);
        uploadFileService.addFiles(post.getId(), postDto.getUploadFiles());

        redirectAttributes.addAttribute("postId", post.getId());
        return "redirect:/post/page/{postId}";
    }



    /**
     * 게시물 조회 페이지 조회
     * @param postId 조회할 게시물 아이디
     * @param model 게시물 데이터를 담아 뷰에 전달
     * @return 게시물 조회 페이지
     */
    @GetMapping("/page/{postId}")
    public String findPage(@PathVariable Integer postId, Model model) {
        Post post = postService.findPostById(postId);
        List<UploadFile> uploadFiles = uploadFileService.getUploadFiles(postId);

        model.addAttribute("post", post);
        if (!uploadFiles.isEmpty())
            model.addAttribute("uploadFiles", uploadFiles);
        return "/post/view";
    }

    /**
     * 청부파일 조회
     * @param filename 조회할 파일 이름
     * @return 조회할 파일
     */
    @ResponseBody
    @GetMapping("/upload-file/find/{filename}")
    public Resource findUploadFiles(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + uploadFileService.getFullPath(filename));
    }

    /**
     * 첨부파일 다운로드
     * @param id 다운로드할 파일 아이디
     * @return 다운로드할 파일
     */
    @GetMapping("/upload-file/download/{id}")
    public ResponseEntity<Resource> downloadUploadFiles(@PathVariable Integer id) throws MalformedURLException {
        return uploadFileService.downloadUploadFiles(id);
    }

    /**
     * 게시물 리스트 조회
     * @param model 게시물 리스트를 저장한뒤 뷰에 전달
     * @return 게시물 리스트 조회 페이지
     */
    @GetMapping("/list")
    public String findPageList(Model model) {
        model.addAttribute("postPageList", postService.findPageList());
        return "/post/list";
    }



    /**
     * 게시물 수정 페이지 조회
     * @param postId 수정할 게시물 아이디
     * @param model 게시물 수정 폼을 저장한 뒤 뷰에 전달
     * @return 게시물 수정 페이지
     */
    @GetMapping("/update/{postId}")
    public String findUpdatePage(@PathVariable Integer postId, Model model) {
        Post post = postService.findPostById(postId);
        PostDto postDto = new PostDto(post.getId(), post.getTitle(), post.getContent());

        model.addAttribute("postDto", postDto);
        return "/post/form";
    }

    /**
     * 게시물 수정
     * @param postDto 사용자가 입력한 데이터가 담긴 게시물 수정 폼
     * @param redirectAttributes 게시물 아이디를 뷰에 전달
     * @param postId 수정할 게시물 아이디
     * @return 게시물 조회 페이지
     */
    @PostMapping("/update/{postId}")
    public String updatePost(@ModelAttribute PostDto postDto, RedirectAttributes redirectAttributes, @PathVariable Integer postId) throws IOException {
        postDto.setId(postId);
        Post post = postService.updatePost(postDto);
        uploadFileService.addFiles(post.getId(), postDto.getUploadFiles());

        redirectAttributes.addAttribute("postId", post.getId());
        return "redirect:/post/page/{postId}";
    }



    /**
     * 게시물 삭제
     * @param postId 삭제할 게시물 아이디
     * @return 게시물 리스트 조회 페이지
     */
    @GetMapping("/delete/{postId}")
    public String deletePage(@PathVariable Integer postId) {
        postService.deletePost(postId);
        uploadFileService.deleteUploadFileByPostId(postId);
        return "redirect:/post/list";
    }
}
