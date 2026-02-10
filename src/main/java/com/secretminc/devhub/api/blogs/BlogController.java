package com.secretminc.devhub.api.blogs;


import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.application.blogs.BlogService;
import com.secretminc.devhub.domain.blogs.Blog;
import com.secretminc.devhub.domain.blogs.BlogRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;
    private final BlogService blogService;

    @GetMapping
    public List<Blog> getBlogs(){
        return  blogRepository.findAll();
    }

    @GetMapping("/active")
    public List<Blog> getActiveBlogs(){
        return blogRepository.findByIsActive(true);
    }

    @GetMapping("/{blogId}")
    public Blog getBlog(@PathVariable String blogId){
        return blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
    }

    @PostMapping
    public BlogResponse createBlog(
            @Valid @RequestBody BlogCreateRequest request
    ) {
        return blogService.create(request);
    }

    @PutMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBlog(@PathVariable String blogId, @Valid @RequestBody BlogUpdateRequest request){
         blogService.update(blogId,request);
    }

}
