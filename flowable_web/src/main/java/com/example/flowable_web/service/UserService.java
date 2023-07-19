package com.example.flowable_web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.flowable_web.entity.User;

import java.util.List;

public interface UserService {
    public Page<User> getlist();

    public User insert(User user);
    User update(User user);
    int delete(User user);
    User getbyId(long id);

    void insertbath(List<User> users);
}
