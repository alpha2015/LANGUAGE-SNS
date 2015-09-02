package com.nhnnext.android.languageexchange.common;

import com.nhnnext.android.languageexchange.Model.MessageInfo;
import com.nhnnext.android.languageexchange.Model.User;

/**
 * Created by Alpha on 2015. 8. 26..
 */
public class UrlFactory {
    public static final String DEFAULT_URL = "http://125.209.198.129/";
    public static final String LOGIN = "http://125.209.198.129/user/login";
    public static final String SIGN_UP = "http://125.209.198.129/user";
    public static final String UPDATE_USER = "http://125.209.198.129/user/update";
    public static final String SEARCH_USERS = "http://125.209.198.129/user/search";
    public static final String TIMELINE = "http://125.209.198.129/user/timeline";
    public static final String MATCH = "http://125.209.198.129/user/match";
    public static final String USER_CHECK = "http://125.209.198.129/user/check";
    public static final String SEND_MESSAGE = "http://125.209.198.129/message";
    public static final String RECEIVE_MESSAGE = "http://125.209.198.129/message/list";


//    public static final String DEFAULT_URL = "http://10.0.3.2:8080/";
//    public static final String LOGIN = "http://10.0.3.2:8080/user/login";
//    public static final String SIGN_UP = "http://10.0.3.2:8080/user";
//    public static final String UPDATE_USER = "http://10.0.3.2:8080/user/update";
//    public static final String SEARCH_USERS = "http://10.0.3.2:8080/user/search";
//    public static final String TIMELINE = "http://10.0.3.2:8080/user/timeline";
//    public static final String MATCH = "http://10.0.3.2:8080/user/match";
//    public static final String USER_CHECK = "http://10.0.3.2:8080/user/check";
//    public static final String SEND_MESSAGE = "http://10.0.3.2:8080/message";
//    public static final String RECEIVE_MESSAGE = "http://10.0.3.2:8080/message/list";

    public static String getProfileImagePath(MessageInfo messageInfo) {
        String path = null;
        if (messageInfo.getSenderOAuth() == null) {
            path = DEFAULT_URL + "img/profile/" + messageInfo.getSenderEmail() + ".png";
        } else if (messageInfo.getSenderOAuth() != null && messageInfo.getSenderOAuth().equals("facebook")) {
            path = DEFAULT_URL + "img/profile_facebook/" + messageInfo.getSenderEmail() + ".png";
        } else {
            path = DEFAULT_URL + "img/profile/" + "default_profile_image.png";
        }
        return path;
    }
}
