package com.secretminc.devhub.application.blogs;

import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.domain.blogs.Blog;
import com.secretminc.devhub.domain.blogs.BlogRepository;
import com.secretminc.devhub.exception.BlogNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(BlogResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BlogResponse> getActiveBlogs() {
        return blogRepository.findByIsActive(true).stream()
                .map(BlogResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public BlogResponse getBlog(String blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException(blogId));
        return new BlogResponse(blog);
    }

    public BlogResponse create(BlogCreateRequest request) {
        Blog blog = request.toEntity();
        return new BlogResponse(blogRepository.save(blog));
    }

    public void update(String blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException(blogId));
        blog.update(request.getBlogName(), request.getBlogUrl(), request.getDescription());
    }
}
