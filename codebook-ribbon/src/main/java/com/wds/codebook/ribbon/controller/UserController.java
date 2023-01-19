package com.wds.codebook.ribbon.controller;

import com.wds.codebook.common.model.RespResult;
import com.wds.codebook.entity.bo.User;
import com.wds.codebook.entity.dto.UserReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("/getUser")
    public RespResult getUser(String name){

        User user = new User();
        user.setName(name);
        user.setAge(18);
        user.setSex(1);
        log.info("调用了这个方法。。。");
        return RespResult.success(user);
    }

    @PostMapping("/postUser")
    public RespResult postUser(@RequestBody UserReqDto dto){

        User user = new User();
        user.setName(dto.getName());
        user.setAge(18);
        user.setSex(1);
        log.info("调用了这个post方法。。。");
        return RespResult.success(user);
    }
}
