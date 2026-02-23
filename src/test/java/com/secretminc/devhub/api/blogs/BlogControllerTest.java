package com.secretminc.devhub.api.blogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogResponse;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.application.blogs.BlogService;
import com.secretminc.devhub.exception.BlogNotFoundException;
import com.secretminc.devhub.exception.GlobalExceptionHandler;
import com.secretminc.devhub.fixture.BlogFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogController 통합 테스트")
class BlogControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /api/blogs - 모든 블로그 조회 성공")
    void getBlogs_Success() throws Exception {
        // given
        List<BlogResponse> blogs = Arrays.asList(
                new BlogResponse(BlogFixture.createDefaultBlog()),
                new BlogResponse(BlogFixture.createBlog("blog-2", "블로그2", "https://blog2.com", "설명2"))
        );
        when(blogService.getAllBlogs()).thenReturn(blogs);

        // when & then
        mockMvc.perform(get("/api/blogs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].blogId").value("test-blog-1"))
                .andExpect(jsonPath("$[0].blogName").value("테스트 블로그"))
                .andExpect(jsonPath("$[1].blogId").value("blog-2"));

        verify(blogService, times(1)).getAllBlogs();
    }

    @Test
    @DisplayName("GET /api/blogs/active - 활성 블로그만 조회 성공")
    void getActiveBlogs_Success() throws Exception {
        // given
        List<BlogResponse> activeBlogs = Arrays.asList(
                new BlogResponse(BlogFixture.createActiveBlog()),
                new BlogResponse(BlogFixture.createActiveBlog())
        );
        when(blogService.getActiveBlogs()).thenReturn(activeBlogs);

        // when & then
        mockMvc.perform(get("/api/blogs/active"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(blogService, times(1)).getActiveBlogs();
    }

    @Test
    @DisplayName("GET /api/blogs/{blogId} - 특정 블로그 조회 성공")
    void getBlog_Success() throws Exception {
        // given
        BlogResponse response = new BlogResponse(BlogFixture.createDefaultBlog());
        when(blogService.getBlog("test-blog-1")).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/blogs/test-blog-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogId").value("test-blog-1"))
                .andExpect(jsonPath("$.blogName").value("테스트 블로그"))
                .andExpect(jsonPath("$.blogUrl").value("https://test-blog.com"));

        verify(blogService, times(1)).getBlog("test-blog-1");
    }

    @Test
    @DisplayName("GET /api/blogs/{blogId} - 존재하지 않는 블로그 조회 시 404 반환")
    void getBlog_NotFound_Returns404() throws Exception {
        // given
        when(blogService.getBlog("non-existent-id")).thenThrow(new BlogNotFoundException("non-existent-id"));

        // when & then
        mockMvc.perform(get("/api/blogs/non-existent-id"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(blogService, times(1)).getBlog("non-existent-id");
    }

    @Test
    @DisplayName("POST /api/blogs - 블로그 생성 성공")
    void createBlog_Success() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        BlogResponse response = new BlogResponse(request.toEntity());
        when(blogService.create(any(BlogCreateRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogId").value(request.getBlogId()))
                .andExpect(jsonPath("$.blogName").value(request.getBlogName()))
                .andExpect(jsonPath("$.blogUrl").value(request.getBlogUrl()));

        verify(blogService, times(1)).create(any(BlogCreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/blogs - 필수 필드 누락 시 유효성 검사 실패")
    void createBlog_ValidationFails_WhenRequiredFieldsMissing() throws Exception {
        // given
        BlogCreateRequest invalidRequest = BlogFixture.createInvalidBlogCreateRequest();

        // when & then
        mockMvc.perform(post("/api/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(blogService, never()).create(any(BlogCreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/blogs - description이 500자 초과 시 유효성 검사 실패")
    void createBlog_ValidationFails_WhenDescriptionTooLong() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        String longDescription = "a".repeat(501);
        var field = request.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(request, longDescription);

        // when & then
        mockMvc.perform(post("/api/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(blogService, never()).create(any(BlogCreateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/blogs/{blogId} - 블로그 수정 성공")
    void updateBlog_Success() throws Exception {
        // given
        String blogId = "test-blog-1";
        BlogUpdateRequest request = BlogFixture.createBlogUpdateRequest();
        doNothing().when(blogService).update(eq(blogId), any(BlogUpdateRequest.class));

        // when & then
        mockMvc.perform(put("/api/blogs/" + blogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(blogService, times(1)).update(eq(blogId), any(BlogUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/blogs/{blogId} - 존재하지 않는 블로그 수정 시 404 반환")
    void updateBlog_NotFound_Returns404() throws Exception {
        // given
        String nonExistentId = "non-existent-id";
        BlogUpdateRequest request = BlogFixture.createBlogUpdateRequest();
        doThrow(new BlogNotFoundException(nonExistentId))
                .when(blogService).update(eq(nonExistentId), any(BlogUpdateRequest.class));

        // when & then
        mockMvc.perform(put("/api/blogs/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(blogService, times(1)).update(eq(nonExistentId), any(BlogUpdateRequest.class));
    }

    @Test
    @DisplayName("GET /api/blogs - 빈 리스트 반환")
    void getBlogs_ReturnsEmptyList() throws Exception {
        // given
        when(blogService.getAllBlogs()).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/blogs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(blogService, times(1)).getAllBlogs();
    }
}
