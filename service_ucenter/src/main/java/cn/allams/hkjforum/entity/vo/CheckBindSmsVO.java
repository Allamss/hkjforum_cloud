package cn.allams.hkjforum.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检查短信验证码是否正确前端对象
 *
 * @author Allams
 */
@ApiModel("检查短信验证码是否正确前端对象")
@Data
public class CheckBindSmsVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("手机验证码")
    private String code;
}
