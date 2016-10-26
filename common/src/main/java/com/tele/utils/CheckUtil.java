package com.tele.utils;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ArrayUtils;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-29
 */
@Log4j
public class CheckUtil {

    public static String[] MAC_ADDRESS = {
            "9C-4E-36-CB-0F-B4",    // zlm_work_eth0
            "3C-97-0E-C5-82-05",    // zlm_work_wire
            "00-0E-C6-C5-00-FF",    // gaoji_etho
            "F8-16-54-29-74-67",    // gaoji_wire
            "00-E0-4C-04-AA-C9",    // 程
            "00-E0-4C-04-AA-C6",    // 李
            "00-E0-4C-04-AA-C4",    // 胡
            "00-E0-4C-04-AB-F1",    // 张
            "00-E0-4C-CD-83-80",    // 猫
            "1C-1B-0D-01-E4-BA",    // 吴
            "00-E0-4C-04-A9-94",    // 杨
            "00-10-4C-4D-71-69",    // 主
            "00-23-56-6C-68-44",    // 朱
            "C4-8E-8F-F4-61-4D",    // 家
            "00-E0-4C-04-AA-C4"     // 黑
    };

    public static String getMacAddress() throws Exception {
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        while (ni.hasMoreElements()) {
            NetworkInterface netI = ni.nextElement();
            byte[] bytes = netI.getHardwareAddress();
            if (netI.isUp() && netI != null && bytes != null && bytes.length == 6) {
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) {
                    sb.append(Integer.toHexString((b & 240) >> 4));
                    sb.append(Integer.toHexString(b & 15));
                    sb.append("-");
                }
                sb.deleteCharAt(sb.length() - 1);
                return sb.toString().toUpperCase();
            }
        }
        return null;
    }

    public static boolean checkMacAddress() {
        try {
            String localMac = getMacAddress();
            boolean result = ArrayUtils.contains(MAC_ADDRESS, localMac);
            if (!result) {
                log.info(String.format("mac address is error,%s", localMac));
            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }
}
