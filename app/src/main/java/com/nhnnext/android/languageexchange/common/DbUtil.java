package com.nhnnext.android.languageexchange.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhnnext.android.languageexchange.Model.User;

/**
 * Created by Alpha on 2015. 8. 28..
 */
public class DbUtil {
    /**
     * Method saveUserIntoDb(User user)
     * db에 user 정보 저장
     *
     * @param user
     */
    public static void saveUserIntoDb(User user, MySqliteOpenHelper mDbHelper) {
        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MySqliteOpenHelper.KEY_IMAGE, user.getUserImage());
        values.put(MySqliteOpenHelper.KEY_EMAIL, user.getUserEmail());
        values.put(MySqliteOpenHelper.KEY_NAME, user.getUserName());
        values.put(MySqliteOpenHelper.KEY_PASSWORD, user.getUserPassword());
        values.put(MySqliteOpenHelper.KEY_AGE, user.getUserAge());
        values.put(MySqliteOpenHelper.KEY_GENDER, user.getUserGender());
        values.put(MySqliteOpenHelper.KEY_OAUTH, user.getOAuth());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MySqliteOpenHelper.USER_TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Method deleteUserFromDb()
     * db에서 user 정보 삭제
     *
     * @return delete 성공 유무
     */
    public static boolean deleteUserFromDb(MySqliteOpenHelper mDbHelper) {
        boolean result = false;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (db.delete(MySqliteOpenHelper.USER_TABLE_NAME, null, null) > 0)
            result = true;
        db.close();
        return result;
    }

    public static User readUserFromDb(MySqliteOpenHelper mDbHelper) {
        // Get the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "userImage", "userEmail", "userName", "userPassword", "userAge", "userGender", "oAuth"
        };
        // Table, Column, WHERE, ARGUMENTS, GROUPING, HAVING, SORTING
        Cursor cursor = db.query(MySqliteOpenHelper.USER_TABLE_NAME, projection, null, null, null, null, null);

        User user = null;
        while (cursor.moveToNext()) {
            user = new User();

            if (cursor.getString(0) != null)
                user.setUserImage(cursor.getString(0));
            if (cursor.getString(1) != null)
                user.setUserEmail(cursor.getString(1));
            if (cursor.getString(2) != null)
                user.setUserName(cursor.getString(2));
            if (cursor.getString(3) != null)
                user.setUserPassword(cursor.getString(3));
            if (cursor.getString(4) != null)
                user.setUserAge(cursor.getInt(4));
            if (cursor.getString(5) != null)
                user.setUserGender(cursor.getString(5));
            if (cursor.getString(6) != null)
                user.setoAuth(cursor.getString(6));
        }
        cursor.close();
        db.close();
        return user;
    }
}
