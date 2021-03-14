package com.clover.blog.cache;

import com.clover.blog.utils.ApplicationContextUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;

/**
 * 自定义cache
 */
public class RedisCache implements Cache {


    private final String id;
    private RedisTemplate getRedisTemplate () {
        RedisTemplate redisTemplate = (RedisTemplate)ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    private String getKeyToMD5 (String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // 容器构造，传入id=mapper的namespace
    public RedisCache(String id) {
        this.id = id;

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getRedisTemplate().opsForHash().put(id, getKeyToMD5(key.toString()), value);
    }

    @Override
    public Object getObject(Object key) {
        return getRedisTemplate().opsForHash().get(id, getKeyToMD5(key.toString()));
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return getRedisTemplate().opsForHash().size(id).intValue();
    }

}
