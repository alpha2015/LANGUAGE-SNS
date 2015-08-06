package com.nhnnext.android.languageexchange.match;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.user.User;

import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 7. 22.
 */
public class Fragment_StartMatch extends Fragment implements View.OnClickListener{
    //TODO native, practicing 언어로 서버에서 해당 유저리스트 받아오기
    //TODO DownloadTargetUserListTask().execute(targetUrl);
    Button matchButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_match, container, false);
        matchButton = (Button)view.findViewById(R.id.start_match_button);
        matchButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //TODO 매칭 시작 버튼 클릭시 서버 DB 접근 유저리스트 가져오기
        int id = v.getId();
        switch(id){
            case R.id.start_match_button :
                new MatchAsyncTask().execute("target url");
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;

            //TODO HttpConnection 구현체 호출, 해당 유저리스트 가져오기 url을 통해 요청
            //TODO 유저리스트 결과값 parsing
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            //progressBar 숨기기
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.setAction("com.nhnnext.android.action.RESULT");
            startActivity(intent);
            //TODO 실패시 실패 사유 표시
            //TODO 성공시 App DB에 parsing 결과 리스트 저장(필요한건지 추후 검토, 필요하다면 DB에 저장된 기존 매칭유저리스트 삭제)
            //TODO 결과보기 버튼 활성화
            //TODO 결과보기 버튼 이벤트 발생시 ShowResultActivity 호출(Parcel로 넘길지, DB에서 가져올지 결정)

            //TODO (Fragment 구현부에서 해야하는지 Fragment 스터디 후에 결정)
            //TODO (Fragment1)매칭결과 리스트를 보여주기 위해 Adapter, list 연결 구현
            //TODO (Fragment1)한 페이지보다 많은 리스트일 경우 더보기 버튼 보여주기 및 클릭 이벤트 설정
            //TODO 더 보기 버튼 클릭시 Fragment1 새로운 유저 리스트로 갱신
            //TODO (Fragment1)각각의 view list 클릭 이벤트 설정후 발생시 Fragment2로 전환
        }
    }
}
