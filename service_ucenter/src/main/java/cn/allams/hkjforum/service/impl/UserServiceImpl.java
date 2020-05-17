package cn.allams.hkjforum.service.impl;

import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.mapper.UserMapper;
import cn.allams.hkjforum.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
