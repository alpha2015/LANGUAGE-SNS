package com.nhnnext.android.languageexchange.match;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class MatchingActivity extends FragmentActivity implements View.OnClickListener {
    private FragmentManager fm;

    private Button selectLanButton;
    private Button startMatchButton;
    private Button updateInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        //TODO Fragment 전환을 위한 버튼(언어선택, 매칭시작, 회원정보 변경) 이벤트 등록
        //TODO 언어 설정 Fragment 연결
        selectLanButton = (Button) findViewById(R.id.select_language);
        startMatchButton = (Button) findViewById(R.id.start_match);
        updateInfoButton = (Button) findViewById(R.id.update_info);

        selectLanButton.setOnClickListener(this);
        startMatchButton.setOnClickListener(this);
        updateInfoButton.setOnClickListener(this);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment frag = new Fragment_SelectLanguage();

        ft.add(R.id.fragment_container_match, frag);

        ft.commit();

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
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag;
        int id = v.getId();

        switch(id){
            case R.id.select_language :
                frag = new Fragment_SelectLanguage();
                ft.replace(R.id.fragment_container_match, frag);
                break;
            case R.id.start_match :
                frag = new Fragment_StartMatch();
                ft.replace(R.id.fragment_container_match, frag);
                break;
            case R.id.update_info :
                frag = new Fragment_UpdateUserInfo();
                ft.replace(R.id.fragment_container_match, frag);
                break;
        }
        ft.commit();
    }

}
