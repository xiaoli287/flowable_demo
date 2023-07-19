package com.example.flowable_web.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.flowable_web.enums.SexEnum;
import com.example.flowable_web.utils.EnumValidAnnotation;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserVo {
//    @ExcelProperty(value = "id")
//    private long id;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "年龄")
    private int age;
    @ExcelProperty(value = "邮箱")
    private String email;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @EnumValidAnnotation(message = "枚举类型值输入错误",target = SexEnum.class )
    @ExcelProperty(value = "性别")
    private String sex;

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
