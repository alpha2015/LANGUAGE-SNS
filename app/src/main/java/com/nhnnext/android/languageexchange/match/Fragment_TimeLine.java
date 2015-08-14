package com.nhnnext.android.languageexchange.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.TimeLineItemAdapter;

/**
 * Created by Alpha on 2015. 7. 22..
 * matched(native, practicing language) user's info page depends on update time
 */
public class Fragment_TimeLine extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"}; //user info dummy code

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
        mAdapter = new TimeLineItemAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
