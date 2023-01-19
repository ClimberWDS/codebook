package com.wds.codebook.ribbon.controller;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.wds.codebook.common.model.RespResult;
import com.wds.codebook.common.utils.AllUtils;
import com.wds.codebook.common.utils.RestTemplateUtils;
import com.wds.codebook.entity.dto.UserReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AllUtils allUtils ;

    @GetMapping("/getUser")
    public RespResult getUser(String name){
        return restTemplate.getForObject("http://" + "codebook-ribbon" + "/ribbon/user/getUser?name=" + name, RespResult.class);
    }

    @PostMapping("/postUser")
    public RespResult postUser(@RequestBody UserReqDto userReqDto){
        MultiValueMap<String,Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add ("name ",userReqDto.getName());
        return restTemplate.postForObject("http://" + "codebook-ribbon" + "/ribbon/user/postUser",userReqDto, RespResult.class);
    }

    @PostMapping("/postEnties")
    public RespResult postEnties(@RequestBody UserReqDto userReqDto){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        String url = "http://" + "codebook-ribbon" + "/ribbon/user/postUser";
        HttpEntity<UserReqDto> entityParam =  new HttpEntity<UserReqDto>(userReqDto,headers) ;
        ResponseEntity<RespResult> result = restTemplate.postForEntity(url, entityParam,RespResult.class);
        return result.getBody();
    }

    @PostMapping("/postEnties1")
    public RespResult postEnties1(@RequestBody UserReqDto userReqDto){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        String url = "http://" + "codebook-ribbon" + "/ribbon/user/postUser";


        final ResponseEntity<RespResult> post = allUtils.post(url, userReqDto, RespResult.class);
        return post.getBody();
    }

    @PostMapping("/exchange")
    public RespResult exchange(@RequestBody UserReqDto userReqDto){
        String url = "http://" + "codebook-ribbon" + "/ribbon/user/postUser";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        HttpEntity<UserReqDto> entityParam =  new HttpEntity<UserReqDto>(userReqDto,headers) ;

        ResponseEntity<RespResult> result =restTemplate.exchange(url, HttpMethod.POST,entityParam, RespResult.class);
        System.out.println(result.getBody());
        String url1 = "http://" + "codebook-ribbon" + "/ribbon/user/getUser";
        HttpEntity<String> entityParam1 =  new HttpEntity<String>(userReqDto.getName(),headers) ;
        ResponseEntity<RespResult> result1 =restTemplate.exchange(url1, HttpMethod.GET,entityParam1, RespResult.class);
        System.out.println(result1.getBody());
        return result1.getBody();
    }

}
