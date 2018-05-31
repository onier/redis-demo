package com.example.redis.message;


import com.example.redis.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;


@Configuration
@EnableRedisRepositories
public class MessageConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public void sendData() {
        RedisConnection connection = redisConnectionFactory.getConnection();
        byte[] msg = "hello world".getBytes();
        byte[] channel = "test.chaneel".getBytes();

        connection.publish(msg, channel);
        connection.close();
    }


    @Component
    public static class DataCreatorTask {
        private final static Logger LOG = LoggerFactory.getLogger(DataCreatorTask.class);
        @Autowired
        private RedisTemplate redisTemplate;
        Random random = new Random(100);

        @Scheduled(cron = "*/5 * * * * ?")
        public void redisDataPublishTask() {
            User user = new User(
                    random.nextInt(100),
                    "zhang" + random.nextInt(),
                    "zhang@cincout.cn"
            );
            //redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));
            redisTemplate.convertAndSend("pinpoint.collector.channel", user);
            LOG.info("send user {} to redis channel.", user.toString());
        }
    }

}
