package com.example.flowable_web.service;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgServiceImpl implements JavaDelegate {
    @Autowired
    TaskService taskService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("请假失败短信处理成功");
    }
}
