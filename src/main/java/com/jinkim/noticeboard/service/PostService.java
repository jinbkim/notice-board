package com.jinkim.noticeboard.service;

import com.jinkim.noticeboard.entity.Post;
import com.jinkim.noticeboard.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void test() {
        Post post = Post.builder()
            .title("제목")
            .build();
        Post post2 = postRepository.save(post);
        log.info(post2.getTitle());
    }

}
