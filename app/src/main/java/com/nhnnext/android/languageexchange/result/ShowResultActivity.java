package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.content.Context;
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
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.ImageLoadHelper;
import com.nhnnext.android.languageexchange.common.UrlFactory;
import com.nhnnext.android.languageexchange.common.UserItemAdapter;
import com.nhnnext.android.languageexchange.user.Fragment_UserProfileDialog;

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
    private RequestQueue queue;
    private GsonRequest<ArrayList<User>> matchRequest;
    private Context mContext;
    private UserParcelable user;

    /**
     * Method onCreate(Bundle savedInstanceState)
     * 매칭된 유저 리스트 보여주기
     * Adapter, list 연결
     * stop - start 간 view 변경될 일 없으므로 onCreate 시점에 adapter, list 등록
     * list item 클릭시 activity 호출 intent 구현, parcelable User instance  전달
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext = this;
        user = getIntent().getExtras().getParcelable("user");
        queue = Volley.newRequestQueue(this);
        Type collectionType = new TypeToken<ArrayList<User>>() {
        }.getType();
        matchRequest = new GsonRequest<ArrayList<User>>(UrlFactory.MATCH, collectionType, null,
                new Response.Listener<ArrayList<User>>() {
                    @Override
                    public void onResponse(ArrayList<User> users) {
                        userListGridView = (GridView) findViewById(R.id.gridview);
                        userListGridView.setAdapter(new UserItemAdapter(mContext, R.layout.user_list_item, users, ImageLoadHelper.getInstance(mContext).getImageLoader()));

                        userListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                UserParcelable parcelUser = new UserParcelable((User) userListGridView.getAdapter().getItem(position));
                                //params reveiver, sender
                                Fragment_UserProfileDialog.newInstance(parcelUser, user).show(getFragmentManager(), "dialog");
                            }
                        });
                        if (users.size() == 0) {
                            userListGridView.setVisibility(View.GONE);
                            findViewById(R.id.no_matched_user).setVisibility(View.VISIBLE);
                        } else {
                            userListGridView.setVisibility(View.VISIBLE);
                            findViewById(R.id.no_matched_user).setVisibility(View.GONE);
                        }
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
                params.put("userEmail", user.getEmail());
                params.put("userNative", user.getNativeLanguage());
                params.put("userPracticing", user.getPracticingLanguage());
                if (user.getOauth() != null)
                    params.put("oAuth", user.getOauth());
                return params;
            }
        };
        queue.add(matchRequest);
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
