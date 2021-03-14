package com.clover.blog.dao;

import com.clover.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BlogDao {

    Blog findBlogById(String blogId);
    void insertBlog(Blog blog);
    Blog[] findBlogByUser(String userId);
    int updateBlogView(String blogId);
    int addBlogLike(String blogId);
    int decBlogLike(String blogId);
    int updateBlogVisible(String blogId, boolean visible);
    Blog[] findAll();
}
