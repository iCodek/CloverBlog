package com.clover.blog.config;


import com.clover.blog.interceptors.JwtInterceptor;
import com.clover.blog.interceptors.RedisInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Value("${user.img-path}")
//    private String userImgPath;
//    @Value("${user.blog-path}")
//    private String blogImgPath;
    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600 * 24);
    }

    /**
     * 添加静态资源--过滤
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/myapi/img/**")
//                .addResourceLocations("file:" + userImgPath);
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("file:" + userImgPath);
//        registry.addResourceHandler("**/img/**")
//                .addResourceLocations("file:" + userImgPath);
//        registry.addResourceHandler("/myapi/blogimg/**")
//                .addResourceLocations("file:" + blogImgPath);
//        registry.addResourceHandler("/blogimg/**")
//                .addResourceLocations("file:" + blogImgPath);
//        registry.addResourceHandler("**/blogimg/**")
//                .addResourceLocations("file:" + blogImgPath);
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/user/save**");
        registry.addInterceptor(new RedisInterceptor())
                .addPathPatterns("/**");
    }
}