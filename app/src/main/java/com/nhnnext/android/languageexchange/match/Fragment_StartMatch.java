package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 22.
 */

/**
 * Created by Alpha on 2015. 7. 22..
 * Class Fragment_StartMatch : 매칭시작 fragment
 */
public class Fragment_StartMatch extends Fragment implements View.OnClickListener {
    //User data로 부터 native, practicing 언어 가져오기
    //App DB, Server DB에 변경된 언어 저장.
    private Button nativeBtn;
    private Button practicingBtn;
    private TextView selectedNative;
    private TextView selectedPracticing;
    private Button matchButton;
    private UserParcelable user;
    private int dialogType;

    /**
     * Method newInstance(UserParcelable user)
     * caller activity로부터 전달 받은 user 값 bundle로 저장
     *
     * @param user 매칭할 언어 정보를 담고 있는 user data
     * @return Fragment_StartMatch instance
     */
    public static Fragment_StartMatch newInstance(UserParcelable user) {
        Fragment_StartMatch f = new Fragment_StartMatch();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        f.setArguments(args);

        return f;
    }

    /**
     * Method getShownIndex()
     * user data getter
     *
     * @return activity로부터 전달 받은 data 값
     */
    private UserParcelable getShownIndex() {
        return getArguments().getParcelable("user");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_match, container, false);
        matchButton = (Button) view.findViewById(R.id.start_match_button);
        matchButton.setOnClickListener(this);

        nativeBtn = (Button) view.findViewById(R.id.native_language);
        practicingBtn = (Button) view.findViewById(R.id.practicing_language);
        selectedNative = (TextView) view.findViewById(R.id.selected_native);
        selectedPracticing = (TextView) view.findViewById(R.id.selected_practicing);

        nativeBtn.setOnClickListener(this);
        practicingBtn.setOnClickListener(this);

        user = getShownIndex();
        if (user.getNativeLanguage() == null)
            user.setNativeLanguage("Korean");
        if (user.getPracticingLanguage() == null)
            user.setPracticingLanguage("English");

        return view;
    }

    /**
     * Method dialogLanguageSelectOption(int index)
     * language 선택 다이얼로그 호출
     *
     * @param index native dialog / practicing dialog
     */
    private void dialogLanguageSelectOption(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        if (dialogType == 1)
            builder.setTitle("Select a Native Language");
        else
            builder.setTitle("Select a Practicing Language");
        builder.setSingleChoiceItems(R.array.languages, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                String[] langs = getResources().getStringArray(R.array.languages);
                if (dialogType == 1) {
                    user.setNativeLanguage(langs[position]);
                    selectedNative.setText(user.getNativeLanguage());
                } else {
                    user.setPracticingLanguage(langs[position]);
                    selectedPracticing.setText(user.getPracticingLanguage());
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * Method onClick(View v)
     *
     * @param v : clicked view
     *          매칭시작 클릭시) 매칭된 사용자 리스트 검색을 위해 server db조회
     *          모국어 선택 버튼 클릭시) 모국어어 선택 dialog 출
     *          배울 언어 선택 클릭시) 배울 언어 선택 dialog 출
     */
    @Override
    public void onClick(View v) {
        //매칭 시작 버튼 클릭시 서버 DB 접근 유저리스트 가져오기
        //TODO 추후 ENUM으로 리팩토링
        int id = v.getId();
        switch (id) {
            case R.id.start_match_button:
                Intent intent = new Intent();
                intent.setAction("com.nhnnext.android.action.RESULT");
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case R.id.native_language:
                dialogType = 1;
                if (user.getNativeLanguage() == null)
                    dialogLanguageSelectOption(0);
                else if (user.getNativeLanguage().equals("Korean"))
                    dialogLanguageSelectOption(0);
                else if (user.getNativeLanguage().equals("English"))
                    dialogLanguageSelectOption(1);
                else if (user.getNativeLanguage().equals("Japanese"))
                    dialogLanguageSelectOption(2);
                else if (user.getNativeLanguage().equals("Chinese"))
                    dialogLanguageSelectOption(3);
                else
                    dialogLanguageSelectOption(4);
                break;
            case R.id.practicing_language:
                dialogType = 2;
                if (user.getPracticingLanguage() == null)
                    dialogLanguageSelectOption(1);
                else if (user.getPracticingLanguage().equals("Korean"))
                    dialogLanguageSelectOption(0);
                else if (user.getPracticingLanguage().equals("English"))
                    dialogLanguageSelectOption(1);
                else if (user.getPracticingLanguage().equals("Japanese"))
                    dialogLanguageSelectOption(2);
                else if (user.getPracticingLanguage().equals("Chinese"))
                    dialogLanguageSelectOption(3);
                else
                    dialogLanguageSelectOption(4);
                break;
        }
    }
}
