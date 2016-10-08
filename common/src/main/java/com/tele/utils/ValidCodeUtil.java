package com.tele.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-29
 */
public class ValidCodeUtil {

    public static final String WORK_DIR = System.getProperty("user.dir") + "\\";
//    private static final String WORK_DIR = "d:\\";
    public static final String EXE_PATH = WORK_DIR + "getcode.exe";
    public static final String PIC_PATH = WORK_DIR + "tmpcode.jpg";
    public static final String TXT_PATH = WORK_DIR + "tmpcode.txt";
    public static final String TMP_PATH = WORK_DIR + "tmpcode";

    public static String decrypt() {
        String str1 = "";
        try {
            String commons[] = {EXE_PATH, PIC_PATH, TMP_PATH};
            /*
            String commons[] = { "D:\\getcode.exe", "d:\\tmpcode.jpg",
                    "d:\\tmpcode"};
            */
            Process pro = Runtime.getRuntime().exec(commons);
            pro.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String str;
            fis = new FileInputStream(TXT_PATH);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            while ((str = br.readLine()) != null) {
                str1 += str + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                StreamUtil.close(br, isr, fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (str1.trim());
    }

    public static void saveToFile(String destUrl) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            URL url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(PIC_PATH);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fos, bis);
            try {
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        decrypt();
    }

    public static void main(String[] args) {
//        String s = saveToFile("http://service.sh.189.cn/service/createValidate.do?key=validateName&time=0.9229398625152292");
//        System.out.println(s);
    }
}
