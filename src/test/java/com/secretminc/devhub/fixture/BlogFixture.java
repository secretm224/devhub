package com.secretminc.devhub.fixture;

import com.secretminc.devhub.api.blogs.dto.BlogCreateRequest;
import com.secretminc.devhub.api.blogs.dto.BlogUpdateRequest;
import com.secretminc.devhub.domain.blogs.Blog;

import java.lang.reflect.Constructor;
import java.time.LocalDate;

/**
 * 테스트용 Blog 엔티티 및 DTO 생성 Fixture 클래스
 */
public class BlogFixture {

    public static Blog createBlog(String blogId, String blogName, String blogUrl, String description) {
        return new Blog(blogId, blogName, blogUrl, description);
    }

    public static Blog createDefaultBlog() {
        return new Blog(
                "test-blog-1",
                "테스트 블로그",
                "https://test-blog.com",
                "테스트용 블로그 설명입니다."
        );
    }

    public static Blog createActiveBlog() {
        return createActiveBlog("active-blog-" + System.nanoTime());
    }

    public static Blog createActiveBlog(String blogId) {
        Blog blog = new Blog(
                blogId,
                "활성 블로그 " + blogId,
                "https://active-blog.com/" + blogId,
                "활성 상태의 블로그"
        );
        try {
            var field = Blog.class.getDeclaredField("isActive");
            field.setAccessible(true);
            field.set(blog, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set isActive", e);
        }
        return blog;
    }

    public static Blog createInactiveBlog() {
        return createInactiveBlog("inactive-blog-" + System.nanoTime());
    }

    public static Blog createInactiveBlog(String blogId) {
        Blog blog = new Blog(
                blogId,
                "비활성 블로그 " + blogId,
                "https://inactive-blog.com/" + blogId,
                "비활성 상태의 블로그"
        );
        try {
            var field = Blog.class.getDeclaredField("isActive");
            field.setAccessible(true);
            field.set(blog, false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set isActive", e);
        }
        return blog;
    }

    public static Blog createBlogWithAllFields() {
        Blog blog = createDefaultBlog();
        try {
            setField(blog, "logoUrl", "https://test-blog.com/logo.png");
            setField(blog, "company", "테스트 회사");
            setField(blog, "isActive", true);
            setField(blog, "totalArticleCount", 100);
            setField(blog, "newArticleCount", 5);
            setField(blog, "lastCrawledAt", LocalDate.now());
            setField(blog, "createdAt", LocalDate.now());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set fields", e);
        }
        return blog;
    }

    public static BlogCreateRequest createBlogCreateRequest() {
        try {
            BlogCreateRequest request = new BlogCreateRequest();
            setField(request, "blogId", "new-blog-1");
            setField(request, "blogName", "새로운 블로그");
            setField(request, "blogUrl", "https://new-blog.com");
            setField(request, "description", "새로 생성할 블로그 설명");
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create BlogCreateRequest", e);
        }
    }

    public static BlogCreateRequest createInvalidBlogCreateRequest() {
        try {
            BlogCreateRequest request = new BlogCreateRequest();
            setField(request, "blogId", "");
            setField(request, "blogName", "");
            setField(request, "blogUrl", "");
            setField(request, "description", "");
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create invalid BlogCreateRequest", e);
        }
    }

    public static BlogUpdateRequest createBlogUpdateRequest() {
        try {
            BlogUpdateRequest request = new BlogUpdateRequest();
            setField(request, "blogName", "수정된 블로그 이름");
            setField(request, "blogUrl", "https://updated-blog.com");
            setField(request, "description", "수정된 설명");
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create BlogUpdateRequest", e);
        }
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
