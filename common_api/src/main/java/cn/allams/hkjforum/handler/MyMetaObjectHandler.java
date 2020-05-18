package cn.allams.hkjforum.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mybatis Plus时间拦截器
 *
 * @author Allams
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 添加时自动赋值
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("is_deleted", 0, metaObject);
    }

    /**
     * 更新时自动自动赋值
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }


}
