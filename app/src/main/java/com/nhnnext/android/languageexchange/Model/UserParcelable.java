package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alpha on 2015. 8. 6..
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

    public UserParcelable() {
        this(null, null, null, null, 0, null, null, null, null, null);
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
        this.oauth = user.getoAuth();
        this.intro = user.getUserIntro();
    }

    public UserParcelable(Bitmap image, String email, String name, String password, int age, String gender, String nativeLanguage, String practicingLanguage, String oauth, String intro) {
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
                '}';
    }
}
