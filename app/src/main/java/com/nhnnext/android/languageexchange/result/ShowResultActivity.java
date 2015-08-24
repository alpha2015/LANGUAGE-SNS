package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.FriendListAdapter;
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.UserItemAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class ShowResultActivity : 매칭된 사용자 리스트 보여주기 activity
 */
public class ShowResultActivity extends Activity {
    private GridView userListGridView;
//    private ArrayList<User> userList;
    private RequestQueue queue;
    private GsonRequest<ArrayList<User>> matchRequest;
    private Context mContext;

    /**
     * Method onCreate(Bundle savedInstanceState)
     * 매칭된 유저 리스트 보여주기
     * Adapter, list 연결
     * stop - start 간 view 변경될 일 없으므로 onCreate 시점에 adapter, list 등록
     * list item 클릭시 activity 호출 intent 구현, parcelable User instance  전달
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext = this;
        final String userNative = getIntent().getExtras().getString("userNative");
        final String userPracticing = getIntent().getExtras().getString("userPracticing");

        Log.d("testtt", userNative + "  " + userPracticing);

        queue = Volley.newRequestQueue(this);
        Type collectionType = new TypeToken<ArrayList<User>>() {
        }.getType();
        String matchUrl = "http://10.0.3.2:8080/user/match";
        matchRequest = new GsonRequest<ArrayList<User>>(matchUrl, collectionType, null,
                new Response.Listener<ArrayList<User>>() {
                    @Override
                    public void onResponse(ArrayList<User> users) {
                        for(int i = 0; i< users.size();i++)
                            users.get(0).setUserImage(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_1));
                        userListGridView = (GridView) findViewById(R.id.gridview);
                        userListGridView.setAdapter(new UserItemAdapter(mContext, R.layout.user_list_item, users));

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

//                        Log.d("searchresult", "" + users);
//                        friendListAdapter = new FriendListAdapter(MatchingActivity.this, users);
//                        friendListView.setAdapter(friendListAdapter);
//                        friendListView.setTextFilterEnabled(false);
//
//                        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            public void onItemClick(AdapterView<?> parent, View v,
//                                                    int position, long id) {
//                                Intent intent = new Intent();
//                                intent.setAction("com.nhnnext.android.action.DETAIL");
//                                //상세보기 activity 호출시 해당 유저 정보 parcelable instance로 전달
//                                UserParcelable parcelUser = new UserParcelable((User) friendListView.getAdapter().getItem(position));
//                                intent.putExtra("user", parcelUser);
//                                startActivity(intent);
//                            }
//                        });

                        Toast.makeText(getApplicationContext(), "검색 성공", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "검색 실패", Toast.LENGTH_SHORT).show();
            }
        }) {
            /**
             * Method getParams()
             * @return match를 위한 언어 prams
             * @throws AuthFailureError
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userNative", userNative);
                params.put("userPracticing", userPracticing);
                return params;
            }
        };
        queue.add(matchRequest);




//        userList = new ArrayList<>(); //유저리스트 저장을 위한 ArrayList
//        //dummy user list item
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_1), "sample1@naver.com",
//                "Rebecca", null, 21, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_2), "sample2@naver.com",
//                "TOM", null, 22, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_3), "sample3@naver.com",
//                "JUNG WOO", null, 23, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_4), "sample4@naver.com",
//                "Liu", null, 24, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_5), "sample5@naver.com",
//                "Emilia", null, 25, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_6), "sample6@naver.com",
//                "Ko-eun", null, 26, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_7), "sample7@naver.com",
//                "천우희", null, 27, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_8), "sample8@naver.com",
//                "전지현", null, 28, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_9), "sample9@naver.com",
//                "Seydoux", null, 29, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_10), "sample10@naver.com",
//                "은미", null, 30, "female", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_11), "sample11@naver.com",
//                "Jeremy", null, 31, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_12), "sample12@naver.com",
//                "Christopher", null, 32, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_13), "sample13@naver.com",
//                "이정재", null, 33, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_14), "sample14@naver.com",
//                "황정민", null, 34, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_15), "sample15@naver.com",
//                "달수", null, 35, "male", null, null, null, null, null, null));
//        userList.add(new User(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_16), "sample16@naver.com",
//                "임지연", null, 36, "female", null, null, null, null, null, null));
//
//        //TODO USER Class 자체를 Parcelable로 변경할지 DB 설계시 검토
//        //TODO userList DB에서 읽어올지 Parcel로 전달 받을지 DB 설계시 결정할 것
//        userListGridView = (GridView) findViewById(R.id.gridview);
//        userListGridView.setAdapter(new UserItemAdapter(this, R.layout.user_list_item, userList));
//
//        userListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction("com.nhnnext.android.action.DETAIL");
//                //상세보기 activity 호출시 해당 유저 정보 parcelable instance로 전달
//                UserParcelable parcelUser = new UserParcelable((User) userListGridView.getAdapter().getItem(position));
//                intent.putExtra("user", parcelUser);
//                startActivity(intent);
//            }
//        });
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
