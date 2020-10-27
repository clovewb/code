package com.santu.leaves.common.utils;

/**
 * 非空判断
 * @Author LEAVES
 * @Date 2020/8/31
 * @Version 1.0
 */
public class IsNotEmptyUtil {
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return (true);
        }
        if ("".equals(object)) {
            return (true);
        }
        if ("null".equals(object)) {
            return (true);
        }
        return (false);
    }

    public static boolean isNotEmpty(Object object) {
        if (object != null && !object.equals("") && !object.equals("null")) {
            return (true);
        }
        return (false);
    }
}
