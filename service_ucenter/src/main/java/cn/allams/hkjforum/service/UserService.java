package cn.allams.hkjforum.service;

import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.entity.vo.CheckBindSmsVO;
import cn.allams.hkjforum.entity.vo.SendBindSmsVO;
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

    /**
     * 检查是否存在该手机号
     *
     * @param mobile 手机号码
     * @return 是否存在
     */
    boolean existMobile(String mobile);

    /**
     * 发送验证短信
     *
     * @param sendBindSmsVO 发短信对象
     */
    void sendBindSms(SendBindSmsVO sendBindSmsVO);

    /**
     * 校验短信验证码并绑定
     *
     * @param checkBindSmsVO 校验短信对象
     */
    boolean checkBindSms(CheckBindSmsVO checkBindSmsVO);
}
