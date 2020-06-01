package cn.allams.hkjforum.controller;


import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.entity.vo.CheckBindSmsVO;
import cn.allams.hkjforum.entity.vo.SendBindSmsVO;
import cn.allams.hkjforum.service.UserService;
import cn.allams.hkjforum.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Allams
 * @since 2020-05-17
 */
@Api("个人中心模块控制器")
@RestController
@RequestMapping("/hkjforum/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    //TODO:图形验证码

    /**
     * 用户名密码登录
     *
     * @param user 登录用户表格
     * @return token JWT令牌
     */
    @ApiOperation("用户名登陆")
    @PostMapping("login")
    public CommonResult login(@RequestBody User user) {
        String token = null;
        try {
            token = userService.login(user);
        } catch (HkjforumException e) {
            return CommonResult.error().message(e.getMessage());
        }
        return CommonResult.ok().data("token", token);
    }

    @ApiOperation("用户名注册")
    @PostMapping("register")
    public CommonResult register(@RequestBody User user) {
        try {
            userService.register(user);
        } catch (HkjforumException e) {
            return CommonResult.error().message(e.getMessage());
        }
        return CommonResult.ok();
    }

    @ApiOperation("发送登陆短信")
    @PostMapping("/sms/send")
    public CommonResult mobileLogin(String mobile) {
        //未注册
        if (!userService.existMobile(mobile)) {
            return CommonResult.error().message("该手机未注册");
        }

        //已经注册
        //已经发过短信了


        return null;
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("userInfo")
    public CommonResult getUserInfo(HttpServletRequest request) {
        //调用JWT工具类的方法。根据request对象获取头信息，返回用户id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户ID获取用户信息
        User user = userService.getById(userId);
        return CommonResult.ok().data("userInfo", user);
    }

    @ApiOperation("测试能否进行服务调用")
    @GetMapping("echo")
    public String echo() {
        return "hello";
    }

    @ApiOperation("发送用户绑定手机号码短信")
    @PostMapping("bind/send")
    public CommonResult sendBindSms(@RequestBody SendBindSmsVO sendBindSmsVO) {
        userService.sendBindSms(sendBindSmsVO);
        return CommonResult.ok();
    }

    @ApiOperation("用户绑定手机号码")
    @PostMapping("bind/mobile")
    public CommonResult bindMobile(CheckBindSmsVO checkBindSmsVO) {
        if (userService.checkBindSms(checkBindSmsVO)) {
            return CommonResult.ok().message("绑定手机号成功");
        }
        return CommonResult.error().message("验证码不正确");
    }


}

