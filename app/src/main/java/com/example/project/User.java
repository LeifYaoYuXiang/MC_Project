package com.example.project;

import cn.bmob.v3.BmobObject;

/**
 * @author Leif(Yuxiang Yao)
 * @Reference http://doc.bmob.cn/data/android/develop_doc/
 */
public class User extends BmobObject {

    private String userName;
    private String userPassword;
    private String userGender;
    private String userEmail;
    private String userDescription;

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
