package com.hnca.gongshangcheck.fragment;

import java.io.Serializable;

public class LoginUserInfo implements Serializable {
    private String code;
    private String message;
    private String success;
    private String userId;
    private String token;
    private String name;
    private String username;
    private String userCode;
    private String birthday;
    private String sex;
    private String mobile;
    private String avatar;
    private String userStatus;
    private String userType;
    private String loginTime;
    private String remark;

    public LoginUserInfo() {
        this.code = "";
        this.message = "";
        this.success = "";
        this.userId = "";
        this.token = "";
        this.name = "";
        this.username = "";
        this.userCode = "";
        this.birthday = "";
        this.sex = "";
        this.mobile = "";
        this.avatar = "";
        this.userStatus = "";
        this.userType = "";
        this.loginTime = "";
        this.remark = "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //geter-------------------------------
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", success='" + success + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", userCode='" + userCode + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userType='" + userType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
