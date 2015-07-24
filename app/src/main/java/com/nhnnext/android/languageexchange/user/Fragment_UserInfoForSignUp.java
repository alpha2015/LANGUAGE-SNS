package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.view.View;

/**
 * Created by Alpha on 2015. 7. 23..
 */
public class Fragment_UserInfoForSignUp extends Fragment implements View.OnClickListener {
    //TODO 이메일의 경우 네트워크 통신을 하여 서버 DB에 존재 여부 확인
    //TODO 단계별 회원정보 입력 EditText view 보여지도록 처리
    //TODO 해당 단계 회원정보 입력중 입력내용 유무에 따른 다음단계 버튼 활성/비활성 처리
    //TODO 각 입력 단계별 회원정보 유효성 체크

    private void enableSignUpBtn() {
        //TODO 회원정보 전부 입력시 회원가입 버튼 활성화

    }

    @Override
    public void onClick(View v) {
        //TODO 마지막 회원정보 입력후 활성화된 회원가입버튼 클릭시 입력한 회원정보 모아서 Activity에 전달
        //TODO sendUserInfoToActivity(User user);
    }
}
