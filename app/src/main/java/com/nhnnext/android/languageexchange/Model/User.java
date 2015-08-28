package com.nhnnext.android.languageexchange.Model;

/**
 * Created by Alpha on 2015. 8. 3..
 * Class User : 사용자 정보에 대한 model
 * field values : 프로필 이미지, 이메일, 이름, 비밀번호, 나이, 성별, 모국어, 배우고자하는 언어, oAuth유무
 * 소개 내용, 생성 시간, 수정 시간
 */
public class User {
    private String userImage;
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
        this(null, null, null, null, 0, null, null, null, null, null, null, null);
    }

    //for login
    public User(String userEmail, String userPassword, String oAuth) {
        this(null, userEmail, null, userPassword, 0, null, null, null, oAuth, null, null, null);
    }

    public User(String userEmail, String userName, String userPassword, int userAge, String userGender) {
        this(null, userEmail, userName, userPassword, userAge, userGender, null, null, null, null, null, null);
    }

    public User(String userImage, String userEmail, String userName, String userPassword, int userAge, String userGender, String userNative, String userPracticing, String oAuth, String userIntro, String userCreateDate, String userUpdateDate) {
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

    public User(UserParcelable user) {
        this(user.getImage(), user.getEmail(), user.getName(), user.getPassword(), user.getAge(), user.getGender(), user.getNativeLanguage(), user.getPracticingLanguage(), user.getOauth(), user.getIntro(), user.getUserCreateDate(), user.getUserUpdateDate());
    }

    public String getUserImage() {
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

    public String getOAuth() {
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

    /**
     * Method getGenderForKorean()
     * user 성별 한글로 표현
     *
     * @return 성별에 대한 한글단어값(male = 남성, female = 여성)
     */
    public String getGenderForKorean() {
        if (userGender.equals("male"))
            return "남성";
        else if (userGender.equals("female"))
            return "여성";
        else
            return null;
    }

    public void setUserImage(String userImage) {
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
