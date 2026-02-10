package com.secretminc.devhub.domain.blogs;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Entity
@Table(name="blogs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog {

    @Id
    @Column(name="blog_id",length = 50,nullable = false)
    private String blogId;

    @Column(name="blog_name",length=200,nullable = false)
    private String blogName;

    @Column(name="blog_url",length=500,nullable = false)
    private String blogUrl;

    @Column(name="logo_url",length=500)
    private String logoUrl;

    @Lob
    @Column(name="description")
    private String description;

    @Column(name="company",length=100)
    private String company;

    @Column(name="is_active")
    private Boolean isActive;

    @Column(name="total_article_count")
    private Integer totalArticleCount;

    @Column(name="new_article_count")
    private Integer newArticleCount;

    @Column(name="last_crawled_at")
    private LocalDate lastCrawledAt;

    @Column(name="created_at")
    private LocalDate createdAt;


    public Blog(String blogId, String blogName, String blogUrl, String description) {
        this.blogId = blogId;
        this.blogName = blogName;
        this.blogUrl = blogUrl;
        this.description = description;
    }

    public void update(String blogName, String blogUrl, String description) {
        this.blogName = blogName;
        this.blogUrl = blogUrl;
        this.description = description;
    }

}
