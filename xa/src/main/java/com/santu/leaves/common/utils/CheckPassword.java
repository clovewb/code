package com.santu.leaves.common.utils;

/**
 * 密码正则判断 密码为6-12位字母+数字+特殊字符!
 * @Author LEAVES
 * @Date 2020/10/22
 * @Version 1.0
 */

public class CheckPassword {
    public static boolean checkPassword(String passwordStr) {
        if (passwordStr != null && !"".equals(passwordStr) && (passwordStr.length() < 6 || passwordStr.length() > 12)) {
//            return "密码为 6-12 位字母、数字或英文字符!";
            return false;
        }

        // Z = 字母       S = 数字           T = 特殊字符
        String regexZ = "[A-Za-z]+";
        String regexS = "^\\d+$";
        String regexT = "[~!@#$%^&*.]+";
        String regexZT = "[a-zA-Z~!@#$%^&*.]+";
        String regexZS = "[0-9A-Za-z]+";
        String regexST = "[\\d~!@#$%^&*.]*";
        String regexZST = "[\\da-zA-Z~!@#$%^&*.]+";

//        if (passwordStr.matches(regexZ)){
//            return "纯字母，弱";
//        }
//        if (passwordStr.matches(regexS)){
//            return "纯数字，弱";
//        }
//        if (passwordStr.matches(regexT)){
//            return "纯字符，弱";
//        }
//        if (passwordStr.matches(regexZT)){
//            return "字母字符，中";
//        }
//        if (passwordStr.matches(regexZS)){
//            return "字母数字，中";
//        }
//        if (passwordStr.matches(regexST)){
//            return "数字字符，中";
//        }
//        if (passwordStr.matches(regexZST)) {
//            return "强";
//        }
//        return "不知道是啥";
        if (passwordStr.matches(regexZ)){
            return false;
        }
        if (passwordStr.matches(regexS)){
            return false;
        }
        if (passwordStr.matches(regexT)){
            return false;
        }
        if (passwordStr.matches(regexZT)){
            return false;
        }
        if (passwordStr.matches(regexZS)){
            return false;
        }
        if (passwordStr.matches(regexST)){
            return false;
        }
        if (passwordStr.matches(regexZST)) {
            return true;
        }
        return false;
    }
    public static void main(String[] args)
    {
        System.out.println(checkPassword("qqqqqq"));
        System.out.println(checkPassword("111111"));
        System.out.println(checkPassword("......"));

        System.out.println(checkPassword("qqq..."));
        System.out.println(checkPassword("111qqq"));
        System.out.println(checkPassword("111..."));

        System.out.println("1"+checkPassword("11qq..^*.1!"));
        System.out.println("2"+checkPassword("11..qq"));
        System.out.println("3"+checkPassword("qq..11"));
        System.out.println("4"+checkPassword("qq11.@#4%"));
        System.out.println("5"+checkPassword("..11qq"));
        System.out.println("6"+checkPassword("..qq11"));

        System.out.println("6"+checkPassword("123zxc."));
    }
}
