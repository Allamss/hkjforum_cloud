package cn.allams.hkjforum.controller;

import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 登陆控制器
 *
 * @author Allams
 */
@RestController
@Slf4j
public class LoginController {
    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.service-ucenter}")
    private String serviceUrl;

    @PostMapping("/login")
    public CommonResult loginByUsername(@RequestBody User user) {
        return restTemplate.postForObject(serviceUrl+"/hkjforum/user/login", user, CommonResult.class);
    }

    @GetMapping("/hello")
    public String hello() {
        return restTemplate.getForObject(serviceUrl+"/hkjforum/user/echo", String.class);
    }

}
