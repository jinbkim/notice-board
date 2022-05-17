package com.jinkim.noticeboard.repository;

import com.jinkim.noticeboard.entity.UploadFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Integer> {

    @Modifying
    @Transactional
    @Query(value ="INSERT INTO upload_file(id) VALUES(:id)", nativeQuery = true)
    void createUploadFile(@Param("id")Integer id);



    @Query(value ="SELECT * FROM upload_file" , nativeQuery = true)
    List<UploadFile> findUploadFileAll();

    @Query(value = "SELECT * FROM upload_file WHERE id=:id",nativeQuery = true)
    Optional<UploadFile> findUploadFileById(@Param("id") Integer id);

    @Query(value="SELECT max(id) FROM upload_file", nativeQuery = true)
    Integer findUploadFileMaxId();



    @Modifying
    @Transactional
    @Query(value ="UPDATE upload_file SET post_id=:post_id, upload_filename=:upload_filename, store_filename=:store_filename where id=:id" , nativeQuery = true)
    void updateUploadFile(@Param("id")Integer id, @Param("post_id")Integer postId, @Param("upload_filename")String uploadFilename, @Param("store_filename")String storeFilename);



    @Modifying
    @Transactional
    @Query(value ="DELETE FROM upload_file WHERE id=:id" , nativeQuery = true)
    void deleteUploadFileById(@Param("id") Integer id);
}
