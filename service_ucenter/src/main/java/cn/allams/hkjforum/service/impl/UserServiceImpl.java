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
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Allams
 * @since 2020-05-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 阿里云accessKey
     */
    @Value("${accessKeyId}")
    private String accessKeyId;

    /**
     * 阿里云accessSecret
     */
    @Value("${accessSecret}")
    private String accessSecret;

    /**
     * 阿里云OSS地区endPoint
     */
    @Value("${endPoint}")
    private String endPoint;

    /**
     * 阿里云OSS bucket名
     */
    @Value("${bucketName}")
    private String bucketName;

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

    @Override
    public String uploadAvatar(MultipartFile file, Long userId) {
        String uploadUrl = null;
        try {
            //获取上传文件流
            InputStream inputStream = file.getInputStream();

            //构建日期路径
            String filePath = "avatar/" + new DateTime().toString("yyyy/MM/dd");

            //文件名：UUID.扩展名
            String original = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fileType = original.substring(original.lastIndexOf("."));
            String newName = fileName + fileType;
            String fileUrl = filePath + "/" + newName;

            //文件上传至阿里云OSS
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessSecret);
            ossClient.putObject(bucketName, fileUrl, inputStream);
            ossClient.shutdown();

            //获取url地址
            uploadUrl = "http://" + bucketName + "." + endPoint + "/" + fileUrl;

            //保存头像路径到数据库
            User user = baseMapper.selectById(userId);
            user.setAvatar(uploadUrl);
            baseMapper.updateById(user);
       } catch (IOException e) {
            log.error("上传头像IO异常");
            return null;
        }
        return uploadUrl;
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
