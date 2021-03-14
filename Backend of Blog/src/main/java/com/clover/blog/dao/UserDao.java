package com.clover.blog.dao;

import com.alibaba.fastjson.JSONObject;
import com.clover.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserDao {

    // 注册用户
    void register(User user);
    // 查找用户名返回ID
    String findByUserName(String userName);
    // 修改用户名
    int updateUserName(String id, String userName);
    // 查找ID返回User
    User findByUserID(String id);
    // 保存头像
    int updateAvatar(String id, String path);
    // 保存背景
    int updateBg(String id, String path);
    // 保存顶部背景
    int updateTop(String id, String path);
    // 保存联系方式
    int updateContact(String id, JSONObject contact);
    // 保存歌曲列表
    int updateSong(String id, List<String> song);
    // 保存歌曲列表
    int updateNotice(String id, String notice);
    // 保存API
    int updateApi(String id, String api);
    // 博客数目
    int updateBlogCount(String id);
    // 访问增加
    int updateView(String id);
    // 获取所有用户信息
    User[] allUser();

}
