package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;

/**
 * Created by Alpha on 2015. 8. 3..
 */
public class User {
    private Bitmap userImage;
    private String userEmail;
    private String userName;
    private String userPassword;
    private int userAge;
    private String userGender;
    private String userNative;
    private String userPracticing;
    private String oAuth;
    private String userIntro;
    private String userCreateDate;
    private String userUpdateDate;


    public User() {
        this(null, null, null, null, 0, null, null, null, null, null,null,null);
    }

    //for login
    public User(String userEmail, String userPassword, String oAuth) {
        this(null, userEmail, null, userPassword, 0, null, null, null, oAuth, null,null,null);
    }

    public User(String userEmail, String userName, String userPassword, int userAge, String userGender) {
        this(null, userEmail, userName, userPassword, userAge, userGender, null, null, null, null,null,null);
    }

    public User(Bitmap userImage, String userEmail, String userName, String userPassword, int userAge, String userGender, String userNative, String userPracticing, String oAuth, String userIntro, String userCreateDate, String userUpdateDate) {
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userNative = userNative;
        this.userPracticing = userPracticing;
        this.oAuth = oAuth;
        this.userIntro = userIntro;
        this.userCreateDate = userCreateDate;
        this.userUpdateDate = userUpdateDate;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserNative() {
        return userNative;
    }

    public String getUserPracticing() {
        return userPracticing;
    }

    public String getoAuth() {
        return oAuth;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public String getUserCreateDate() {
        return userCreateDate;
    }

    public String getUserUpdateDate() {
        return userUpdateDate;
    }

    public String getGenderForKorean() {
        if (userGender.equals("male"))
            return "남성";
        else if (userGender.equals("female"))
            return "여성";
        else
            return null;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserNative(String userNative) {
        this.userNative = userNative;
    }

    public void setUserPracticing(String userPracticing) {
        this.userPracticing = userPracticing;
    }

    public void setoAuth(String oAuth) {
        this.oAuth = oAuth;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public void setUserCreateDate(String userCreateDate) {
        this.userCreateDate = userCreateDate;
    }

    public void setUserUpdateDate(String usesrUpdateDate) {
        this.userUpdateDate = usesrUpdateDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userImage=" + userImage +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAge=" + userAge +
                ", userGender='" + userGender + '\'' +
                ", userNative='" + userNative + '\'' +
                ", userPracticing='" + userPracticing + '\'' +
                ", oAuth='" + oAuth + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", userCreateDate='" + userCreateDate + '\'' +
                ", userUpdateDate='" + userUpdateDate + '\'' +
                '}';
    }
}
