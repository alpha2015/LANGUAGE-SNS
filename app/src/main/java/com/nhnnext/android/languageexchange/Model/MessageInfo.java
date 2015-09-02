package com.nhnnext.android.languageexchange.Model;

import android.graphics.Bitmap;

import com.android.volley.Request;

import java.util.Date;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class MessageInfo : 타 사용자로 부터 받은 push notification message model
 * field values : 프로필 이미지, 이름, 메시지, 보낸 시간
 */
public class MessageInfo {
    private int messageId;
    private String senderEmail;
    private String senderOAuth;
    private String senderName;
    private String message;
    private String sendTime;

    public MessageInfo() {
        this(0, null, null, null, null, null);
    }

    public MessageInfo(int messageId, String senderEmail, String senderOAuth, String senderName, String message, String sendTime) {
        this.messageId = messageId;
        this.senderEmail = senderEmail;
        this.senderOAuth = senderOAuth;
        this.senderName = senderName;
        this.message = message;
        this.sendTime = sendTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderOAuth() {
        return senderOAuth;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSenderOAuth(String senderOAuth) {
        this.senderOAuth = senderOAuth;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageInfo that = (MessageInfo) o;

        if (messageId != that.messageId) return false;
        if (senderEmail != null ? !senderEmail.equals(that.senderEmail) : that.senderEmail != null)
            return false;
        if (senderOAuth != null ? !senderOAuth.equals(that.senderOAuth) : that.senderOAuth != null)
            return false;
        if (senderName != null ? !senderName.equals(that.senderName) : that.senderName != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return !(sendTime != null ? !sendTime.equals(that.sendTime) : that.sendTime != null);

    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + (senderEmail != null ? senderEmail.hashCode() : 0);
        result = 31 * result + (senderOAuth != null ? senderOAuth.hashCode() : 0);
        result = 31 * result + (senderName != null ? senderName.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "messageId=" + messageId +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderOAuth='" + senderOAuth + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
