package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 23..
 */
public class Fragment_UserInfoForSignUp extends Fragment{
    //TODO 이메일의 경우 네트워크 통신을 하여 서버 DB에 존재 여부 확인
    //TODO 단계별 회원정보 입력 EditText view 보여지도록 처리
    //TODO 해당 단계 회원정보 입력중 입력내용 유무에 따른 다음단계 버튼 활성/비활성 처리
    //TODO 각 입력 단계별 회원정보 유효성 체크
    EditText emailEditText;
    EditText nameEditText;
    EditText passwordEditText;
    EditText birthEditText;
    EditText genderEditText;
    Button continueButton;
    Button requestButton;
    enum SignUpStep{EMAIL, NAME, PASSWORD, BIRTH, GENDER};
    SignUpStep signUpStep;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        return super.onCreateView(inflater, container, savedInstanceState);
        signUpStep = SignUpStep.EMAIL;
        user = new User();
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailEditText = (EditText)view.findViewById(R.id.sign_up_email);
        nameEditText = (EditText)view.findViewById(R.id.sign_up_name);
        passwordEditText = (EditText)view.findViewById(R.id.sign_up_password);
        birthEditText = (EditText)view.findViewById(R.id.sign_up_birth);
        genderEditText = (EditText)view.findViewById(R.id.sign_up_gender);

        continueButton = (Button)view.findViewById(R.id.sign_up_continue_btn);
        requestButton = (Button)view.findViewById(R.id.sign_up_request_btn);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 서버 DB에 login 요청
                //TODO 1) 실패시 실패 사유 메시지 TOAST
                //TODO 2) 성공시 MatchingActivity 호출
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch(signUpStep) {

                    case EMAIL:
                        user.setEmail(emailEditText.getText().toString());
                        emailEditText.setVisibility(View.GONE);
                        nameEditText.setVisibility(View.VISIBLE);
                        signUpStep = SignUpStep.NAME;
                        break;

                    case NAME:
                        nameEditText.setVisibility(View.GONE);
                        passwordEditText.setVisibility(View.VISIBLE);
                        user.setName(nameEditText.getText().toString());
                        signUpStep = SignUpStep.PASSWORD;
                        break;

                    case PASSWORD:
                        passwordEditText.setVisibility(View.GONE);
                        birthEditText.setVisibility(View.VISIBLE);
                        user.setPassword(passwordEditText.getText().toString());
                        signUpStep = SignUpStep.BIRTH;
                        break;

                    case BIRTH:
                        birthEditText.setVisibility(View.GONE);
                        genderEditText.setVisibility(View.VISIBLE);
                        user.setBirth(birthEditText.getText().toString());
                        signUpStep = SignUpStep.GENDER;
                        break;

                    case GENDER:
                        user.setGender(genderEditText.getText().toString());
                        genderEditText.setVisibility(View.GONE);
                        enableSignUpBtn();
                        break;
                }

            }
        });

        return view;
    }

    private void enableSignUpBtn() {
        //TODO 회원정보 전부 입력시 회원가입 버튼 활성화
        continueButton.setVisibility(View.GONE);
        requestButton.setVisibility(View.VISIBLE);
    }
}
