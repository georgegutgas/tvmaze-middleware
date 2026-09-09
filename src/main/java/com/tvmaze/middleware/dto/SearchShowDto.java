package com.tvmaze.middleware.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchShowDto {
    private Long id;
    private String name;
    private String channel;
    private String summary;
    private List<String> genres;
}
