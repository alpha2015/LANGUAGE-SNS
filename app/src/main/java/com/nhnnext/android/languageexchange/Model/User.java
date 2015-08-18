package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;

/**
 * Created by Alpha on 2015. 8. 3..
 */
public class User {
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

    public User() {
        this(null, null, null, null, 0, null, null, null, null, null);
    }

    public User(String email, String name, String password, int age, String gender) {
        this(null, email, name, password, age, gender, null, null, null, null);
    }

    public User(Bitmap image, String email, String name, String password, int age, String gender, String nativeLanguage, String practicingLanguage, String oauth, String intro) {
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

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getPracticingLanguage() {
        return practicingLanguage;
    }

    public String getOauth() { return oauth; }

    public String getIntro() {
        return intro;
    }

    public String getGenderForKorean() {
        if (gender.equals("male"))
            return "남성";
        else if (gender.equals("female"))
            return "여성";
        else
            return null;
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

    public void setOauth(String oauth) { this.oauth = oauth; }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (image != null ? !image.equals(user.image) : user.image != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (gender != null ? !gender.equals(user.gender) : user.gender != null) return false;
        if (nativeLanguage != null ? !nativeLanguage.equals(user.nativeLanguage) : user.nativeLanguage != null)
            return false;
        if (practicingLanguage != null ? !practicingLanguage.equals(user.practicingLanguage) : user.practicingLanguage != null)
            return false;
        if (oauth != null ? !oauth.equals(user.oauth) : user.oauth != null) return false;
        return !(intro != null ? !intro.equals(user.intro) : user.intro != null);

    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (nativeLanguage != null ? nativeLanguage.hashCode() : 0);
        result = 31 * result + (practicingLanguage != null ? practicingLanguage.hashCode() : 0);
        result = 31 * result + (oauth != null ? oauth.hashCode() : 0);
        result = 31 * result + (intro != null ? intro.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
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
