package com.nhnnext.android.languageexchange;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class MainActivity extends Activity implements View.OnClickListener {

    //TODO Email/Password EditText에 대해 입력값 없을 경우 버튼 비활성화 구현(TextWatcher)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO view에 대해 이벤트 등록(TextChangedListener, OnClickListener)
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO 앱내 DB에 회원정보있는지 판별하여 자동로그인 시도
        //TODO 자동로그인 시도시 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청 AsyncTask 실행
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
        //TODO 회원가입, 로그인 버튼 클릭에 대한 처리 구현
        //TODO 회원가입 버튼 클릭시 회원가입 Activity 호출
        //TODO 로그인 버튼 클릭시 서버 api를 통해 로그인 시도 및 성공/실패 여부 반환 요청 AsyncTask 실행

    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Pair<Boolean, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO progressBar 표시
        }

        @Override
        protected Pair<Boolean, String> doInBackground(String... params) {
            //TODO HttpConnection 구현체 호출, 로그인 url을 통해 요청
            //TODO 결과값(성공여부, 회원정보) parsing
            return null;
        }

        @Override
        protected void onPostExecute(Pair<Boolean, String> booleanStringPair) {
            super.onPostExecute(booleanStringPair);
            //TODO progressBar 숨기기
            //TODO 실패시) 실패 사유 표시

            //TODO 성공시) 회원정보 App DB에 저장, match Activity 호출
        }
   }
}
