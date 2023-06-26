package com.example.flowable_web.vo;


import lombok.Data;

@Data
public class TaskVO {

    private String id;
    private String name;
    private String assignee;
    private String processInstanceId;
    private String executionId;
    private String createTime;
    private String variables;
}
