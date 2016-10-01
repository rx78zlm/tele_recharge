package com.tele.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-26
 */
public class StreamUtil {

    public static void close(Closeable ... closeable) {
        for (Closeable closeable1 : closeable) {
            close(closeable1);
        }
    }

    public static void close(AutoCloseable ... closeable) {
        for (AutoCloseable closeable1 : closeable) {
            close(closeable1);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
