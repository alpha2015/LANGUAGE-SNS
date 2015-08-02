package com.nhnnext.android.languageexchange.user;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.MainActivity;
import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class SignUpActivity extends FragmentActivity implements View.OnClickListener {
    //TODO fragment에서 입력한 회원정보 저장
    TextView backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //TODO 회원정보 입력 fragment 연결
        //TODO 로그인 Activity 돌아가기 이벤트 등록
        backLogin = (TextView)findViewById(R.id.back_login_page);
        backLogin.setOnClickListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();

        Fragment fragment1 = new Fragment_UserInfoForSignUp();

        transaction.add(R.id.fragment_container1, fragment1);

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
        //TODO 로그인 Activity로 돌아가기
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class SignUpAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO progressBar 표시
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //TODO HttpConnection 구현체 호출, 회원가입 url을 통해 요청
            //TODO 성공유무 parsing해서 받아오기
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //TODO 성공시 추후 자동로그인을 위해 DB에 회원정보 저장, 자동 로그인, match Activity 호출
            //TODO 실패시 실패 사유 표시
        }
    }

    void sendUserInfoToActivity() {
        //TODO 서버 api를 통해 회원가입 AsyncTask 실행
    }
}
