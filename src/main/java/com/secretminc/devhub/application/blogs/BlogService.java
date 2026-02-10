package com.secretminc.devhub.application.blogs;


import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.domain.blogs.Blog;
import com.secretminc.devhub.domain.blogs.mapper.BlogMapper;
import com.secretminc.devhub.domain.blogs.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;


    public List<Blog> getAllBlogs(){
        return blogRepository.findAll();
    }

    public List<Blog> getActiveBlogs(){
        return blogRepository.findByIsActive(true);
    }

    public Blog getBlog(String blogId){
        return blogMapper.findById(blogId);
    }

    public Blog save(Blog blog){
        return blogRepository.save(blog);
    }

    public void update(String blogId, BlogUpdateRequest request){
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("blog not found"));
        blog.update(request.getBlogName(),request.getBlogUrl(),request.getDescription());
    }

    @Transactional(readOnly = true)
    public BlogResponse getBlogs(String blogId) {
        return new BlogResponse(blogMapper.findById(blogId));
    }


    public BlogResponse create(BlogCreateRequest request) {
        Blog blog = request.toEntity();
        return new BlogResponse(blogRepository.save(blog));
    }



}
