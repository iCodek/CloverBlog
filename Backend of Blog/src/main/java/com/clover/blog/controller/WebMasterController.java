package com.clover.blog.controller;

import com.clover.blog.entity.Result;
import com.clover.blog.entity.User;
import com.clover.blog.service.UserService;
import com.clover.blog.utils.OtherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("webmaster")
@CrossOrigin
@Slf4j
public class WebMasterController {

    private final StringRedisTemplate redisTemplate;
    private final UserService userService;

    public WebMasterController(UserService userService, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("info")
    @ResponseBody
    public Result<User> info(String id, HttpServletRequest request){
        if (id == null)
            log.info("获取站长信息");
        else
            log.info("获取用户"+id+"信息");
        try {
            if (id != null) {
                User userDB = userService.findUserById(id);
                if (userDB != null) {
                    userDB.setPassword("");
                    userService.addView(userDB.getId());
                    return Result.success("用户"+id+"信息", userDB);
                }
            }
            User webMaster = userService.findUserById("0");
            webMaster.setPassword("");
            userService.addView(webMaster.getId());
            return Result.success("站长信息", webMaster);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("allUser")
    @ResponseBody
    public Result<User[]> allUser(){
        log.info("获取用户列表");
        try {
            User[] users = userService.allUser();
            return Result.success("全部用户信息", users);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
    
    @GetMapping("visit")
    @ResponseBody
    public String info(HttpServletRequest request){
        return setStyle(request);
    }
    public String setStyle(HttpServletRequest request) {
        String[] styles = new String[]{"anime", "Ghibli", "cyberpunk", "nature"};
        String type = styles[new Random().nextInt(styles.length)];
        redisTemplate.opsForValue().set(OtherUtils.getIpAddr(request), type, 600, TimeUnit.SECONDS);
        return type;
    }


}
