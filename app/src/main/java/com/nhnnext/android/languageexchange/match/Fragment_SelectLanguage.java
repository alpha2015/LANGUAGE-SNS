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
public class Fragment_SelectLanguage extends Fragment {
    //TODO 서버 DB를 통해 default native, practicing 언어 가져오기
    //TODO native, practicing 언어 선택, 서버 DB에 저장
    //TODO StartMatch Fragment로 전환 기능 구현

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template, container, false);
    }
}
