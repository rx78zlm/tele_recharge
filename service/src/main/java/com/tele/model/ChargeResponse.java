package com.tele.model;

import lombok.Data;

/**
 * 充值响应信息
 * @author zhangleimin
 * @package com.tele.model
 * @date 16-9-26
 */
@Data
public class ChargeResponse {

    private boolean success;    // 成功
    private String accountNo;   // 分账序号
    private String cardNo;      // 卡号
    private String cardPwd;     // 卡密
    private String message;     // 充值结果
    private QueryResponse queryResponse;    // 卡信息
}
