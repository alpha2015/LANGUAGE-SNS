package com.nhnnext.android.languageexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.common.DbUtil;
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;
import com.nhnnext.android.languageexchange.common.UrlFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 7. 21..
 * Class MainActivity : 로그인 / 회원가입 activity
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
    private OauthFragment oauthFragment;
    private ProgressDialog progressDialog;
    private RequestQueue queue;
    private GsonRequest<User> loginRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        mContext = this;
        mDbHelper = new MySqliteOpenHelper(mContext);

        loginRequest = new GsonRequest<>(UrlFactory.LOGIN, User.class, null,
                new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        progressDialog.dismiss();
                        DbUtil.deleteUserFromDb(mDbHelper);
                        DbUtil.saveUserIntoDb(user, mDbHelper);
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
                DbUtil.deleteUserFromDb(mDbHelper);
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

    /**
     * Method onResume()
     * 앱내 DB에 회원정보 가져오기
     * if 회원정보 존재하지 않을 경우) 자동 로그인 수행 하지 않음
     * else 회원정보 존재할 경우) 서버 api를 통해 자동로그인 시도 및 성공/실패 여부 반환 요청
     */
    @Override
    protected void onResume() {
        super.onResume();
        user = DbUtil.readUserFromDb(mDbHelper);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (user != null) {
            if (user.getOAuth() == null || (accessToken != null && !accessToken.isExpired())) {
                //progressBar 표시
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("자동 로그인 중");
                progressDialog.show();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userEmail", user.getUserEmail());
                if (user.getOAuth() != null) {
                    params.put("oAuth", user.getOAuth());
                } else {
                    params.put("userPassword", user.getUserPassword());
                }

                loginRequest.setParams(params);
                queue.add(loginRequest);
            }
        }
    }

    /**
     * Method onClick()
     *
     * @param v : clicked view
     *          회원가입 버튼 클릭시) 회원가입 Activity 호출
     *          로그인 버튼 클릭시) 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_btn:
                /*
                    서버 DB에 확인을 통해 login 요청
                 */
                user = new User(emailEditText.getText().toString(), passwordEditText.getText().toString(), null);

                //progressBar 표시
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("로그인 중");
                progressDialog.show();

                Map<String, String> params = new HashMap<String, String>();
                params.put("userEmail", user.getUserEmail());
                if (user.getOAuth() != null) {
                    params.put("oAuth", user.getOAuth());
                } else {
                    params.put("userPassword", user.getUserPassword());
                }

                loginRequest.setParams(params);
                queue.add(loginRequest);

                break;
            case R.id.signup_btn: {
                /*
                    회원가입 Activity 호출
                 */
                Intent intent = new Intent();
                intent.setAction("com.nhnnext.android.action.SIGNUP");
                startActivity(intent);
                break;
            }
        }

    }

    /**
     * Email/Password EditText에 대해 입력값 없을 경우 버튼 비활성화
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
}
