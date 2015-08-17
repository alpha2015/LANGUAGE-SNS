package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class SignUpActivity extends FragmentActivity implements View.OnClickListener {
    //fragment에서 입력한 회원정보 저장
    private TextView backLogin;
    private Button requestButton;
    private User userForSignUp;
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
                /*
                    서버에 회원가입 요청
                 */
                new SignUpAsyncTask().execute("target url", userForSignUp);
                break;
        }
    }

    private class SignUpAsyncTask extends AsyncTask<Object, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressBar 표시
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("회원가입 중");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            /*
                loadXmlFromNetwork 구현부
                //TODO HttpConnection 구현체 호출, 회원가입 url을 통해 요청
                //TODO 결과값(성공여부) parsing
                //TODO 성공시 TRUE return
                //TODO 실패시 FALSE return
             */
            try {
                Thread.sleep(500); //progressBar 정상 동작 테스트를 위한 sleep, loadXmlFromNetwork구현시 제거
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //progressBar 숨기기
            progressDialog.dismiss();

            /*
               회원가입 실패
             */
            //TODO 실패사유 Toast로 표시

            /*
                회원가입 성공
             */
            //TODO 추후 자동로그인을 위해 App DB에 회원정보 INSERT, MatchingActivity 호출
            Intent intent = new Intent();
            intent.setAction("com.nhnnext.android.action.MATCH");
            startActivity(intent);
        }
    }

    protected void enableSignUp(User userForSignUp) {
        requestButton.setVisibility(View.VISIBLE);
        this.userForSignUp = userForSignUp;
    }
}

