package cn.allams.hkjforum.controller;


import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}

