package com.jinkim.noticeboard.postRepositoryTest;

import com.jinkim.noticeboard.entity.Post;
import com.jinkim.noticeboard.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostRepositoryTest {

    Logger log = (Logger) LoggerFactory.getLogger(PostRepositoryTest.class);

    @Autowired
    private PostRepository postRepository;

    @Test
    public void create() {
        Post post = Post.builder()
            .title("제목")
            .build();
        Post post2 = postRepository.save(post);
        log.info(post2.getTitle());
    }

}
