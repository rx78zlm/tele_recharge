package com.tele.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-29
 */
public class CheckUtil {

    public static String[] MAC_ADDRESS = {
            "9C-4E-36-CB-0F-B4",
            "9C-4E-36-CB-0F-B3"
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
            return ArrayUtils.contains(MAC_ADDRESS, localMac);
        } catch (Exception e) {
            return false;
        }
    }
}
