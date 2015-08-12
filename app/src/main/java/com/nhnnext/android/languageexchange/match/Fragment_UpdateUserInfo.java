package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.user.User;
import com.nhnnext.android.languageexchange.user.UserParcelable;

/**
 * Created by Alpha on 2015. 7. 22..
 */
//TODO 질문사항 1)
//TODO 코드리뷰시 질문하기) Fragment 내부에 dialog를 위한 fragment 구현이 올바른 설계인지(static으로 인한 문제)
//TODO 잘못된것이라면 어떤식으로 설계를 바꾸는것이 좋은지?
public class Fragment_UpdateUserInfo extends Fragment implements View.OnClickListener {
    //TODO 사진찍기 Activity 호출, 프로필 사진으로 저장 구현(서버DB 저장)
    //TODO 갤러리 접근, 프로필 사진으로 저장 구현(서버DB 저장)
    private EditText editEmail;
    private EditText editName;
    private EditText editPassword;
    private static TextView editAge;
    private static TextView editGender;
    private Button saveButton;
    //test를 위한 dummy user data(db구현시 제거)
//    private User user = new User(null, "test@naver.com", "김아무개", "1234", 30, "male", null, null, null);
    private UserParcelable user;

    public static Fragment_UpdateUserInfo newInstance(UserParcelable user) {
        Fragment_UpdateUserInfo f = new Fragment_UpdateUserInfo();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        f.setArguments(args);

        return f;
    }

    private UserParcelable getShownIndex() {
        UserParcelable user = getArguments().getParcelable("user");
        return user;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO DB에서 가져온 회원정보 보여주기

        user = getShownIndex();

        //레이아웃 view
        View view = inflater.inflate(R.layout.fragment_update_userinfo, container, false);
        editEmail = (EditText) view.findViewById(R.id.setting_edit_email);
        editName = (EditText) view.findViewById(R.id.setting_edit_name);
        editPassword = (EditText) view.findViewById(R.id.setting_edit_password);
        editAge = (TextView) view.findViewById(R.id.setting_edit_age);
        editGender = (TextView) view.findViewById(R.id.setting_edit_gender);
        saveButton = (Button) view.findViewById(R.id.setting_save);
        //각 회원정보에 대한 클릭, 저장 이벤트 등록
        editAge.setOnClickListener(this);
        editGender.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        //DB에서 가져온 user data view에 설정
        editEmail.setText(user.getEmail());
        editName.setText(user.getName());
        editPassword.setText("********");
        editAge.setText(Integer.toString(user.getAge()));
        editGender.setText(user.getGenderForKorean());
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.setting_edit_age:
//                AgePickerDialog.newInstance(user.getAge()).show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_edit_gender:
//                GenderRadioDialog.newInstance(user.getGender()).show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_save:
                //TODO App, SERVER DB 저장 구현
                user.setEmail(editEmail.getText().toString());
                user.setName(editName.getText().toString());
                user.setPassword(editPassword.getText().toString());
                user.setAge(Integer.parseInt(editAge.getText().toString()));
                if (editGender.getText().equals("남성"))
                    user.setGender("male");
                else
                    user.setGender("female");
                Toast.makeText(getActivity().getApplicationContext(), "저장 완료!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //TODO Fragment 스터디후 반드시 수정(리팩토링)
    public static class AgePickerDialog extends DialogFragment {
        private NumberPicker npView;

        public static AgePickerDialog newInstance(int age) {
            AgePickerDialog f = new AgePickerDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", age);
            f.setArguments(args);

            return f;
        }

        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            npView = (NumberPicker) inflater.inflate(R.layout.age_picker_dialog_layout, null);
            npView.setWrapSelectorWheel(false);
            npView.setMinValue(0);
            npView.setMaxValue(99);
            npView.setValue(getShownIndex());
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("나이를 설정하세요.");
            builder.setView(npView)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editAge.setText(Integer.toString(npView.getValue()));
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // DO NOTHING
                        }
                    });
            return builder.create();
        }
    }

    //TODO Fragment 스터디후 반드시 수정(리팩토링)
    public static class GenderRadioDialog extends DialogFragment {
        private RadioGroup npView;

        public static GenderRadioDialog newInstance(String gender) {
            GenderRadioDialog f = new GenderRadioDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putString("index", gender);
            f.setArguments(args);

            return f;
        }

        public char getShownIndex() {
            return getArguments().getChar("index", 'N');
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            npView = (RadioGroup) inflater.inflate(R.layout.gender_radio_dialog_layout, null);
            if (getShownIndex() == 'M') {
                RadioButton maleRadio = (RadioButton) npView.findViewById(R.id.gender_male);
                maleRadio.setChecked(true);
            } else {
                RadioButton femaleRadio = (RadioButton) npView.findViewById(R.id.gender_female);
                femaleRadio.setChecked(true);
            }

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("성별을 설정하세요.");
            builder.setView(npView)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (npView.getCheckedRadioButtonId() == R.id.gender_male) {
                                editGender.setText("남성");
                            } else {
                                editGender.setText("여성");
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // DO NOTHING
                        }
                    });
            return builder.create();
        }
    }
}
