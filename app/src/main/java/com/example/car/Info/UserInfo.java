package com.example.car.Info;

public class UserInfo {
    public static String UserName = "";
    public static String UserStuNum = "";
    public static boolean Admin = false;

    private UserInfo(){}

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static void setUserStuNum(String userStuNum) {
        UserStuNum = userStuNum;
    }

    public static void setAdmin(String admin) {Admin = admin.equals("1");}
}
