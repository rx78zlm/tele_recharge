package com.tele.model;

import lombok.Data;

/**
 * @author zhangleimin
 * @package com.tele.model
 * @date 16-9-30
 */
@Data
public class QueryResponse {

    // 查询用户
    private String requestUser;
    // 充值卡卡号
    private String cardNumber;
    // 充值卡发卡地
    private String cardPublisher;
    // 充值卡状态
    private String cardStatus;
    // 充值卡有效期
    private String expirationTime;
    // 充值卡延长有效期
    private String prolongDays;
    // 充值卡余额
    private String cardValue;
    // 充值时间
    private String rechargeTime;
    // 主叫号码
    private String callingNumber;
    // 被充值用户的标识
    private String destinationAccount;
    // 被充值用户的属性
    private String destinationAttr;
}
