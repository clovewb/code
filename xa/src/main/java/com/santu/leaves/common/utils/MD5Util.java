package com.santu.leaves.common.utils;

import java.security.MessageDigest;

/**
 * MD5加密
 * @Author LEAVES
 * @Date 2020/9/1
 * @Version 1.0
 */
public class MD5Util {
    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16){
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    // 测试
    public static void main(String args[]) {
        String s = "123xcx";
        String t = "a8761791a5f3e39dc27a0d7cd127d230";
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));
        System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));
        System.out.println("***********");
        System.out.println(string2MD5(s));
        System.out.println(convertMD5(s));

        System.out.println("**********/");
        String s1 = convertMD5(s);
        System.out.println(s1);
        System.out.println("---------");
        System.out.println(convertMD5(s1));


        String c = "EFG\f\u0017\f";

        System.out.println("**"+convertMD5(c));
        String d = "123456";
        System.out.println("-*-* ++"+ convertMD5(d));

    }

}
