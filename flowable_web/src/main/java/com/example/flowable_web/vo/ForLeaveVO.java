package com.example.flowable_web.vo;

import lombok.Data;

/**
 * 请假参数
 */
@Data
public class ForLeaveVO {
    //当前登陆人id
    private String staffId;
    //当前登陆人名称
    private String name;
    //请假备注
    private String reason;
    //请假天数
    private String days;

}
