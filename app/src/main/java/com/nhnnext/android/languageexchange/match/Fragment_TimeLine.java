package com.nhnnext.android.languageexchange.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
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
import com.nhnnext.android.languageexchange.common.TimeLineItemAdapter;
import com.nhnnext.android.languageexchange.common.UrlFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class Fragment_TimeLine : user's timeline page depends on update time
 */
public class Fragment_TimeLine extends Fragment {
    private RecyclerView mRecyclerView;
    private TimeLineItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GsonRequest<ArrayList<User>> timelineRequest;
    private RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    /**
     * Method newInstance(UserParcelable user)
     * caller activity로부터 전달 받은 user 값 bundle로 저장
     *
     * @param user 매칭할 언어 정보를 담고 있는 user data
     * @return Fragment_TimeLine instance
     */
    public static Fragment_TimeLine newInstance(UserParcelable user) {
        Fragment_TimeLine f = new Fragment_TimeLine();

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

    /**
     * Method onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
     * 가장 최근 업데이트된 사용자 리스트 server로 부터 읽어오기(최대 20개)
     *
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // RecyclerView 사이즈 고정
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // user list adapter 등록
        Type collectionType = new TypeToken<ArrayList<User>>() {
        }.getType();
        timelineRequest = new GsonRequest<ArrayList<User>>(Request.Method.GET, UrlFactory.TIMELINE, collectionType, null,
                new Response.Listener<ArrayList<User>>() {
                    @Override
                    public void onResponse(ArrayList<User> users) {
                        mAdapter = new TimeLineItemAdapter(users, getShownIndex(), ImageLoadHelper.getInstance(getActivity()).getImageLoader(), getActivity().getFragmentManager());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "네트워크 설정을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(timelineRequest);
        return view;
    }
}
