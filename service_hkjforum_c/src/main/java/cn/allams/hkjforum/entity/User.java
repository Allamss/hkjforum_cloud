package cn.allams.hkjforum.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通用户实体
 *
 * @author Allams
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户表id")
    private Long id;

    @ApiModelProperty(value = "登陆用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "性别（男：0，女：1），默认男")
    private Boolean sex;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "邮件地址")
    private String email;

    @ApiModelProperty(value = "学号")
    private Integer account;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "论坛签名")
    private String sign;

    @ApiModelProperty(value = "是否被封禁")
    private Boolean isDisable;

    @ApiModelProperty(value = "是否被逻辑删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;


}