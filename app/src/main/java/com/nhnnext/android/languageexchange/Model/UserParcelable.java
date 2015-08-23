package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alpha on 2015. 8. 6..
 * Class UserParcelable : 사용자 정보에 대한 pacelable model
 * field values : 프로필 이미지, 이메일, 이름, 비밀번호, 나이, 성별, 모국어, 배우고자하는 언어, oAuth유무
 * 소개 내용, 생성 시간, 수정 시간
 */
public class UserParcelable implements Parcelable {
    private Bitmap image;
    private String email;
    private String name;
    private String password;
    private int age;
    private String gender;
    private String nativeLanguage;
    private String practicingLanguage;
    private String oauth;
    private String intro;
    private String userCreateDate;
    private String userUpdateDate;

    public UserParcelable() {
        this(null, null, null, null, 0, null, null, null, null, null, null, null);
    }

    public UserParcelable(User user) {
        this.image = user.getUserImage();
        this.email = user.getUserEmail();
        this.name = user.getUserName();
        this.password = user.getUserPassword();
        this.age = user.getUserAge();
        this.gender = user.getUserGender();
        this.nativeLanguage = user.getUserNative();
        this.practicingLanguage = user.getUserPracticing();
        this.oauth = user.getOAuth();
        this.intro = user.getUserIntro();
        this.userCreateDate = user.getUserCreateDate();
        this.userUpdateDate = user.getUserUpdateDate();
    }

    public UserParcelable(Bitmap image, String email, String name, String password, int age, String gender, String nativeLanguage, String practicingLanguage, String oauth, String intro, String userCreateDate, String userUpdateDate) {
        this.image = image;
        this.email = email;
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.nativeLanguage = nativeLanguage;
        this.practicingLanguage = practicingLanguage;
        this.oauth = oauth;
        this.intro = intro;
        this.userCreateDate = userCreateDate;
        this.userUpdateDate = userUpdateDate;

    }

    public UserParcelable(Parcel source) {
        image = (Bitmap) source.readValue(Bitmap.class.getClassLoader());
        email = source.readString();
        name = source.readString();
        password = source.readString();
        age = source.readInt();
        gender = source.readString();
        nativeLanguage = source.readString();
        practicingLanguage = source.readString();
        oauth = source.readString();
        intro = source.readString();
        userCreateDate = source.readString();
        userUpdateDate = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(nativeLanguage);
        dest.writeString(practicingLanguage);
        dest.writeString(oauth);
        dest.writeString(intro);
        dest.writeString(userCreateDate);
        dest.writeString(userUpdateDate);
    }

    public static final Parcelable.Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }

        @Override
        public UserParcelable createFromParcel(Parcel source) {
            return new UserParcelable(source);
        }
    };

    public Bitmap getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    /**
     * Method getGenderForKorean()
     * user 성별 한글로 표현
     *
     * @return 성별에 대한 한글단어값(male = 남성, female = 여성)
     */
    public String getGenderForKorean() {
        if (gender.equals("male"))
            return "남성";
        else if (gender.equals("female"))
            return "여성";
        else
            return null;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getPracticingLanguage() {
        return practicingLanguage;
    }

    public String getOauth() {
        return oauth;
    }

    public String getIntro() {
        return intro;
    }

    public String getUserCreateDate() {
        return userCreateDate;
    }

    public String getUserUpdateDate() {
        return userUpdateDate;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public void setPracticingLanguage(String practicingLanguage) {
        this.practicingLanguage = practicingLanguage;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setUserCreateDate(String userCreateDate) {
        this.userCreateDate = userCreateDate;
    }

    public void setUserUpdateDate(String userUpdateDate) {
        this.userUpdateDate = userUpdateDate;
    }

    @Override
    public String toString() {
        return "UserParcelable{" +
                "image=" + image +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", nativeLanguage='" + nativeLanguage + '\'' +
                ", practicingLanguage='" + practicingLanguage + '\'' +
                ", oauth='" + oauth + '\'' +
                ", intro='" + intro + '\'' +
                ", userCreateDate='" + userCreateDate + '\'' +
                ", userUpdateDate='" + userUpdateDate + '\'' +
                '}';
    }
}
