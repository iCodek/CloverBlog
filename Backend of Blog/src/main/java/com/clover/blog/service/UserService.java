package com.clover.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.clover.blog.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    void register(User user);
    User login(User user);
    User findUserById(String id);
    boolean findId(String id);
    int saveAvatar(String id, String path);
    int saveBg(String id, String path);
    int saveTop(String id, String path);
    int saveContact(String id, JSONObject contact);
    int saveSong(String id, List<String> song);
    int saveNotice(String id, String notice);
    int saveApi(String id, String api);
    int addBlogCount(String id);
    int addView(String id);
    User[] allUser();
    int changeUserName(String id, String userName);

}
