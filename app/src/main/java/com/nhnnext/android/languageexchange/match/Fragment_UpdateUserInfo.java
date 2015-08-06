package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.net.InterfaceAddress;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class Fragment_UpdateUserInfo extends Fragment implements View.OnClickListener {
    //TODO DB에서 가져온 회원정보 보여주기
    //TODO 각 회원정보에 대한 클릭, 저장 이벤트 등록
    //TODO 사진찍기 Activity 호출, 프로필 사진으로 저장 구현(서버DB 저장)
    //TODO 갤러리 접근, 프로필 사진으로 저장 구현(서버DB 저장)
    private EditText editEmail;
    private EditText editName;
    private EditText editPassword;
    private static TextView editAge;
    private static TextView editGender;
    private Button saveButton;

    private static User user = new User(null, "test@naver.com", "김아무개", "1234", 30, 'M', null, null);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_userinfo, container, false);
        editEmail = (EditText) view.findViewById(R.id.setting_edit_email);
        editName = (EditText) view.findViewById(R.id.setting_edit_name);
        editPassword = (EditText) view.findViewById(R.id.setting_edit_password);
        editAge = (TextView) view.findViewById(R.id.setting_edit_age);
        editGender = (TextView) view.findViewById(R.id.setting_edit_gender);
        saveButton = (Button) view.findViewById(R.id.setting_save);
        editAge.setOnClickListener(this);
        editGender.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        editEmail.setText(user.getEmail());
        editName.setText(user.getName());
        editPassword.setText("********");
        editAge.setText(Integer.toString(user.getAge()));
        editGender.setText(user.getGenderForKorean());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.setting_edit_age:
                new AgePickerDialog().show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_edit_gender:
                new GenderRadioDialog().show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_save:
                //TODO App, SERVER DB 저장 구현
                user.setEmail(editEmail.getText().toString());
                user.setName(editName.getText().toString());
                user.setPassword(editPassword.getText().toString());
                user.setAge(Integer.parseInt(editAge.getText().toString()));
                if (editGender.getText().equals("남성"))
                    user.setGender('M');
                else
                    user.setGender('F');
                Toast.makeText(getActivity().getApplicationContext(), "저장 완료!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class AgePickerDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final NumberPicker npView = (NumberPicker) inflater.inflate(R.layout.age_picker_dialog_layout, null);
            npView.setWrapSelectorWheel(false);
            npView.setMinValue(0);
            npView.setMaxValue(99);
            npView.setValue(user.getAge());
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("나이를 설정하세요.");
            builder.setView(npView)
//            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editAge.setText(Integer.toString(npView.getValue()));
                            Toast.makeText(getActivity().getApplicationContext(), "" + npView.getValue(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Toast.makeText(getActivity().getApplicationContext(), "나이 변경을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
//            return super.onCreateDialog(savedInstanceState);
        }
    }

    public static class GenderRadioDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RadioGroup npView = (RadioGroup) inflater.inflate(R.layout.gender_radio_dialog_layout, null);
            if (user.getGender() == 'M') {
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
//            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (npView.getCheckedRadioButtonId() == R.id.gender_male) {
                                editGender.setText("남성");
                                user.setGender('M');
                            } else {
                                editGender.setText("여성");
                                user.setGender('F');
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Toast.makeText(getActivity().getApplicationContext(), "성별 변경을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
//            return super.onCreateDialog(savedInstanceState);
        }
    }
}
