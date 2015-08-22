package com.nhnnext.android.languageexchange;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private MySqliteOpenHelper mDbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupButton;
    private User user;

    private TextView loginResultText;
    private OauthFragment oauthFragment;
    private ProgressDialog progressDialog;
    private RequestQueue queue;
    private String loginUrl = "http://10.0.3.2:8080/user/login";
    private GsonRequest<User> loginRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        mContext = this;
        mDbHelper = new MySqliteOpenHelper(mContext);

        loginRequest = new GsonRequest<>(loginUrl, User.class, null,
                new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        progressDialog.dismiss();
                        saveUserIntoDb(user);
                        Intent intent = new Intent();
                        intent.setAction("com.nhnnext.android.action.MATCH");
                        UserParcelable parcelUser = new UserParcelable(user);
                        intent.putExtra("user", parcelUser);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
            }
        });

        //parse db 연동 테스트
//        ParseObject testObject = new ParseObject("User");
//        testObject.put("name", "최성원");
//        testObject.put("password", "1234");
//        testObject.put("email", "test.naver.com");
//        testObject.saveInBackground();

        //레이아웃 view
        emailEditText = (EditText) findViewById(R.id.login_email_text);
        passwordEditText = (EditText) findViewById(R.id.login_password_text);
        loginButton = (Button) findViewById(R.id.login_btn);
        signupButton = (TextView) findViewById(R.id.signup_btn);

        //각각의 view에 대한 이벤트 등록(TextChangedListener, OnClickListener)
        emailEditText.addTextChangedListener(textBarWatcher);
        passwordEditText.addTextChangedListener(textBarWatcher);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        oauthFragment = new OauthFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container_oauth, oauthFragment);
        ft.commit();

    }

    @Override
    protected void onStart() {

        super.onStart();
        //TODO 앱내 DB에 회원정보 가져오기
        //TODO if 회원정보 존재하지 않을 경우) 자동 로그인 수행 하지 않음

        //TODO else 회원정보 존재할 경우) 자동로그인 시도시 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청 AsyncTask 실행
        user = new User("test@naver.com", "최성원", "1234", 29, "male"); //test dummy data
        emailEditText.setText(user.getUserEmail());
//        new LoginAsyncTask().execute("target url", user);
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        deleteUserFromDb();
        User user = readUserFromDb();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (user != null) {
            if (user.getOAuth() == null || (accessToken != null && !accessToken.isExpired())) {
                //progressBar 표시
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("자동 로그인 중");
                progressDialog.show();

                Map<String, String> params = new HashMap<String, String>();
                params.put("userEmail", user.getUserEmail());
                params.put("userPassword", user.getUserPassword());
                params.put("oAuth", user.getOAuth());

                loginRequest.setParams(params);
                queue.add(loginRequest);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
        회원가입, 로그인 버튼 클릭에 대한 처리 구현
        1) 회원가입 버튼 클릭시 회원가입 Activity 호출
        2) 로그인 버튼 클릭시 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청 AsyncTask 실행
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.login_btn:
                /*
                    서버 DB에 확인을 통해 login 요청
                 */
                //TODO target url 변경
                user = new User(emailEditText.getText().toString(), passwordEditText.getText().toString(), null);

                //progressBar 표시
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("로그인 중");
                progressDialog.show();

                Map<String, String> params = new HashMap<String, String>();
                params.put("userEmail", user.getUserEmail());
                params.put("userPassword", user.getUserPassword());
                params.put("oAuth", user.getOAuth());

                loginRequest.setParams(params);
                queue.add(loginRequest);

                break;
            case R.id.signup_btn: {
                /*
                    회원가입 Activity 호출
                 */
                Intent intent = new Intent();
                intent.setAction("com.nhnnext.android.action.SIGNUP");
                UserParcelable parcelUser = new UserParcelable(user);
                intent.putExtra("user", parcelUser);
                startActivity(intent);
                break;
            }
        }

    }

    /*
        Email/Password EditText에 대해 입력값 없을 경우 버튼 비활성화
     */
    private TextWatcher textBarWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (emailEditText.length() > 0 && passwordEditText.length() > 0) {
                loginButton.setEnabled(true);
            } else {
                loginButton.setEnabled(false);
            }
        }
    };

    private User readUserFromDb() {
        // Get the data repository in read mode
        db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "userEmail", "userName", "userPassword", "userAge", "userGender", "oAuth"
        };

        // Table, Column, WHERE, ARGUMENTS, GROUPING, HAVING, SORTING
        Cursor cursor = db.query(MySqliteOpenHelper.USER_TABLE_NAME, projection, null, null, null, null, null);

        // AddView into the TableLayout using return value

        User user = null;
        while (cursor.moveToNext()) {
            user = new User();
            user.setUserEmail(cursor.getString(0));
            user.setUserName(cursor.getString(1));
            user.setUserPassword(cursor.getString(2));
            user.setUserAge(cursor.getInt(3));
            user.setUserGender(cursor.getString(4));
            user.setoAuth(cursor.getString(5));
        }
        cursor.close();

        db.close();
        return user;
    }

    private boolean deleteUserFromDb() {
        boolean result = false;
        db = mDbHelper.getWritableDatabase();
        if (db.delete(MySqliteOpenHelper.USER_TABLE_NAME, null, null) > 0)
            result = true;
        db.close();
        return result;
    }

    private void saveUserIntoDb(User user) {
        // Get the data repository in write mode
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MySqliteOpenHelper.KEY_EMAIL, user.getUserEmail());
        values.put(MySqliteOpenHelper.KEY_NAME, user.getUserName());
        values.put(MySqliteOpenHelper.KEY_PASSWORD, user.getUserPassword());
        values.put(MySqliteOpenHelper.KEY_AGE, user.getUserAge());
        values.put(MySqliteOpenHelper.KEY_GENDER, user.getUserGender());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MySqliteOpenHelper.USER_TABLE_NAME, null, values);
        db.close();
    }
}
