package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class SignUpActivity extends FragmentActivity implements View.OnClickListener {
    //fragment에서 입력한 회원정보 저장
    private TextView backLogin;
    private Button requestButton;
    private User userForSignUp;
    private ProgressDialog progressDialog;

    //TODO 가입완료후 자동로그인시 BACK BUTTON 눌러도 회원가입으로 돌아오는 문제 해결(taskAffinity)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //회원정보 입력 fragment 연결
        //로그인 Activity 돌아가기 이벤트 등록
        backLogin = (TextView) findViewById(R.id.back_login_page);
        requestButton = (Button) findViewById(R.id.sign_up_request_btn);
        backLogin.setOnClickListener(this);
        requestButton.setOnClickListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = new Fragment_UserInfoForSignUp();
        transaction.add(R.id.fragment_container_signup, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back_login_page:
                //TODO 로그인 Activity로 돌아가기
                onBackPressed();
                break;
            case R.id.sign_up_request_btn:

                //progressBar 표시
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("회원가입 중");
                progressDialog.show();

                /*
                    서버에 회원가입 요청
                 */
                new Thread(new Runnable() {
                    public void run() {
                        URL url = null;       // URL 설정
                        String result = null;
                        try {
                            url = new URL("http://10.0.3.2:8080/user");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();
                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);  // 읽기 모드 지정
                            http.setDoOutput(true); // 쓰기 모드 지정
                            http.setRequestMethod("POST");  // method POST
                            //Form tag 방식으로 처리
                            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                            List<Pair<String, String>> params = new ArrayList<>();
                            params.add(new Pair<>("userEmail", userForSignUp.getUserEmail()));
                            params.add(new Pair<>("userName", userForSignUp.getUserName()));
                            params.add(new Pair<>("userPassword", userForSignUp.getUserPassword()));
                            params.add(new Pair<>("userGender", userForSignUp.getUserGender()));
                            params.add(new Pair<>("userAge", "" + userForSignUp.getUserAge()));
                            params.add(new Pair<>("oAuth", userForSignUp.getoAuth()));

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(NetworkUtil.getQuery(params));
                            writer.flush();
                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                            }
                            result = builder.toString();

                            /*
                               회원가입 실패
                             */
                            //TODO 실패사유 Toast로 표시

                            /*
                                회원가입 성공
                             */
                            //TODO 추후 자동로그인을 위해 App DB에 회원정보 INSERT, MatchingActivity 호출
                            if (result.equals("success\n")) {
                                progressDialog.dismiss();   //progressDialog dismiss
                                Intent intent = new Intent();
                                intent.setAction("com.nhnnext.android.action.MATCH");
                                UserParcelable parcelUser = new UserParcelable(userForSignUp);
                                intent.putExtra("user", parcelUser);
                                startActivity(intent);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;
        }
    }

    protected void enableSignUp(User userForSignUp) {
        requestButton.setVisibility(View.VISIBLE);
        this.userForSignUp = userForSignUp;
    }
}

