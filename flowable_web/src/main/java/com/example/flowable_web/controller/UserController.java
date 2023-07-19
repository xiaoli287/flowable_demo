package com.example.flowable_web.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.flowable_web.entity.User;
import com.example.flowable_web.enums.SexEnum;
import com.example.flowable_web.service.UserService;
import com.example.flowable_web.vo.UserVo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/getList")
    public String getList(User user){
        Page<User> users = userService.getlist();
        return JSONUtil.toJsonStr(users);
    }

    @PostMapping("/insert")
    public String insert(User user){
        User users = userService.insert(user);
        return JSONUtil.toJsonStr(users);
    }

    @GetMapping("/export")
    public String export(){
        Page<User> users = userService.getlist();
        List<User> a = users.getRecords();
        List<UserVo> b = new ArrayList<>();
        BeanUtils.copyProperties(a,b);
        for(User user : a){
            UserVo userVo = new UserVo();
//            userVo.setId(user.getId());
            userVo.setAge(user.getAge());
            userVo.setName(user.getName());
            userVo.setSex(SexEnum.getByValue(user.getSex().toString()).getKey());
            userVo.setEmail(user.getEmail());
            userVo.setCreateTime(user.getCreateTime());
            b.add(userVo);
        }
        String filename = "userExcel_"+ UUID.randomUUID()+".xls";
        EasyExcel.write("D:\\home\\" + filename, UserVo.class).sheet("用户信息").doWrite(b);
        return "导出成功";
    }

    @GetMapping("/imports")
    public String imports(@RequestParam("file") MultipartFile file) throws IOException {
        String filepath = "D:\\home\\"+file.getName();
        File files = new File(filepath);
        FileUtils.copyInputStreamToFile(file.getInputStream(), files);
        List<UserVo> list = EasyExcel.read(filepath).head(UserVo.class).sheet().doReadSync();
        System.out.println(JSONUtil.toJsonStr(list));
        List<User> users = new ArrayList<>();
        for(UserVo userVo :list){
            User user = new User();
            SexEnum sex = SexEnum.getByKey(userVo.getSex());
            user.setSex(Integer.parseInt(sex.getValue()));
            user.setName(userVo.getName());
            user.setEmail(userVo.getEmail());
            user.setCreateTime(new Date());
            user.setAge(userVo.getAge());
            users.add(user);
        }
        userService.insertbath(users);
        return "导入成功";
    }

}
