package com.secretminc.devhub.api.blogs;

import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.application.blogs.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public List<BlogResponse> getBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/active")
    public List<BlogResponse> getActiveBlogs() {
        return blogService.getActiveBlogs();
    }

    @GetMapping("/{blogId}")
    public BlogResponse getBlog(@PathVariable String blogId) {
        return blogService.getBlog(blogId);
    }

    @PostMapping
    public BlogResponse createBlog(@Valid @RequestBody BlogCreateRequest request) {
        return blogService.create(request);
    }

    @PutMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBlog(@PathVariable String blogId, @Valid @RequestBody BlogUpdateRequest request) {
        blogService.update(blogId, request);
    }
}
