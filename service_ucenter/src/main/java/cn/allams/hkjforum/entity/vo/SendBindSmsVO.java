package cn.allams.hkjforum.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送短信前端对象
 *
 * @author Allams
 */
@ApiModel("发送短信前端对象")
@Data
public class SendBindSmsVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("要绑定的手机号码")
    private String mobile;

}
