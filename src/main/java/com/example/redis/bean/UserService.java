package com.example.redis.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, String> template;

    public List<User> list() {
        List<User> res = new ArrayList<>();
        userRepository.findAll().forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                res.add(user);

            }
        });
        return res;
    }

    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }
}
