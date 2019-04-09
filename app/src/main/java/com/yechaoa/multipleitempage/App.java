package com.yechaoa.multipleitempage;

import android.app.Application;

import com.yechaoa.yutils.ActivityUtil;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

/**
 * Created by syy on 2019/4/8.
 * Describe :
 */
public class App extends Application {
    private final static String URL="http://ip:port/app/";

    @Override
    public void onCreate() {
        super.onCreate();

        YUtils.initialize(this);
        LogUtil.setIsLog(true);
        registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());

    }
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
}
