package com.example.flowable_web.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.flowable_web.entity.User;
import com.example.flowable_web.mapper.UserMapper;
import com.example.flowable_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

    @Autowired
    private UserMapper userMapper;
    public Page<User> getlist() {
        Page<User> page = new LambdaQueryChainWrapper<>(userMapper)
                .page(new Page<User>(1, 10));
        return page;
    }

    public User insert(User user) {
        userMapper.insert(user);
        return user;
    }

    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    public int delete(User user) {
        return userMapper.deleteById(user);
    }

    public User getbyId(long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void insertbath(List<User> users) {
        super.saveBatch(users);
    }

}
