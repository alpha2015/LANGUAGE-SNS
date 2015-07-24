package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class ShowResultActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO (Fragment 구현부에서 해야하는지 Fragment 스터디 후에 결정)
        //TODO (Fragment1)매칭된유저 리스트 보여주기 - Adapter, list 연결 구현(Parcel or DB)
        //TODO (Fragment1)한 페이지보다 많은 리스트일 경우 더보기 버튼 보여주기 및 클릭 이벤트 설정
        //TODO 더 보기 버튼 클릭시 Fragment1 새로운 유저 리스트로 갱신
        //TODO (Fragment1)각각의 view list 클릭 이벤트 설정후 발생시 Fragment2로 전환
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

}
