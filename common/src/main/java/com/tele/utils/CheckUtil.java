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
            "9C-4E-36-CB-0F-B4",
            "3C-97-0E-C5-82-05",
            "00-0E-C6-C5-00-FF",
            "F8-16-54-29-74-67"
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
