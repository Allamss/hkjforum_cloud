package cn.allams.hkjforum.service.impl;

import cn.allams.hkjforum.entity.Post;
import cn.allams.hkjforum.mapper.PostMapper;
import cn.allams.hkjforum.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Allams
 * @since 2020-06-01
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}
