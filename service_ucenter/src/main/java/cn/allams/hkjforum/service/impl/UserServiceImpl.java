package cn.allams.hkjforum.service.impl;

import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.mapper.UserMapper;
import cn.allams.hkjforum.service.UserService;
import cn.allams.hkjforum.utils.JwtUtils;
import cn.allams.hkjforum.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Allams
 * @since 2020-05-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(User user) throws HkjforumException {
        //获取登陆用户名和密码
        String username = user.getUsername();
        String password = user.getPassword();

        //用户名和密码非空判断
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new HkjforumException(20001, "登陆失败");
        }

        //判断用户名是否正确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User result = baseMapper.selectOne(queryWrapper);
        //判断查询对象是否为空
        if (result == null) {
            throw new HkjforumException(20001, "用户名或密码不能为空");
        }

        //判断密码
        if (!MD5.encrypt(password).equals(result.getPassword())) {
            throw new HkjforumException(20001, "密码错误");
        }

        //判断用户是否被禁用
        if (result.getIsDisable()) {
            throw new HkjforumException(20001, "用户已被禁用");
        }

        //登陆成功，生成token
        String token = JwtUtils.getJwtToken(result.getId() + "", result.getUsername());



        return token;
    }
}
