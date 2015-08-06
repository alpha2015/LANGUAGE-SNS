package com.nhnnext.android.languageexchange.match;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class Fragment_SelectLanguage extends Fragment {
    private ArrayAdapter<CharSequence> adapter;
    private Spinner nativeLanguageSpinner;
    private Spinner practicingLanguageSpinner;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_language, container, false);
        nativeLanguageSpinner = (Spinner) view.findViewById(R.id.native_language);
        practicingLanguageSpinner = (Spinner) view.findViewById(R.id.practicing_language);
        saveButton = (Button) view.findViewById(R.id.selected_language_save_button);

        //TODO 서버 DB를 통해 설정된 default native, practicing 언어 가져오기
        //TODO 선택할 수 있는 언어 종류 저장 할 DB 설계
        //spinner에 item 설정
        adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nativeLanguageSpinner.setAdapter(adapter);
        practicingLanguageSpinner.setAdapter(adapter);

        /*
            저장버튼 이벤트 등록
            1) native, practicing 언어 동일시 실패 문구 toast 알림(저장 수행하지 않음)
            2) native, practicing 언어 다를시 DB저장, 저장되었다는 문구 toast 알림
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nativeLanguageSpinner.getSelectedItem().equals(practicingLanguageSpinner.getSelectedItem())) {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "모국어와 배우고자하는 언어를 다르게 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    //TODO App DB, Server DB에 변경된 언어 저장.
                }
            }
        });

        return view;
    }
}
