package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.user.User;
import com.nhnnext.android.languageexchange.user.UserParcelable;

import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 7. 22.
 */
public class Fragment_StartMatch extends Fragment implements View.OnClickListener {
    //TODO 서버 DB를 통해 설정된 default native, practicing 언어 가져오기
    //TODO 선택할 수 있는 언어 종류 저장 할 DB 설계
    //TODO App DB, Server DB에 변경된 언어 저장.
    //TODO "모국어와 배우고자하는 언어를 다르게 선택해주세요." 메시지

    private Button nativeBtn;
    private Button practicingBtn;
    private TextView selectedNative;
    private TextView selectedPracticing;
    private Button matchButton;
    private UserParcelable user;
    private int dialogType;

    public static Fragment_StartMatch newInstance(UserParcelable user) {
        Fragment_StartMatch f = new Fragment_StartMatch();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        f.setArguments(args);

        return f;
    }

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

        return view;
    }


    private void DialogLanguageSelectOption(int index) {
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

    @Override
    public void onStart() {
        super.onStart();
        //TODO Fragment 전환후 바뀐 native, practicing language에 따라 TextView text 변환
        //TODO Fragment 라이프 사이클 스터디후 해당 위치에 view 변경하는게 맞는지 검토
    }

    @Override
    public void onClick(View v) {
        //TODO 매칭 시작 버튼 클릭시 서버 DB 접근 유저리스트 가져오기
        //TODO 매칭 유저리스트 정상적으로 받았을시 결과보기 버튼 이벤트 처리
        int id = v.getId();
        switch (id) {
            case R.id.start_match_button:
                new MatchAsyncTask().execute("target url");
                break;

            //TODO ENUM으로 리팩토링!!!!!
            case R.id.native_language:
                dialogType = 1;
                if (user.getNativeLanguage() == null)
                    DialogLanguageSelectOption(0);
                else if (user.getNativeLanguage().equals("Korean"))
                    DialogLanguageSelectOption(0);
                else if (user.getNativeLanguage().equals("English"))
                    DialogLanguageSelectOption(1);
                else if (user.getNativeLanguage().equals("Japanese"))
                    DialogLanguageSelectOption(2);
                else if (user.getNativeLanguage().equals("Chinese"))
                    DialogLanguageSelectOption(3);
                else
                    DialogLanguageSelectOption(4);
                break;

            //TODO ENUM으로 리팩토링!!!!!
            case R.id.practicing_language:
                dialogType = 2;
                if (user.getNativeLanguage() == null)
                    DialogLanguageSelectOption(1);
                else if (user.getPracticingLanguage().equals("Korean"))
                    DialogLanguageSelectOption(0);
                else if (user.getPracticingLanguage().equals("English"))
                    DialogLanguageSelectOption(1);
                else if (user.getPracticingLanguage().equals("Japanese"))
                    DialogLanguageSelectOption(2);
                else if (user.getPracticingLanguage().equals("Chinese"))
                    DialogLanguageSelectOption(3);
                else
                    DialogLanguageSelectOption(4);
                break;
        }

    }

    private class MatchAsyncTask extends AsyncTask<String, Void, ArrayList<User>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressBar 표시
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("회원매칭 중");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            /*
                loadXmlFromNetwork 구현부
                //TODO native, practicing 언어로 서버에서 해당 유저리스트 결과 parsing
             */
            try {
                Thread.sleep(500); //progressBar 정상 동작 테스트를 위한 sleep, loadXmlFromNetwork구현시 제거
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            //progressBar 숨기기
            progressDialog.dismiss();
            /*
                parsing 결과에 따른 view 처리
                //TODO 실패시 실패 사유 표시
                //TODO 성공시 App DB에 parsing 결과 리스트 저장(DB에 저장할지 ShowResultActivity로 리스트 전달할지 DB구현시 결정)
                //TODO 결과보기 버튼 활성화
                //TODO 결과보기 버튼 이벤트 발생시 ShowResultActivity 호출
             */
            Intent intent = new Intent();
            intent.setAction("com.nhnnext.android.action.RESULT");
            startActivity(intent);


        }
    }
}
