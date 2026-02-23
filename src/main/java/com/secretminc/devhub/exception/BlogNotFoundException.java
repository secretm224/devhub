package com.secretminc.devhub.exception;

public class BlogNotFoundException extends RuntimeException {

    public BlogNotFoundException(String blogId) {
        super("Blog not found: " + blogId);
    }
}
