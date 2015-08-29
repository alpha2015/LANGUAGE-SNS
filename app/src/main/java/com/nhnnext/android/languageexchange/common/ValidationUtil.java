package com.nhnnext.android.languageexchange.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alpha on 2015. 8. 29..
 */
public class ValidationUtil {
    public static boolean checkEmail(String email) {
        String emailRegex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPassword(String password) {
        String passwordRegex = "^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$";
        Pattern p = Pattern.compile(passwordRegex);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
