package com.tele.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 充值请求信息
 * @author zhangleimin
 * @package com.tele.model
 * @date 16-9-26
 */
@Data
public class ChargeRequest {

    @NotBlank(message = "分账序号不能为空")
    @Pattern(regexp = "[0-9]{11}",message = "分账序号格式有误")
    private String accountNo;   // 分账序号
    private String cardNo;      // 卡号
    @NotBlank(message = "卡密码不能为空")
    @Pattern(regexp = "[0-9]{18}",message = "卡密码格式有误")
    private String cardPwd;     // 卡密
}
