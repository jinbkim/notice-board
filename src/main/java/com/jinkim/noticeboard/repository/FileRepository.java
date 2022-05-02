package com.jinkim.noticeboard.repository;

import com.jinkim.noticeboard.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFile, Integer> {

}
