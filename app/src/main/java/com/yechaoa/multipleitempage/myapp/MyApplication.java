package com.yechaoa.multipleitempage.myapp;

import android.app.Application;

public class MyApplication extends Application {
    private final static String URL="http://ip:port/app/";

    //登录URL
    public String getURLForLogin(){
        return URL + "user/login";
    }

    //提交工作表单URL
    public String getURLForWeekSheet(){
        return URL + "weekSheet/save";
    }

    // 获取历史记录URL
    public String getURLForHistory(){
        return URL + "weekSheet/list";
    }

    //修改密码
    public String getURLForUpdatePW(){
        return URL + "user/updatepwd";
    }

    //退出登录
    public String getURLForLogout(){
        return URL + "user/logout";
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
