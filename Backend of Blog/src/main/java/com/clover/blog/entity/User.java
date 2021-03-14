package com.clover.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private String id;
    private String userName;
    private String password;
    private String email;
    private int blogCount;
    private int comments;
    private int views;
    private String avatar;
    private String userBg;
    private String userTop;
    private String notice;
    private String api;
    private List<String> song;
    private JSONObject contact;
}
