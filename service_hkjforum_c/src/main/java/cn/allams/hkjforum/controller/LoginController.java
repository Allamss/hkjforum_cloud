package cn.allams.hkjforum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
}
