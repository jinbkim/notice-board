package com.jinkim.noticeboard.service;

import com.jinkim.noticeboard.dto.PostDto;
import com.jinkim.noticeboard.entity.Post;
import com.jinkim.noticeboard.repository.PostRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /**
     * 게시물 생성
     * @param postDto 게시물 생성 폼
     * @return 생성된 게시물
     */
    public Post createPost(PostDto postDto) throws IOException {
        Post post = Post.builder()
            .title(postDto.getTitle())
            .content(postDto.getContent())
            .build();
        return postRepository.save(post);
    }



    /**
     * 게시물 조회
     * @param id 조회할 게시물 아이디
     * @return 조회할 게시물
     */
    public Post findPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * 게시물 리스트 조회
     * @return 게시물 리스트
     */
    public List<Post> findPageList() {
        return postRepository.findAll();
    }


    /**
     * 게시물 수정
     * @param postDto 게시물 수정 폼
     * @return 수정된 게시물
     */
    public Post updatePost(PostDto postDto) throws IOException {
        Post post = Post.builder()
            .id(postDto.getId())
            .title(postDto.getTitle())
            .content(postDto.getContent())
            .build();
        return postRepository.save(post);
    }



    /**
     * 게시물 삭제
     * @param id 삭제할 게시물 아이디
     */
    public void deletePost(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        postRepository.delete(post);
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

}
