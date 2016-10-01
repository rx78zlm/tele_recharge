package com.tele.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-29
 */
public class DirNameUtil {

    public static boolean isValidDir(String dirName) {
        return isValid(dirName, true);
    }

    public static boolean isValidFile(String dirName) {
        return isValid(dirName, false);
    }

    public static boolean isValid(String name, boolean isDir) {
        if (Strings.isNullOrEmpty(name)) {
            return false;
        }
        File dir = new File(name);
        return isDir ? dir.isDirectory() && dir.exists() : dir.isFile() && dir.exists();
    }

    public static String getDirName(String dirName) {
        if (!isValidDir(dirName)) {
            return "";
        } else {
            if (StringUtils.endsWith(dirName, File.separator)) {
                return dirName;
            } else {
                return dirName + File.separator;
            }
        }
    }
}
