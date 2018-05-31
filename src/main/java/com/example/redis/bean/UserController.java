package com.example.redis.bean;

import com.example.redis.queue.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired
    MessagePublisher messagePublisher;
     

    public UserController() {
    }

    @RequestMapping(path = {"addUser", "add"}, method = {RequestMethod.POST})
    public String addUser(User user) {
        userService.addUser(user);
        return "sucess";
    }

    @RequestMapping(path = {"list", "li"})
    public List<User> list() {
        return userService.list();
    }

    @RequestMapping("/find/{id}")
    public User findUser(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @RequestMapping("/get/{key}")
    public String values(@PathVariable("key") String key) {
        return template.opsForValue().get(key);
    }

    @RequestMapping("/add/{key}/{value}")
    public void setValue(@PathVariable String key, @PathVariable String value) {
        template.opsForValue().set(key, value);
    }

    @RequestMapping("/push")
    public void pushMessage(@RequestParam("message") String message) {
        messagePublisher.publish(UUID.randomUUID().toString() + ":" + message);
    }
}

