package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 23..
 */
public class Fragment_UserInfoForSignUp extends Fragment {
    //TODO 이메일의 경우 네트워크 통신을 하여 서버 DB에 존재 여부 확인
    //TODO 단계별 회원정보 입력 EditText view 보여지도록 처리
    //TODO 해당 단계 회원정보 입력중 입력내용 유무에 따른 다음단계 버튼 활성/비활성 처리
    //TODO 각 입력 단계별 회원정보 유효성 체크
    private enum SignUpStep {
        EMAIL, NAME, PASSWORD, AGE, GENDER
    }

    private LinearLayout emailLayout;
    private LinearLayout nameLayout;
    private LinearLayout passwordLayout;
    private LinearLayout ageLayout;
    private RelativeLayout completeLayout;

    private LinearLayout genderLayout;
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private NumberPicker agePicker;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private Button continueButton;

    private SignUpStep signUpStep;
    private User userForSignUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        signUpStep = SignUpStep.EMAIL;
        userForSignUp = new User();
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailLayout = (LinearLayout) view.findViewById(R.id.sign_up_email_layout);
        nameLayout = (LinearLayout) view.findViewById(R.id.sign_up_name_layout);
        passwordLayout = (LinearLayout) view.findViewById(R.id.sign_up_password_layout);
        ageLayout = (LinearLayout) view.findViewById(R.id.sign_up_age_layout);
        genderLayout = (LinearLayout) view.findViewById(R.id.sign_up_gender_layout);
        completeLayout = (RelativeLayout) view.findViewById(R.id.sign_up_complete_layout);

        emailEditText = (EditText) view.findViewById(R.id.sign_up_email);
        nameEditText = (EditText) view.findViewById(R.id.sign_up_name);
        passwordEditText = (EditText) view.findViewById(R.id.sign_up_password);
        agePicker = (NumberPicker) view.findViewById(R.id.sign_up_age);
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.sign_up_gender);
        maleRadioButton = (RadioButton) view.findViewById(R.id.gender_male);
        femaleRadioButton = (RadioButton) view.findViewById(R.id.gender_female);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                continueButton.setVisibility(View.VISIBLE);
            }
        });

        emailEditText.addTextChangedListener(textBarWatcher);
        nameEditText.addTextChangedListener(textBarWatcher);
        passwordEditText.addTextChangedListener(textBarWatcher);

        continueButton = (Button) view.findViewById(R.id.sign_up_continue_btn);
        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (signUpStep) {

                    case EMAIL:
                        userForSignUp.setEmail(emailEditText.getText().toString());
                        emailLayout.setVisibility(View.GONE);
                        nameLayout.setVisibility(View.VISIBLE);
                        signUpStep = SignUpStep.NAME;
                        continueButton.setVisibility(View.GONE);
                        break;

                    case NAME:
                        nameLayout.setVisibility(View.GONE);
                        passwordLayout.setVisibility(View.VISIBLE);
                        userForSignUp.setName(nameEditText.getText().toString());
                        signUpStep = SignUpStep.PASSWORD;
                        continueButton.setVisibility(View.GONE);
                        break;

                    case PASSWORD:
                        passwordLayout.setVisibility(View.GONE);
                        ageLayout.setVisibility(View.VISIBLE);
                        agePicker.setWrapSelectorWheel(false);
                        agePicker.setMinValue(0);
                        agePicker.setMaxValue(99);
                        agePicker.setValue(20);
                        userForSignUp.setPassword(passwordEditText.getText().toString());
                        signUpStep = SignUpStep.AGE;
                        break;

                    case AGE:
                        ageLayout.setVisibility(View.GONE);
                        genderLayout.setVisibility(View.VISIBLE);
                        userForSignUp.setAge(agePicker.getValue());
                        continueButton.setVisibility(View.GONE);
                        signUpStep = SignUpStep.GENDER;
                        break;

                    case GENDER:
                        if (maleRadioButton.isChecked())
                            userForSignUp.setGender('M');
                        else    //femaleRadioButton is checked
                            userForSignUp.setGender('F');
                        genderLayout.setVisibility(View.GONE);
                        continueButton.setVisibility(View.GONE);
                        requestEnableSignUpBtn();
                        completeLayout.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        return view;
    }

    private void requestEnableSignUpBtn() { //TODO 메소드명 변경
        //TODO 회원정보 전부 입력시 회원가입요청 버튼 활성화 요청
        Log.d("storedUserInfo", String.valueOf(userForSignUp));
        SignUpActivity signUpActivity = (SignUpActivity) getActivity();
        signUpActivity.enableSignUp(userForSignUp);
    }

    /*
        Email/Name/Password EditText에 대해 입력값 없을 경우 '계속' 버튼 Visibility = GONE 처리
     */
    private TextWatcher textBarWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (signUpStep) {

                case EMAIL:
                    if (emailEditText.length() > 0)
                        continueButton.setVisibility(View.VISIBLE);
                    else
                        continueButton.setVisibility(View.GONE);
                    break;
                case NAME:
                    if (nameEditText.length() > 0)
                        continueButton.setVisibility(View.VISIBLE);
                    else
                        continueButton.setVisibility(View.GONE);
                    break;
                case PASSWORD:
                    if (passwordEditText.length() > 0)
                        continueButton.setVisibility(View.VISIBLE);
                    else
                        continueButton.setVisibility(View.GONE);
                    break;
            }

        }
    };
}
