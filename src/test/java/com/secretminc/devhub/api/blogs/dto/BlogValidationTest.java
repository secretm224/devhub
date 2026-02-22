package com.secretminc.devhub.api.blogs.dto;

import com.secretminc.devhub.fixture.BlogFixture;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Blog DTO 유효성 검사 테스트")
class BlogValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("BlogCreateRequest - 모든 필수 필드가 있으면 검증 성공")
    void blogCreateRequest_ValidData_PassesValidation() {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("BlogCreateRequest - blogId가 null이면 검증 실패")
    void blogCreateRequest_NullBlogId_FailsValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        var field = request.getClass().getDeclaredField("blogId");
        field.setAccessible(true);
        field.set(request, null);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("blogId");
    }

    @Test
    @DisplayName("BlogCreateRequest - blogId가 빈 문자열이면 검증 실패")
    void blogCreateRequest_BlankBlogId_FailsValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        var field = request.getClass().getDeclaredField("blogId");
        field.setAccessible(true);
        field.set(request, "");

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("BlogCreateRequest - blogName이 null이면 검증 실패")
    void blogCreateRequest_NullBlogName_FailsValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        var field = request.getClass().getDeclaredField("blogName");
        field.setAccessible(true);
        field.set(request, null);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("BlogCreateRequest - blogUrl이 null이면 검증 실패")
    void blogCreateRequest_NullBlogUrl_FailsValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        var field = request.getClass().getDeclaredField("blogUrl");
        field.setAccessible(true);
        field.set(request, null);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("BlogCreateRequest - description이 500자를 초과하면 검증 실패")
    void blogCreateRequest_DescriptionTooLong_FailsValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        String longDescription = "a".repeat(501);
        var field = request.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(request, longDescription);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("500");
    }

    @Test
    @DisplayName("BlogCreateRequest - description이 정확히 500자이면 검증 성공")
    void blogCreateRequest_DescriptionExactly500Chars_PassesValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        String maxDescription = "a".repeat(500);
        var field = request.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(request, maxDescription);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("BlogCreateRequest - description이 null이면 검증 성공 (선택적 필드)")
    void blogCreateRequest_NullDescription_PassesValidation() throws Exception {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();
        var field = request.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(request, null);

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("BlogCreateRequest - 모든 필수 필드가 빈 문자열이면 검증 실패")
    void blogCreateRequest_AllRequiredFieldsBlank_FailsValidation() {
        // given
        BlogCreateRequest request = BlogFixture.createInvalidBlogCreateRequest();

        // when
        Set<ConstraintViolation<BlogCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(3); // blogId, blogName, blogUrl
    }

    @Test
    @DisplayName("BlogUpdateRequest - 모든 필드가 null이어도 검증 성공 (현재 검증 없음)")
    void blogUpdateRequest_AllFieldsNull_PassesValidation() {
        // given
        BlogUpdateRequest request = new BlogUpdateRequest();

        // when
        Set<ConstraintViolation<BlogUpdateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("BlogUpdateRequest - 유효한 데이터로 검증 성공")
    void blogUpdateRequest_ValidData_PassesValidation() {
        // given
        BlogUpdateRequest request = BlogFixture.createBlogUpdateRequest();

        // when
        Set<ConstraintViolation<BlogUpdateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("BlogCreateRequest - toEntity() 메서드 테스트")
    void blogCreateRequest_ToEntity_CreatesValidBlog() {
        // given
        BlogCreateRequest request = BlogFixture.createBlogCreateRequest();

        // when
        var blog = request.toEntity();

        // then
        assertThat(blog).isNotNull();
        assertThat(blog.getBlogId()).isEqualTo(request.getBlogId());
        assertThat(blog.getBlogName()).isEqualTo(request.getBlogName());
        assertThat(blog.getBlogUrl()).isEqualTo(request.getBlogUrl());
        assertThat(blog.getDescription()).isEqualTo(request.getDescription());
    }
}
