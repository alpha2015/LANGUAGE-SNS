package com.nhnnext.android.languageexchange.match;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class Fragment_UpdateUserInfo extends Fragment {
    //TODO DB에서 가져온 회원정보 보여주기
    //TODO 각 회원정보에 대한 클릭, 저장 이벤트 등록
    //TODO 사진찍기 Activity 호출, 프로필 사진으로 저장 구현(서버DB 저장)
    //TODO 갤러리 접근, 프로필 사진으로 저장 구현(서버DB 저장)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template3, container, false);
    }
}
