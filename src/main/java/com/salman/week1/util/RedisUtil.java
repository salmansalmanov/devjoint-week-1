package com.salman.week1.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T getResource(String key, Class<T> responseType) {
        String cachedData = redisTemplate.opsForValue().get(key);
        if (cachedData != null) {
            try {
                return objectMapper.readValue(cachedData, responseType);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while reading cached data", e);
            }
        }
        return null;
    }

    public void save(String key, Object data) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while caching data", e);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
