package com.nhnnext.android.languageexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.common.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupButton;
    private User user;

    private TextView loginResultText;
    private OauthFragment oauthFragment;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//                new LoginAsyncTask().execute("target url", user);
                user = new User(emailEditText.getText().toString(), passwordEditText.getText().toString(), null);

                //progressBar 표시
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("로그인 중");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User loadedUser = loginFromNetwork(user);
                        progressDialog.dismiss();
                        if (loadedUser != null) {
                            Intent intent = new Intent();
                            intent.setAction("com.nhnnext.android.action.MATCH");
                            UserParcelable parcelUser = new UserParcelable(loadedUser);
                            intent.putExtra("user", parcelUser);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

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

    private User loginFromNetwork(User user) {
        URL url = null;       // URL 설정
        String result = null;
        try {
            url = new URL("http://10.0.3.2:8080/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);  // 읽기 모드 지정
            http.setDoOutput(true); // 쓰기 모드 지정
            http.setRequestMethod("POST");  // method POST
            //Form tag 방식으로 처리
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("userEmail", user.getUserEmail()));
            params.add(new Pair<>("userPassword", user.getUserPassword()));
            params.add(new Pair<>("oAuth", user.getoAuth()));

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(NetworkUtil.getQuery(params));
            writer.flush();


            int responseCode = http.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("error code = " + responseCode);
            }

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str);
            }
            result = builder.toString();

            Log.d("loadedJson", result);

            User loadedUser = new Gson().fromJson(result, new TypeToken<User>() {
            }.getType());

            return loadedUser;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
                //TODO HttpConnection 구현체 호출, 로그인 url을 통해 요청
                //TODO 결과값(성공여부, 회원정보) parsing
                //TODO 성공시 TRUE, user정보 Pair instance return
                //TODO 실패시 FALSE, 실패 사유 Pair instance return
             */
            try {
                Thread.sleep(500); //progressBar 정상 동작 테스트를 위한 sleep, loadXmlFromNetwork구현시 제거
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            UserParcelable parcelUser = new UserParcelable(new User("test1@naver.com", "최성원", "1234", 29, "male"));
            intent.putExtra("user", parcelUser);
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
