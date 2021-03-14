package com.clover.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {
    private String blogId;
    private String userId;
    private String title;
    private String preview;
    private String uploadTime;
    private String category;
    private String blogFile;
    private List<String> tags;
    private int likes;
    private int comments;
    private int views;
    private boolean visible;
}
