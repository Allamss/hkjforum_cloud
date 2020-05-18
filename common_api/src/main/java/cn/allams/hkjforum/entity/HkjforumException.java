package cn.allams.hkjforum.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 *
 * @author Allams
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HkjforumException extends Exception {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String message;
}
