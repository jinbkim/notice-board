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

    @GetMapping("/create")
    public String findCreatePage(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "/post/form";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostDto postDto, RedirectAttributes redirectAttributes) throws IOException {
        Post post = postService.createPost(postDto);
        uploadFileService.addFiles(post.getId(), postDto.getUploadFiles());

        redirectAttributes.addAttribute("postId", post.getId());
        return "redirect:/post/page/{postId}";
    }



    @GetMapping("/page/{postId}")
    public String findPage(@PathVariable Integer postId, Model model) {
        Post post = postService.findPostById(postId);
        List<UploadFile> uploadFiles = uploadFileService.getUploadFiles(postId);

        model.addAttribute("post", post);
        if (!uploadFiles.isEmpty())
            model.addAttribute("uploadFiles", uploadFiles);
        return "/post/view";
    }

    @ResponseBody
    @GetMapping("/upload-file/dpwnload/{filename}")
    public Resource downloadUploadFiles(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + uploadFileService.getFullPath(filename));
    }

    @GetMapping("/upload-file/find/{postId}")
    public ResponseEntity<Resource> findUploadFiles(@PathVariable Integer postId) throws MalformedURLException {
        return uploadFileService.findUploadFiles(postId);
    }

    @GetMapping("/list")
    public String findPageList(Model model) {
        model.addAttribute("postPageList", postService.findPageList());
        return "/post/list";
    }



    @GetMapping("/update/{postId}")
    public String findUpdatePage(@PathVariable Integer postId, Model model) {
        Post post = postService.findPostById(postId);
        PostDto postDto = PostDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .build();

        model.addAttribute("postDto", postDto);
        return "/post/form";
    }

    @PostMapping("/update/{postId}")
    public String updatePost(@ModelAttribute PostDto postDto, RedirectAttributes redirectAttributes, @PathVariable Integer postId) throws IOException {
        postDto.setId(postId);
        Post post = postService.updatePost(postDto);
        uploadFileService.addFiles(post.getId(), postDto.getUploadFiles());

        redirectAttributes.addAttribute("postId", post.getId());
        return "redirect:/post/page/{postId}";
    }

//
//    @DeleteMapping
//    public void deletePost(Integer id) {
//        postService.deletePost(id);
//    }
}
