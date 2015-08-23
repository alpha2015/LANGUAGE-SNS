package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class MessageInfo : 타 사용자로 부터 받은 push notification message model
 * field values : 프로필 이미지, 이름, 메시지, 보낸 시간
 */
public class MessageInfo {
    private Bitmap userImage;
    private String userName;
    private String message;
    private Date sendTime;

    public MessageInfo(Bitmap userImage, String userName, String message, Date sendTime) {
        this.userImage = userImage;
        this.userName = userName;
        this.message = message;
        this.sendTime = sendTime;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageInfo that = (MessageInfo) o;

        if (userImage != null ? !userImage.equals(that.userImage) : that.userImage != null)
            return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return !(sendTime != null ? !sendTime.equals(that.sendTime) : that.sendTime != null);

    }

    @Override
    public int hashCode() {
        int result = userImage != null ? userImage.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "userImage=" + userImage +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
