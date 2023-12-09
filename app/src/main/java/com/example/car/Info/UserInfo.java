package com.example.car.Info;

public class UserInfo {
    public static String UserName;
    public static String UserStuNum;
    public static boolean admin;

    private UserInfo(){}

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static void setUserStuNum(String userStuNum) {
        UserStuNum = userStuNum;
    }
}
