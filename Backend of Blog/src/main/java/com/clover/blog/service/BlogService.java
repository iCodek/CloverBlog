package com.clover.blog.service;

import com.clover.blog.elasticsearch.entity.EsBlog;
import com.clover.blog.entity.Blog;

import java.io.IOException;
import java.util.List;

public interface BlogService {
    Blog findBlogById(String blogId);
    void addBlog(Blog blog);
    Blog[] findBlogByUser(String userId);
    int addView(String blogId);
    int changeLike(String blogId, boolean add);
    int changeBlogVisible(String blogId, boolean visible);
    Blog[] findAll();
    List<EsBlog> getSearchResult(Integer userId, String keyword) throws IOException;
}
