package com.nhnnext.android.languageexchange.match;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class MatchingActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Fragment 전환을 위한 버튼(언어선택, 매칭시작, 회원정보 변경) 이벤트 등록
        //TODO 언어 설정 Fragment 연결
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO 회원정보 DB에서 읽어오기( onCreate / onStart 위치 확인후 추후 다시 고려)
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
        //TODO 변경된 회원정보 DB 저장
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        //TODO 버튼 클릭에 따른 Fragment 동적 전환
    }
}
