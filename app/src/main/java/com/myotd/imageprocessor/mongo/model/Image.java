package com.myotd.imageprocessor.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Image {
    @Id
    private String id;
    private String userID;
    private String collectionID;
    private String name;
    private String path;
    private LocalDateTime uploadTime;
    @CreatedDate
    private LocalDateTime createTime;
    @Version
    private String version;
}