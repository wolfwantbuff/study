package com.example.study.aop;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    public String getUserById(Long id) {
        System.out.println("执行业务方法: getUserById");
        return "User_" + id;
    }
}
