package com.example.flowable_web.vo;

import lombok.Data;

/**
 * 已完成的流程返回
 */
@Data
public class FinishedTaskVO {
    //流程id
    private String id;
    //流程名称
    private String name;
    //发起用户id
    private String startUserId;
    //启动流程id
    private String startActivityId;
    //进程实例ID
    private String processInstanceId;
    //结束流程id
    private String endActivityId;
    //启动时间
    private String startTime;
    //结束时间
    private String endTime;
    //流程变量
    private String variables;
}
