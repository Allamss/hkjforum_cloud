package cn.allams.hkjforum.service;

import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Allams
 * @since 2020-05-17
 */
public interface UserService extends IService<User> {

    /**
     * 用户名密码登录
     *
     * @param user 用户信息
     * @exception HkjforumException 自定义异常
     * @return token
     */
    String login(User user) throws HkjforumException;

    /**
     * 用户注册
     *
     * @param user 注册信息
     */
    void register(User user) throws HkjforumException;
}
