package cn.allams.hkjforum.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册前端接收对象
 *
 * @author Allams
 */
@ApiModel("注册接收对象")
@Data
public class RegisterVO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机验证码")
    private String code;
}
