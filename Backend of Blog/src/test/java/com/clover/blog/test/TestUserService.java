package com.clover.blog.test;

import com.clover.blog.BlogApplication;
import com.clover.blog.entity.User;
import com.clover.blog.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class TestUserService {

    @Autowired
    private UserService userService;

    @Test
    public void testReg() {
        User user = new User();
        user.setUserName("124323");
        user.setPassword("133223");
        user.setEmail("322322");
        JSONObject js = new JSONObject();
        js.put("email","3323");
        user.setContact(js);
        userService.register(user);
        System.out.println(user.getId());
    }
}
