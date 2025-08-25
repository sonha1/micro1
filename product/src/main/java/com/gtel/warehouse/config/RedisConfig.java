package com.gtel.warehouse.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.File;
import java.io.IOException;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Autowired
    Environment env;


    @Bean
    public RedissonClient createRedissionClient() throws IOException {
//        String fileUrl = env.getProperty("redis.config-file");
//
//        if (StringUtils.isBlank(fileUrl)) {
//            fileUrl = "redis";
//        }
//        Config config = Config.fromYAML(new File(fileUrl));
//        return Redisson.create(config);


        String fileUrl = env.getProperty("redis.config-file");
        if (StringUtils.isBlank(fileUrl)) {
            fileUrl = "redis_single.yaml";
        }
        Config config = Config.fromYAML(new File(fileUrl));
        return Redisson.create(config);
    }

    @Bean("redisTemplateString")
    RedisTemplate<String, String> redisTemplateString(@Autowired RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redissonConnectionFactory);

        ObjectMapper om = new ObjectMapper();

        // support Java 8 date time apis
        om.registerModule(new JavaTimeModule());
        Jackson2JsonRedisSerializer<String> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om, String.class);
        template.setValueSerializer(jsonRedisSerializer);
        return template;
    }


    @Bean
    RedisTemplate<String, Object> redisTemplate(@Autowired RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redissonConnectionFactory);

        ObjectMapper om = new ObjectMapper();

        // support Java 8 date time apis
        om.registerModule(new JavaTimeModule());
        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(om, Object.class);
        template.setValueSerializer(jsonRedisSerializer);
        return template;
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(@Autowired RedissonClient redissonClient ) {
        return new RedissonConnectionFactory(redissonClient);
    }

}