package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

/**
 * Created by Alpha on 15. 8. 22..
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String USER_TABLE_NAME = "user";
    public static final String KEY_IMAGE = "userImage";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_NAME = "userName";
    public static final String KEY_PASSWORD = "userPassword";
    public static final String KEY_AGE = "userAge";
    public static final String KEY_GENDER = "userGender";
    public static final String KEY_NATIVIE = "userNative";
    public static final String KEY_PRACTICING = "userPracticing";
    public static final String KEY_OAUTH = "oAuth";
    public static final String KEY_INTRO = "userIntro";

    private final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NAME + " (" +
                    KEY_IMAGE + " BLOB, " +
                    KEY_EMAIL + " TEXT, " +
                    KEY_NAME + " TEXT, " +
                    KEY_PASSWORD + " TEXT, " +
                    KEY_AGE + " INTEGER, " +
                    KEY_GENDER + " TEXT, " +
                    KEY_NATIVIE + " TEXT, " +
                    KEY_PRACTICING + " TEXT, " +
                    KEY_OAUTH + " TEXT, " +
                    KEY_INTRO + " TEXT);";

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySqliteOpenHelper(Context context) {
        super(context, USER_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(SQL_DELETE_TABLE);
        // onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade(db, oldVersion, newVersion);
    }
}
