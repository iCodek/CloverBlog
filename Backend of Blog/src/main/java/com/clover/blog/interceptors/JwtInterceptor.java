package com.clover.blog.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.clover.blog.entity.Result;
import com.clover.blog.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result<String> result = Result.fail();
        try {
            String token = request.getHeader("Token");
            JwtUtils.verify(token);
            return true;
        } catch (NullPointerException | SignatureVerificationException e) {
            result.setMsg("请先登录");
        } catch (TokenExpiredException e) {
            result.setMsg("登录过期，请重新登录");
        } catch (AlgorithmMismatchException e) {
            result.setMsg("算法不一致");
        } catch (Exception e) {
            result.setMsg("token无效");
        }
        String json = new ObjectMapper().writeValueAsString(result);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Token");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
