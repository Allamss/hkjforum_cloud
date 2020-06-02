package cn.allams.hkjforum.service.impl;

import cn.allams.hkjforum.entity.HkjforumException;
import cn.allams.hkjforum.entity.User;
import cn.allams.hkjforum.entity.vo.CheckBindSmsVO;
import cn.allams.hkjforum.entity.vo.SendBindSmsVO;
import cn.allams.hkjforum.mapper.UserMapper;
import cn.allams.hkjforum.service.UserService;
import cn.allams.hkjforum.utils.JwtUtils;
import cn.allams.hkjforum.utils.MD5;
import cn.allams.hkjforum.utils.RedisUtils;
import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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

    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessSecret}")
    private String accessSecret;

    @Resource(name = "redisUtils")
    RedisUtils redisUtils;


    @Override
    public String login(User user) throws HkjforumException {
        //获取登陆用户名和密码
        String username = user.getUsername();
        String password = user.getPassword();

        // TODO: 这一步也许应该交由校验器进行判断
        //用户名和密码非空判断
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new HkjforumException(20001, "用户名密码不能为空");
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

    @Override
    public void register(User user) throws HkjforumException {
        //获取登陆用户名和密码
        String username = user.getUsername();
        String password = user.getPassword();

        // TODO: 这一步也许应该交由校验器进行判断
        //用户名和密码非空判断
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new HkjforumException(20001, "登陆失败");
        }

        //判断用户名是否已经存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new HkjforumException(20001, "用户名已经存在");
        }
        //密码MD5加密保存
        user.setPassword(MD5.encrypt(password));
        baseMapper.insert(user);
    }

    @Override
    public boolean existMobile(String mobile) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return true;
        }
        //不存在
        return false;
    }

    @Override
    public void sendBindSms(SendBindSmsVO sendBindSmsVO) {

        //生成随机6位验证码
        String code = String.valueOf(RandomUtil.randomInt(111111, 999999));

        //放入Redis中并设置5分钟后过期
        redisUtils.set("bind::" + sendBindSmsVO.getUserId(), code, 300);

        sendSms(sendBindSmsVO.getMobile(), code);
    }

    @Override
    public boolean checkBindSms(CheckBindSmsVO checkBindSmsVO) {

        String code = (String)redisUtils.get("bind::" + checkBindSmsVO.getUserId());

        //检验码不对
        if (code == null || !code.equals(checkBindSmsVO.getCode())) {
            return false;
        }

        //正确就清除缓存并保存（暂不清除缓存，因为安全原因删除键命令禁止）
        //redisUtils.del("bind::" + checkBindSmsVO.getUserId());

        User user = baseMapper.selectById(checkBindSmsVO.getUserId());
        user.setMobile(checkBindSmsVO.getMobile());
        baseMapper.updateById(user);
        return true;
    }

    /**
     * 发送sms短信
     *
     * @param mobile 手机号码
     * @param code 验证码
     */
    void sendSms(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "黑科技论坛");
        request.putQueryParameter("TemplateCode", "SMS_190785959");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+ code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
