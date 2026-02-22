-- 테스트용 Blog 테이블 스키마
CREATE TABLE IF NOT EXISTS blogs (
    blog_id VARCHAR(50) PRIMARY KEY,
    blog_name VARCHAR(200) NOT NULL,
    blog_url VARCHAR(500) NOT NULL,
    logo_url VARCHAR(500),
    description CLOB,
    company VARCHAR(100),
    is_active BOOLEAN,
    total_article_count INTEGER,
    new_article_count INTEGER,
    last_crawled_at DATE,
    created_at DATE
);
