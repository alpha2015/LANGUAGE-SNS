package com.nhnnext.android.languageexchange.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.R;

/**
 * Created by Alpha on 2015. 8. 14..
 */
public class TimeLineItemAdapter extends  RecyclerView.Adapter<TimeLineItemAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //CartView item - User photo, name, update time, intro desc
        public View v;
        public ImageView mPhoto;
        public TextView mName;
        public TextView mTime;
        public TextView mInfo;
        public ViewHolder(View v) {
            super(v);
            this.v = v;
            this.mPhoto = (ImageView)v.findViewById(R.id.person_photo);
            this.mName = (TextView)v.findViewById(R.id.person_name);
            this.mTime = (TextView)v.findViewById(R.id.person_update_time);
            this.mInfo = (TextView)v.findViewById(R.id.person_info);
        }
    }

    // Card adapter constructor (dataset is matched user info)
    public TimeLineItemAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeLineItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create item list(matched user list)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // user info setting
        holder.mPhoto.setImageResource(R.drawable.sample_image_1);
        holder.mTime.setText(mDataset[position]);
        holder.mName.setText(mDataset[position]);
        holder.mInfo.setText("To use the RecyclerView widget, you have to specify an adapter and a layout manager. To create an adapter, extend the RecyclerView.Adapter class. The details of the implementation depend on the specifics of your dataset and the type of views. For more information, see the examples below.");

    }

    // return user list count
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
