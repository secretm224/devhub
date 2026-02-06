package com.secretminc.devhub.api.blogs;


import com.secretminc.devhub.domain.blogs.Blog;
import com.secretminc.devhub.domain.blogs.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;

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
}
