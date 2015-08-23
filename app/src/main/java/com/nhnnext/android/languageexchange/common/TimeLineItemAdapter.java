package com.nhnnext.android.languageexchange.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 2015. 8. 14..
 * Class TimeLineItemAdapter : timeline item list adapter
 */
public class TimeLineItemAdapter extends RecyclerView.Adapter<TimeLineItemAdapter.ViewHolder> {
    private List<User> users;

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
            this.mPhoto = (ImageView) v.findViewById(R.id.person_photo);
            this.mName = (TextView) v.findViewById(R.id.person_name);
            this.mTime = (TextView) v.findViewById(R.id.person_update_time);
            this.mInfo = (TextView) v.findViewById(R.id.person_info);
        }
    }

    /**
     * TimeLineItemAdapter(ArrayList<User> users)
     * Initialize user list
     * @param users timeline user list
     */
    public TimeLineItemAdapter(ArrayList<User> users) {
        this.users = users;
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
        holder.mTime.setText(users.get(position).getUserUpdateDate());
        holder.mName.setText(users.get(position).getUserName());
        holder.mInfo.setText(users.get(position).getUserIntro());

    }

    // return user list count
    @Override
    public int getItemCount() {
        return users.size();
    }
}
