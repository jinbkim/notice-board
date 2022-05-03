package com.jinkim.noticeboard.service;

import com.jinkim.noticeboard.dto.PostDto;
import com.jinkim.noticeboard.entity.Post;
import com.jinkim.noticeboard.repository.PostRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(@ModelAttribute PostDto postDto) throws IOException {
        Post post = Post.builder()
            .title(postDto.getTitle())
            .content(postDto.getContent())
            .build();
        return postRepository.save(post);
    }

    public Post findPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findPageList() {
        return postRepository.findAll();
    }


//    public PostDto[] findPostList(Integer postAmount, Integer pageNumber) {
//        List<Post> postList = postRepository.findAll();
//        PostDto[] postDtos = new PostDto[postAmount];
//
//        pageNumber--;
//        int index = -1;
//        for(int i=pageNumber*pageNumber; i<pageNumber*(pageNumber+1); i++) {
//            if (i == postList.size())
//                break ;
//            postDtos[++index] = new PostDto(postList.get(i));
//        }
//
//        return postDtos;
//    }
//
//    public void modifyPost(PostDto postDto) {
//        Post post = postDtotoPost(postDto);
//        postRepository.save(post);
//    }
//
//    public void deletePost(Integer id) {
//        Post post = postRepository.findById(id).get();
//        postRepository.delete(post);
//    }
//
//
//    private Post postDtotoPost(PostDto postDto) {
//        Post post = Post.builder()
//            .title(postDto.getTitle())
//            .content(postDto.getContent())
//            .file(postDto.getFile())
//            .build();
//        return post;
//    }

}
