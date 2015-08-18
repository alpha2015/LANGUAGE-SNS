package com.nhnnext.android.languageexchange.common;

import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Alpha on 2015. 8. 18..
 */
public class NetworkUtil {
    public static String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String, String> pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            if (pair.second == null)
                result.append(URLEncoder.encode("", "UTF-8"));
            else
                result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }
        return result.toString();
    }
}
