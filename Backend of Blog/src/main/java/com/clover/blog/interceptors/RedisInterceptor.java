package com.clover.blog.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.clover.blog.entity.Result;
import com.clover.blog.utils.ApplicationContextUtils;
import com.clover.blog.utils.JwtUtils;
import com.clover.blog.utils.OtherUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class RedisInterceptor  implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result<String> result = Result.fail();
        RedisTemplate<String, String> redisTemplate = getRedisTemplate();
        String count = redisTemplate.opsForValue().get("COUNT_"+OtherUtils.getIpAddr(request));
        if (count == null) {
            redisTemplate.opsForValue().set("COUNT_"+OtherUtils.getIpAddr(request), "1", 4 , TimeUnit.SECONDS);
            return true;
        } else {
            int times = Integer.parseInt(count);
            count = times + 1 + "";
            redisTemplate.opsForValue().set("COUNT_"+OtherUtils.getIpAddr(request), count, 4 , TimeUnit.SECONDS);
            if (times < 40) return true;
        }
        result.setMsg("访问频繁");
        String json = new ObjectMapper().writeValueAsString(result);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Token");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    private RedisTemplate<String, String> getRedisTemplate () {
        RedisTemplate<String, String> redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}

