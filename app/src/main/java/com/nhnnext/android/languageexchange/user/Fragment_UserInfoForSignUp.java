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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.UrlFactory;
import com.nhnnext.android.languageexchange.common.ValidationUtil;

/**
 * Created by Alpha on 2015. 7. 23..
 * Class Fragment_UserInfoForSignUp : 회원가입을 위한 회원정보 입력 fragment
 */
public class Fragment_UserInfoForSignUp extends Fragment {
    private enum SignUpStep {
        EMAIL, NAME, PASSWORD, AGE, GENDER, CONFIRM
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
    private TextView writtenEmailEditText;
    private TextView writtenNameEditText;
    private TextView writtenAgeEditText;
    private TextView writtenGenderEditText;
    private Button continueButton;
    private ImageButton preStateButton;
    private SignUpStep signUpStep;
    private User userForSignUp;
    private String userEmail;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        //회원정보 입력단계 초기화
        signUpStep = SignUpStep.EMAIL;
        //입력한 회원정보를 저장할 User instance
        userForSignUp = new User();
        userForSignUp.setUserImage(UrlFactory.DEFAULT_URL + "img/profile/default_profile_image.png");
        //레이아웃 view
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
        continueButton = (Button) view.findViewById(R.id.sign_up_continue_btn);
        preStateButton = (ImageButton) view.findViewById(R.id.sign_up_pre_state_btn);

        writtenEmailEditText = (TextView) view.findViewById(R.id.sign_up_email_written);
        writtenNameEditText = (TextView) view.findViewById(R.id.sign_up_name_written);
        writtenAgeEditText = (TextView) view.findViewById(R.id.sign_up_age_written);
        writtenGenderEditText = (TextView) view.findViewById(R.id.sign_up_gender_written);

        //성별 설정 radio group 이벤트 등록
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //check시 다음단계를 위한 계속 버튼 VISIBLE
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                continueButton.setVisibility(View.VISIBLE);
            }
        });

        //Email,Name,Password EditText 이벤트 등록
        emailEditText.addTextChangedListener(textBarWatcher);
        nameEditText.addTextChangedListener(textBarWatcher);
        passwordEditText.addTextChangedListener(textBarWatcher);
        preStateButton.setVisibility(View.INVISIBLE);

        //다음단계를 위한 계속 버튼 이벤트 등록 - 각각의 회원정보 입력시 추가 회원정보 입력을 위한 VIEW VISIBLE
        //Step : 이메일 -> 이름 -> 비밀번호 -> 나이 -> 성별

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //단계별 회원정보 입력 EditText view visible 설정
                //각 입력 단계별 회원정보 유효성 체크
                switch (signUpStep) {

                    case EMAIL:
                        //이메일의 경우 네트워크 통신을 하여 서버 DB에 존재 여부 확인
                        //if 서버 DB에 해당 email 존재할 경우) toast 알림
                        //else 서버 DB에 해당 Email 존재 하지 않을 경우) 이름 정보 입력을 위한 view 활성화
                        userEmail = emailEditText.getText().toString();
                        if (ValidationUtil.checkEmail(userEmail)) {
                            StringRequest userCheckReq = new StringRequest(Request.Method.GET, UrlFactory.USER_CHECK + "?userEmail=" + userEmail,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            if (response.equals("success")) {
                                                userForSignUp.setUserEmail(userEmail);
                                                emailLayout.setVisibility(View.GONE);
                                                nameLayout.setVisibility(View.VISIBLE);
                                                signUpStep = SignUpStep.NAME;
                                                continueButton.setVisibility(View.GONE);
                                            }
                                            if (response.equals("fail"))
                                                Toast.makeText(getActivity().getApplicationContext(), "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity().getApplicationContext(), "네트워크 설정을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            queue.add(userCheckReq);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case NAME:
                        nameLayout.setVisibility(View.GONE);
                        passwordLayout.setVisibility(View.VISIBLE);
                        userForSignUp.setUserName(nameEditText.getText().toString());
                        signUpStep = SignUpStep.PASSWORD;
                        continueButton.setVisibility(View.GONE);
                        break;

                    case PASSWORD:
                        String userPassword = passwordEditText.getText().toString();
                        if (ValidationUtil.checkPassword(userPassword)) {
                            userForSignUp.setUserPassword(userPassword);
                            passwordLayout.setVisibility(View.GONE);
                            ageLayout.setVisibility(View.VISIBLE);
                            agePicker.setWrapSelectorWheel(false);
                            agePicker.setMinValue(0);
                            agePicker.setMaxValue(99);
                            agePicker.setValue(20);
                            signUpStep = SignUpStep.AGE;
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "비밀번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case AGE:
                        ageLayout.setVisibility(View.GONE);
                        genderLayout.setVisibility(View.VISIBLE);
                        userForSignUp.setUserAge(agePicker.getValue());
                        continueButton.setVisibility(View.GONE);
                        signUpStep = SignUpStep.GENDER;
                        break;

                    case GENDER:
                        if (maleRadioButton.isChecked())
                            userForSignUp.setUserGender("male");
                        else    //femaleRadioButton is checked
                            userForSignUp.setUserGender("female");
                        genderLayout.setVisibility(View.GONE);
                        continueButton.setVisibility(View.GONE);
                        requestEnableSignUpBtn();
                        writtenEmailEditText.setText(userForSignUp.getUserEmail());
                        writtenNameEditText.setText(userForSignUp.getUserName());
                        writtenAgeEditText.setText(Integer.toString(userForSignUp.getUserAge()));
                        writtenGenderEditText.setText(userForSignUp.getGenderForKorean());
                        completeLayout.setVisibility(View.VISIBLE);
                        signUpStep = SignUpStep.CONFIRM;
                        break;
                    default:
                        break;
                }
                preStateButton.setVisibility(View.VISIBLE);
            }
        });

        preStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (signUpStep) {
                    case EMAIL:
                        //DO NOTHING
                        break;
                    case NAME:
                        signUpStep = SignUpStep.EMAIL;
                        emailLayout.setVisibility(View.VISIBLE);
                        nameLayout.setVisibility(View.GONE);
                        preStateButton.setVisibility(View.INVISIBLE);
                        break;
                    case PASSWORD:
                        signUpStep = SignUpStep.EMAIL;
                        passwordLayout.setVisibility(View.GONE);
                        continueButton.performClick();
                        break;
                    case AGE:
                        signUpStep = SignUpStep.NAME;
                        ageLayout.setVisibility(View.GONE);
                        continueButton.performClick();
                        break;
                    case GENDER:
                        signUpStep = SignUpStep.PASSWORD;
                        genderLayout.setVisibility(View.GONE);
                        continueButton.performClick();
                        break;
                    case CONFIRM:
                        signUpStep = SignUpStep.AGE;
                        completeLayout.setVisibility(View.GONE);
                        ((SignUpActivity) getActivity()).disableSignUp();
                        continueButton.performClick();
                        break;
                    default:
                        break;
                }
                continueButton.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    /**
     * Method requestEnableSignUpBtn()
     * 회원정보 전부 입력시 SignUpActivity로 회원가입 버튼 활성화 요청
     */
    private void requestEnableSignUpBtn() {
        SignUpActivity signUpActivity = (SignUpActivity) getActivity();
        signUpActivity.enableSignUp(userForSignUp);
    }

    /**
     * Email/Name/Password EditText에 대해 입력값 없을 경우 '계속' 버튼 보이지 않도록 처리
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
