package com.clover.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.clover.blog.dao.UserDao;
import com.clover.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImp implements UserService {

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private final UserDao userDao;

    public UserServiceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void register(User user) {
        if (userDao.findByUserName(user.getUserName()) == null){
            userDao.register(user);
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }

    @Override
    public User login(User user) {
        String id = userDao.findByUserName(user.getUserName());
        if (id!=null){
            User userDB = userDao.findByUserID(id);
            if (userDB.getPassword().equals(user.getPassword())){
                return userDB;
            } else {
                throw new RuntimeException("密码错误");
            }
        } else {
            User userDB = userDao.findByUserID(user.getUserName());
            if (userDB != null) {
                if (userDB.getPassword().equals(user.getPassword())){
                    return userDB;
                } else {
                    throw new RuntimeException("密码错误");
                }
            } else {
                throw new RuntimeException("用户名输入错误");
            }
        }
    }

    @Override
    public User findUserById(String id) {
        return findId(id) ? userDao.findByUserID(id) : null;
    }

    @Override
    public boolean findId(String id) {
        return id!=null && !id.equals("") && isInteger(id) && userDao.findByUserID(id) != null;
    }

    @Override
    public int saveAvatar(String id, String path) {
        return userDao.updateAvatar(id, path);
    }

    @Override
    public int saveBg(String id, String path) {
        return userDao.updateBg(id, path);
    }

    @Override
    public int saveTop(String id, String path) {
        return userDao.updateTop(id, path);
    }

    @Override
    public int saveContact(String id, JSONObject contact) {
        return userDao.updateContact(id, contact);
    }

    @Override
    public int saveSong(String id, List<String> song) {
        return userDao.updateSong(id, song);
    }

    @Override
    public int saveNotice(String id, String notice) {
        return userDao.updateNotice(id, notice);
    }

    @Override
    public int saveApi(String id, String api) {
        return userDao.updateApi(id, api);
    }

    @Override
    public int addBlogCount(String id) {
        return userDao.updateBlogCount(id);
    }

    @Override
    public int addView(String id) {
        return userDao.updateView(id);
    }

    @Override
    public User[] allUser() {
        return userDao.allUser();
    }

    @Override
    public int changeUserName(String id, String userName) {
        String idDB = userDao.findByUserName(userName);
        if (idDB!=null){
            return 0;
        } else {
            return userDao.updateUserName(id, userName);
        }
    }
}
