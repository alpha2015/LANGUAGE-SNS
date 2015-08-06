package com.nhnnext.android.languageexchange.user;

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
    private char gender;

    public UserParcelable() {
        this(null, null, null, null, 0, 'N');
    }

    public UserParcelable(User user) {
        this.image = user.getImage();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.gender = user.getGender();
    }

    public UserParcelable(Bitmap image, String email, String name, String password, int age, char gender) {
        this.image = image;
        this.email = email;
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public UserParcelable(Parcel source) {
        image = (Bitmap) source.readValue(Bitmap.class.getClassLoader());
        email = source.readString();
        name = source.readString();
        password = source.readString();
        age = source.readInt();
        gender = (char) source.readInt();
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
        dest.writeInt(gender);
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

    public char getGender() {
        return gender;
    }

    public String getGenderForKorean() {
        if (gender == 'M')
            return "남성";
        else if (gender == 'F')
            return "여성";
        else
            return null;
    }

    @Override
    public String toString() {
        return "UserParcelable{" +
                "image=" + image +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
