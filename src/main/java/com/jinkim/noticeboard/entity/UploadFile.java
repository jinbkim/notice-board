package com.jinkim.noticeboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer postId;
    @Column
    private String uploadFilename;
    @Column
    private String storeFilename;

    public UploadFile(Integer postId, String uploadFilename, String storeFilename) {
        this.postId = postId;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
    }
}
