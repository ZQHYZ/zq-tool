package com.zqhyz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqhyz.entity.User;
import com.zqhyz.mapper.UserMapper;
import com.zqhyz.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
