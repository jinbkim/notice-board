package com.jinkim.noticeboard.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer id;
    private String title;
    private String content;
    private List<MultipartFile> uploadFiles;

    public PostDto(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
