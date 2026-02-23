package com.secretminc.devhub.application.blogs;

import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.domain.blogs.Blog;
import com.secretminc.devhub.domain.blogs.BlogRepository;
import com.secretminc.devhub.exception.BlogNotFoundException;
import com.secretminc.devhub.fixture.BlogFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogService 단위 테스트")
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    private Blog testBlog;
    private BlogCreateRequest createRequest;
    private BlogUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testBlog = BlogFixture.createDefaultBlog();
        createRequest = BlogFixture.createBlogCreateRequest();
        updateRequest = BlogFixture.createBlogUpdateRequest();
    }

    @Test
    @DisplayName("모든 블로그 조회 - 성공")
    void getAllBlogs_Success() {
        // given
        List<Blog> blogs = Arrays.asList(
                BlogFixture.createDefaultBlog(),
                BlogFixture.createBlog("blog-2", "블로그2", "https://blog2.com", "설명2")
        );
        when(blogRepository.findAll()).thenReturn(blogs);

        // when
        List<BlogResponse> result = blogService.getAllBlogs();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(BlogResponse::getBlogId)
                .containsExactly("test-blog-1", "blog-2");
        verify(blogRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("활성 블로그만 조회 - 성공")
    void getActiveBlogs_Success() {
        // given
        List<Blog> activeBlogs = Arrays.asList(
                BlogFixture.createActiveBlog(),
                BlogFixture.createActiveBlog()
        );
        when(blogRepository.findByIsActive(true)).thenReturn(activeBlogs);

        // when
        List<BlogResponse> result = blogService.getActiveBlogs();

        // then
        assertThat(result).hasSize(2);
        verify(blogRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("블로그 단건 조회 - 성공")
    void getBlog_Success() {
        // given
        when(blogRepository.findById("test-blog-1")).thenReturn(Optional.of(testBlog));

        // when
        BlogResponse result = blogService.getBlog("test-blog-1");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBlogId()).isEqualTo("test-blog-1");
        verify(blogRepository, times(1)).findById("test-blog-1");
    }

    @Test
    @DisplayName("블로그 단건 조회 - 존재하지 않으면 BlogNotFoundException 발생")
    void getBlog_NotFound_ThrowsBlogNotFoundException() {
        // given
        when(blogRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> blogService.getBlog("non-existent-id"))
                .isInstanceOf(BlogNotFoundException.class)
                .hasMessageContaining("non-existent-id");

        verify(blogRepository, times(1)).findById("non-existent-id");
    }

    @Test
    @DisplayName("블로그 생성 - 성공")
    void create_Success() {
        // given
        Blog savedBlog = createRequest.toEntity();
        when(blogRepository.save(any(Blog.class))).thenReturn(savedBlog);

        // when
        BlogResponse result = blogService.create(createRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBlogId()).isEqualTo(createRequest.getBlogId());
        assertThat(result.getBlogName()).isEqualTo(createRequest.getBlogName());
        assertThat(result.getBlogUrl()).isEqualTo(createRequest.getBlogUrl());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    @DisplayName("블로그 업데이트 - 성공")
    void update_Success() {
        // given
        when(blogRepository.findById("test-blog-1")).thenReturn(Optional.of(testBlog));

        // when
        blogService.update("test-blog-1", updateRequest);

        // then
        assertThat(testBlog.getBlogName()).isEqualTo(updateRequest.getBlogName());
        assertThat(testBlog.getBlogUrl()).isEqualTo(updateRequest.getBlogUrl());
        assertThat(testBlog.getDescription()).isEqualTo(updateRequest.getDescription());
        verify(blogRepository, times(1)).findById("test-blog-1");
    }

    @Test
    @DisplayName("블로그 업데이트 - 존재하지 않는 ID로 BlogNotFoundException 발생")
    void update_NotFound_ThrowsBlogNotFoundException() {
        // given
        when(blogRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> blogService.update("non-existent-id", updateRequest))
                .isInstanceOf(BlogNotFoundException.class)
                .hasMessageContaining("non-existent-id");

        verify(blogRepository, times(1)).findById("non-existent-id");
    }

    @Test
    @DisplayName("블로그 업데이트 - null 값으로도 업데이트 가능")
    void update_WithNullValues() {
        // given
        BlogUpdateRequest nullRequest = new BlogUpdateRequest();
        when(blogRepository.findById("test-blog-1")).thenReturn(Optional.of(testBlog));

        // when
        blogService.update("test-blog-1", nullRequest);

        // then
        assertThat(testBlog.getBlogName()).isNull();
        assertThat(testBlog.getBlogUrl()).isNull();
        assertThat(testBlog.getDescription()).isNull();
    }
}
