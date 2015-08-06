package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 8. 6..
 */
public class UserItemAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_image_1, R.drawable.sample_image_2,
            R.drawable.sample_image_3, R.drawable.sample_image_4,
            R.drawable.sample_image_5, R.drawable.sample_image_6,
            R.drawable.sample_image_7, R.drawable.sample_image_8,
            R.drawable.sample_image_9, R.drawable.sample_image_10,
            R.drawable.sample_image_11, R.drawable.sample_image_12,
            R.drawable.sample_image_13, R.drawable.sample_image_14,
            R.drawable.sample_image_15, R.drawable.sample_image_16
    };

    public UserItemAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public Integer getThumbIds(int position){
        return mThumbIds[position];
    }


}
