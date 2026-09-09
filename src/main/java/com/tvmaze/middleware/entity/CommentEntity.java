package com.tvmaze.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String id;
    private Long showId;
    private String comment;
    private Double rating;
    private LocalDateTime createdAt;
}


