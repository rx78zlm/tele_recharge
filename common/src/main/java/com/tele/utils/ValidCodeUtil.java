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

    public static String decrypt() {
        String str1 = "";
        try {
            String commons[] = {"D:\\getcode.exe", "d:\\tmpcode.jpg",
                    "d:\\tmpcode"};
            Process pro = Runtime.getRuntime().exec(commons);
            pro.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String str = "";
            fis = new FileInputStream("d:\\tmpcode.txt");
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
            fos = new FileOutputStream("d:\\tmpcode.jpg");
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
