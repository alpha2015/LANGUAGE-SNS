package com.nhnnext.android.languageexchange.common;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.user.Fragment_UserProfileDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpha on 2015. 8. 14..
 * Class TimeLineItemAdapter : timeline item list adapter
 */
public class TimeLineItemAdapter extends RecyclerView.Adapter<TimeLineItemAdapter.ViewHolder> {
    private List<User> users;
    private ImageLoader mImageLoader;
    private FragmentManager mFragmentManager;
//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //CartView item - User photo, name, update time, intro desc
        public View v;
        public NetworkImageView mPhoto;
        public TextView mName;
        public TextView mTime;
        public TextView mInfo;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
            this.v.setOnClickListener(this);
            this.mPhoto = (NetworkImageView) v.findViewById(R.id.person_photo);
            this.mName = (TextView) v.findViewById(R.id.person_name);
            this.mTime = (TextView) v.findViewById(R.id.person_update_time);
            this.mInfo = (TextView) v.findViewById(R.id.person_info);
        }

        @Override
        public void onClick(View view) {
            UserParcelable parcelUser = new UserParcelable(users.get(getPosition()));
            Fragment_UserProfileDialog.newInstance(parcelUser).show(mFragmentManager, "dialog");
        }

    }

    /**
     * TimeLineItemAdapter(ArrayList<User> users)
     * Initialize user list
     *
     * @param users timeline user list
     */
    public TimeLineItemAdapter(ArrayList<User> users, ImageLoader imageLoader, FragmentManager fragmentManager) {
        this.users = users;
        this.mImageLoader = imageLoader;
        this.mFragmentManager = fragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeLineItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create item list(matched user list)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // user info setting
        holder.mPhoto.setImageUrl(users.get(position).getUserImage(), mImageLoader);
        holder.mTime.setText(users.get(position).getUserUpdateDate().substring(0,10));
        holder.mName.setText(users.get(position).getUserName());
        holder.mInfo.setText(users.get(position).getUserIntro());

    }

    // return user list count
    @Override
    public int getItemCount() {
        return users.size();
    }
}
