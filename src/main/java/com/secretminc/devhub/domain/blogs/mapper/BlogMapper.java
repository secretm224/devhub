package com.secretminc.devhub.domain.blogs.mapper;

import com.secretminc.devhub.domain.blogs.Blog;
import org.apache.ibatis.annotations.Mapper;


public interface  BlogMapper {
    Blog findById(String blogId);
}
