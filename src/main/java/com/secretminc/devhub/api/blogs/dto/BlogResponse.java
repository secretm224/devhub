package com.secretminc.devhub.api.blogs.dto;

import com.secretminc.devhub.domain.blogs.Blog;
import lombok.Getter;

@Getter
public class BlogResponse {

    private final String blogId;
    private final String blogName;
    private final String blogUrl;
    private final String description;

    public BlogResponse(Blog blog) {
        this.blogId = blog.getBlogId();
        this.blogName = blog.getBlogName();
        this.blogUrl = blog.getBlogUrl();
        this.description = blog.getDescription();
    }
}

