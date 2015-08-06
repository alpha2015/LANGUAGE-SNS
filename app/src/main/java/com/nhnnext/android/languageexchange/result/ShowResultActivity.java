package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.UserItemAdapter;
import com.nhnnext.android.languageexchange.user.User;
import com.nhnnext.android.languageexchange.user.UserParcelable;

import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 7. 22..
 */

public class ShowResultActivity extends Activity {
    private GridView userListGridView;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        userList = new ArrayList<>(); //유저리스트 저장을 위한 ArrayList
        //dummy user list item
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_1), "sample1@naver.com",
                "Rebecca", null, 21, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_2), "sample1@naver.com",
                "TOM", null, 22, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_3), "sample1@naver.com",
                "JUNG WOO", null, 23, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_4), "sample1@naver.com",
                "Liu", null, 24, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_5), "sample1@naver.com",
                "Emilia", null, 25, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_6), "sample1@naver.com",
                "Cindy", null, 26, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_7), "sample1@naver.com",
                "Ko-eun", null, 27, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_8), "sample1@naver.com",
                "천우희", null, 28, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_9), "sample1@naver.com",
                "전지현", null, 29, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_10), "sample1@naver.com",
                "Seydoux", null, 30, 'F'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_11), "sample1@naver.com",
                "Jeremy", null, 31, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_12), "sample1@naver.com",
                "Christopher", null, 32, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_13), "sample1@naver.com",
                "이정재", null, 33, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_14), "sample1@naver.com",
                "황정민", null, 34, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_15), "sample1@naver.com",
                "달수", null, 35, 'M'));
        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_16), "sample1@naver.com",
                "임지연", null, 36, 'F'));

        /*
            매칭된 유저 리스트 보여주기
            Adapter, list 연결
            // stop - start 간 view 변경될 일 없으므로 onCreate 시점에 adapter, list 등록
            // list item 클릭시 activity 호출 intent 구현, parcelable User instance  전달
            //TODO USER Class 자체를 Parcelable로 변경할지 DB 설계시 검토
            //TODO userList DB에서 읽어올지 Parcel로 전달 받을지 DB 설계시 결정할 것
         */
        userListGridView = (GridView) findViewById(R.id.gridview);
        userListGridView.setAdapter(new UserItemAdapter(this, R.layout.user_list_item, userList));

        userListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setAction("com.nhnnext.android.action.DETAIL");
                //상세보기 activity 호출시 해당 유저 정보 parcelable instance로 전달
                UserParcelable parcelUser = new UserParcelable((User) userListGridView.getAdapter().getItem(position));
                intent.putExtra("user", parcelUser);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
