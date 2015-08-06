package com.nhnnext.android.languageexchange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.user.User;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupButton;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEditText = (EditText) findViewById(R.id.login_email_text);
        passwordEditText = (EditText) findViewById(R.id.login_password_text);
        loginButton = (Button) findViewById(R.id.login_btn);
        signupButton = (TextView) findViewById(R.id.signup_btn);

        //각각의 view에 대한 이벤트 등록(TextChangedListener, OnClickListener)
        emailEditText.addTextChangedListener(textBarWatcher);
        passwordEditText.addTextChangedListener(textBarWatcher);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {

        super.onStart();
        //TODO 앱내 DB에 회원정보 가져오기
        //TODO if 회원정보 존재하지 않을 경우) user = null; , 자동 로그인 수행 하지 않음
//        user = null;
        //TODO else 회원정보 존재할 경우) 자동로그인 시도시 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청 AsyncTask 실행
        user = new User("test@naver.com", "최성원", "1234", 29, 'M'); //test data
        emailEditText.setText(user.getEmail());
//        new LoginAsyncTask().execute("target url", user);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                new LoginAsyncTask().execute("target url", user);

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

    /*
        서버로 로그인 요청과 요청결과에 따른 분기 처리
     */
    private class LoginAsyncTask extends AsyncTask<Object, Void, Pair<Boolean, String>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressBar 표시
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("로그인 중");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Pair<Boolean, String> doInBackground(Object... params) {
            /*
                loadXmlFromNetwork 구현부
             */
            //TODO HttpConnection 구현체 호출, 로그인 url을 통해 요청
            //TODO 결과값(성공여부, 회원정보) parsing
            try {
                Thread.sleep(500); //로그인 가상 테스트용 sleep
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //TODO 성공시 TRUE, user정보 Pair instance return
            //TODO 실패시 FALSE, 실패 사유 Pair instance return
            return new Pair<>(true, "success");
        }

        @Override
        protected void onPostExecute(Pair<Boolean, String> booleanStringPair) {
            super.onPostExecute(booleanStringPair);
            //progressBar 숨기기
            progressDialog.dismiss();

            /*
               App DB와 서버 DB 불일치시(로그인 실패)
             */
            //TODO 실패 사유 Toast로 표시
            //TODO App DB에 기존 회원정보 삭제(이후 한번은 수동입력 로그인 하도록)

            /*
                App DB와 서버 DB 일치시(로그인 성공)
             */
            //TODO 회원정보 App DB에 UPDATE
            //match Activity 호출
            Intent intent = new Intent();
            intent.setAction("com.nhnnext.android.action.MATCH");
            startActivity(intent);
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

}
