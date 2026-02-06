package com.secretminc.devhub.domain.blogs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,String> {
    List<Blog> findByIsActive(Boolean isActive);
}

