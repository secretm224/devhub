package com.secretminc.devhub.api.blogs.dto;

import com.secretminc.devhub.domain.blogs.Blog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogCreateRequest {
    @NotBlank
    private String blogId;
    @NotBlank
    private String blogName;
    @NotBlank
    private String blogUrl;

    @Size(max = 500)
    private String description;

    public Blog toEntity() {
        return new Blog(blogId, blogName, blogUrl, description);
    }
}

