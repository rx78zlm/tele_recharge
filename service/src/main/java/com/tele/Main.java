package com.tele;

import com.tele.service.RechargeServiceAccount;

/**
 * @author zhangleimin
 * @package com.tele
 * @date 16-9-26
 */
public class Main {

    public static void main(String[] args) {
        RechargeServiceAccount service = new RechargeServiceAccount();
        String srcFile = "d:/tmp/test.xlsx";
        String msgFile = "d:/tmp/test_result.xlsx";
        service.recharge(srcFile, msgFile);
    }
}
