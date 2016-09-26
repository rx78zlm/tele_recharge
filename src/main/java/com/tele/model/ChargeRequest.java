package com.tele.model;

import lombok.Data;

/**
 * 充值请求信息
 * @author zhangleimin
 * @package com.tele.model
 * @date 16-9-26
 */
@Data
public class ChargeRequest {

    private String accountNo;   // 分账序号
    private String cardNo;      // 卡号
    private String cardPwd;     // 卡密
}
