package com.jinkim.noticeboard.repository;

import com.jinkim.noticeboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
