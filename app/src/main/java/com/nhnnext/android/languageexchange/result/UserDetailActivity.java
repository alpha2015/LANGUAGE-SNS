package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.UserItemAdapter;

/**
 * Created by Alpha on 2015. 8. 6..
 */
public class UserDetailActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_user_detail);

        int positon = getIntent().getExtras().getInt("id");
        UserItemAdapter userItemAdapter = new UserItemAdapter(this);

        ImageView imageView = (ImageView)findViewById(R.id.mached_user_image_view);
        imageView.setImageResource(userItemAdapter.getThumbIds(positon));
    }
}
