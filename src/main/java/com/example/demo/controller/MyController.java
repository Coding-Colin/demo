package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class MyController {

    @Resource
    UserMapper userMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Cacheable(value = "get", key = "#root.methodName")
    @RequestMapping("/get")
    public String get() throws Exception{
        List<User> list = userMapper.getAll();
        return list.toString();
    }


    @RequestMapping("/add")
    @CacheEvict(value = "add", key = "#root.methodName")
    public String add() throws Exception{
        //jdbc
        User user = new User("hujinghan","M");
        userMapper.add(user);
        //rabbitmq
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",String.valueOf(UUID.randomUUID()));
        map.put("messageData","test message, hello!");
        map.put("createTime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rabbitTemplate.convertAndSend("directExchange", "directRouting", map);
        return user.getId().toString();
    }

    @Cacheable(value = "update", key = "#root.methodName")
    @RequestMapping("/update")
    public String update() throws Exception{
        Integer id = userMapper.update(new User(1,"zhangjiaying","W"));
        return id.toString();
    }

    @Cacheable(value = "delete", key = "#root.methodName")
    @RequestMapping("/delete")
    public String delete() throws Exception{
        Integer id = userMapper.delete(1);
        return id.toString();
    }
}
