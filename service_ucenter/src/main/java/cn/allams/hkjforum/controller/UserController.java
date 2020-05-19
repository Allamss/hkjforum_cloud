package cn.allams.hkjforum.controller;


import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.service.UserService;
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
@RestController
@RequestMapping("/hkjforum/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param user 登录用户表格
     * @return token
     */
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

}

