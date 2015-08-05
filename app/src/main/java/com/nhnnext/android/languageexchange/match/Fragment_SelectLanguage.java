package com.nhnnext.android.languageexchange.match;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 7. 22..
 */
public class Fragment_SelectLanguage extends Fragment {
    //TODO 서버 DB를 통해 default native, practicing 언어 가져오기
    //TODO native, practicing 언어 선택, 서버 DB에 저장
    //TODO StartMatch Fragment로 전환 기능 구현

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_language, container, false);
        Spinner s1 = (Spinner) view.findViewById(R.id.native_language);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s1.setAdapter(adapter);

//        return inflater.inflate(R.layout.fragment_select_language, container, false);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),
                        adapter.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });
        return view;
    }
}
