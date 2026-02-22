package com.secretminc.devhub.domain.blogs;

import com.secretminc.devhub.fixture.BlogFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("BlogRepository 테스트")
class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    void setUp() {
        blogRepository.deleteAll();
    }

    @Test
    @DisplayName("블로그 저장 및 조회 - 성공")
    void save_And_FindById_Success() {
        // given
        Blog blog = BlogFixture.createDefaultBlog();

        // when
        Blog savedBlog = blogRepository.save(blog);
        Optional<Blog> foundBlog = blogRepository.findById(savedBlog.getBlogId());

        // then
        assertThat(foundBlog).isPresent();
        assertThat(foundBlog.get().getBlogId()).isEqualTo(blog.getBlogId());
        assertThat(foundBlog.get().getBlogName()).isEqualTo(blog.getBlogName());
        assertThat(foundBlog.get().getBlogUrl()).isEqualTo(blog.getBlogUrl());
    }

    @Test
    @DisplayName("모든 블로그 조회 - 성공")
    void findAll_Success() {
        // given
        Blog blog1 = BlogFixture.createDefaultBlog();
        Blog blog2 = BlogFixture.createBlog("blog-2", "블로그2", "https://blog2.com", "설명2");

        blogRepository.save(blog1);
        blogRepository.save(blog2);

        // when
        List<Blog> blogs = blogRepository.findAll();

        // then
        assertThat(blogs).hasSize(2);
        assertThat(blogs).extracting(Blog::getBlogId)
                .containsExactlyInAnyOrder("test-blog-1", "blog-2");
    }

    @Test
    @DisplayName("활성 상태 블로그만 조회 - 성공")
    void findByIsActive_True_Success() {
        // given
        Blog activeBlog1 = BlogFixture.createActiveBlog();
        Blog activeBlog2 = BlogFixture.createActiveBlog();
        Blog inactiveBlog = BlogFixture.createInactiveBlog();

        blogRepository.save(activeBlog1);
        blogRepository.save(activeBlog2);
        blogRepository.save(inactiveBlog);

        // when
        List<Blog> activeBlogs = blogRepository.findByIsActive(true);

        // then
        assertThat(activeBlogs).hasSize(2);
        assertThat(activeBlogs).allMatch(Blog::getIsActive);
    }

    @Test
    @DisplayName("비활성 상태 블로그만 조회 - 성공")
    void findByIsActive_False_Success() {
        // given
        Blog activeBlog = BlogFixture.createActiveBlog();
        Blog inactiveBlog = BlogFixture.createInactiveBlog();

        blogRepository.save(activeBlog);
        blogRepository.save(inactiveBlog);

        // when
        List<Blog> inactiveBlogs = blogRepository.findByIsActive(false);

        // then
        assertThat(inactiveBlogs).hasSize(1);
        assertThat(inactiveBlogs).allMatch(blog -> !blog.getIsActive());
    }

    @Test
    @DisplayName("존재하지 않는 블로그 조회 - Optional.empty 반환")
    void findById_NotFound_ReturnsEmpty() {
        // when
        Optional<Blog> foundBlog = blogRepository.findById("non-existent-id");

        // then
        assertThat(foundBlog).isEmpty();
    }

    @Test
    @DisplayName("블로그 삭제 - 성공")
    void delete_Success() {
        // given
        Blog blog = BlogFixture.createDefaultBlog();
        Blog savedBlog = blogRepository.save(blog);

        // when
        blogRepository.delete(savedBlog);
        Optional<Blog> foundBlog = blogRepository.findById(savedBlog.getBlogId());

        // then
        assertThat(foundBlog).isEmpty();
    }

    @Test
    @DisplayName("블로그 업데이트 - 성공")
    void update_Success() {
        // given
        Blog blog = BlogFixture.createDefaultBlog();
        Blog savedBlog = blogRepository.save(blog);

        // when
        Blog foundBlog = blogRepository.findById(savedBlog.getBlogId()).orElseThrow();
        foundBlog.update("새로운 이름", "https://new-url.com", "새로운 설명");
        blogRepository.save(foundBlog);

        // then
        Blog updatedBlog = blogRepository.findById(savedBlog.getBlogId()).orElseThrow();
        assertThat(updatedBlog.getBlogName()).isEqualTo("새로운 이름");
        assertThat(updatedBlog.getBlogUrl()).isEqualTo("https://new-url.com");
        assertThat(updatedBlog.getDescription()).isEqualTo("새로운 설명");
    }

    @Test
    @DisplayName("빈 데이터베이스에서 findAll - 빈 리스트 반환")
    void findAll_EmptyDatabase_ReturnsEmptyList() {
        // when
        List<Blog> blogs = blogRepository.findAll();

        // then
        assertThat(blogs).isEmpty();
    }

    @Test
    @DisplayName("isActive가 null인 블로그 조회")
    void findByIsActive_NullValue() {
        // given
        Blog blog = BlogFixture.createDefaultBlog();
        blogRepository.save(blog);

        // when
        List<Blog> activeBlogs = blogRepository.findByIsActive(true);
        List<Blog> inactiveBlogs = blogRepository.findByIsActive(false);

        // then
        // isActive가 null인 경우 어느 쪽에도 포함되지 않음
        assertThat(activeBlogs).isEmpty();
        assertThat(inactiveBlogs).isEmpty();
    }
}
