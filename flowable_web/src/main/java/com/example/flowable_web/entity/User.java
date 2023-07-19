package com.example.flowable_web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "user")
public class User {
    private long id;
    private String name;
    private int age;
    private String email;
    private Date createTime;
    private Integer sex;
}
