package com.nhnnext.android.languageexchange.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.MySingleton;
import com.nhnnext.android.languageexchange.common.TimeLineItemAdapter;
import com.nhnnext.android.languageexchange.common.UrlFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class Fragment_TimeLine : user's timeline page depends on update time
 */
public class Fragment_TimeLine extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<User> users;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                users = loadTimelineListFromNetwork();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new TimeLineItemAdapter(users, MySingleton.getInstance(getActivity()).getImageLoader());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
        return view;
    }

    /**
     * Method loadTimelineListFromNetwork()
     * 가장 최근 업데이트된 사용자 리스트 server로 부터 읽어오기(최대 20개)
     * @return user list
     */
    private ArrayList<User> loadTimelineListFromNetwork() {
        HttpURLConnection connection = null;
        String result = null;
        try {
            URL url = new URL(UrlFactory.TIMELINE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("error code = " + responseCode);
            }

            InputStreamReader tmp = new InputStreamReader(connection.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str);
            }
            result = builder.toString();


            ArrayList<User> users = new Gson().fromJson(result, new TypeToken<List<User>>() {
            }.getType());
            return users;

        } catch (IOException e) {
            Log.e("Error", "IOException occurred", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
