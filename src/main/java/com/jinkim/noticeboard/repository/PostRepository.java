package com.jinkim.noticeboard.repository;

import com.jinkim.noticeboard.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Integer>{

    @Modifying
    @Transactional
    @Query(value ="INSERT INTO post(id) VALUES(:id)", nativeQuery = true)
    void createPost(@Param("id")Integer id);



    @Query(value = "SELECT * FROM post WHERE id=:id",nativeQuery = true)
    Optional<Post> findPostById(@Param("id") Integer id);

    @Query(value ="SELECT * FROM post" , nativeQuery = true)
    List<Post> findPostAll();

    @Query(value="SELECT max(id) FROM post", nativeQuery = true)
    Integer findPostMaxId();



    @Modifying
    @Transactional
    @Query(value ="UPDATE post SET title=:title, content=:content where id=:id" , nativeQuery = true)
    void updatePost(@Param("id")Integer id, @Param("title")String title, @Param("content")String content);



    @Modifying
    @Transactional
    @Query(value ="DELETE FROM post WHERE id=:id" , nativeQuery = true)
    void deletePostById(@Param("id") Integer id);
}