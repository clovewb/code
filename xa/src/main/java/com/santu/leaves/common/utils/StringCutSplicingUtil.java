package com.santu.leaves.common.utils;

import java.util.List;

/**
 * 切割、拼接字符串
 * @Author LEAVES
 * @Date 2020/9/4
 * @Version 1.0
 */
public class StringCutSplicingUtil {

    /**
     * 字符串以英文逗号切割
     * @param path
     * @return
     */
    public static String[] stringCut(String path){
        //分割符是 . 或 | 等时，必须使用 \\ 进行转义
        String[] paths = path.split("\\,");
        return paths;
    }

    /**
     * 字符串以英文逗号拼接   list数组
     * @param paths
     * @return
     */
    public static String stringSplicing(List<String> paths){
        String str = "";
        for (String path : paths) {
            str = str + path + ",";
        }
        return str;


    }

    /**
     * 字符串以英文逗号拼接   String数组
     * @param paths
     * @return
     */
    public static String stringSplicing(String[] paths){
        String str = "";
        for (String path : paths) {
            str = str + path + ",";
        }
        return str;


    }




    public static void main(String[] args) {
        String str = "http://192.168.1.203/a/a.jpg,http://192.168.1.203/a/b.jpg,http://192.168.1.203/a/c.jpg,http://192.168.1.203/a/d.jpg,";
        //String[] strs = str.split("\\,");
        String[] strs = stringCut(str);

        for (String url : strs) {
            System.out.println(url.toString());
        }
    }
}
