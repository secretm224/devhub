package com.secretminc.devhub.api.blogs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogUpdateRequest {

    private String blogName;
    private String blogUrl;
    private String description;
}
