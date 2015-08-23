package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.match.MatchingActivity;

import java.util.ArrayList;

/**
 * Created by Alpha on 2015. 8. 23..
 * Class FriendListAdapter : searched user list adapter
 */
public class FriendListAdapter extends BaseAdapter implements Filterable {

    private MatchingActivity activity;
    private FriendFilter friendFilter;
    private Typeface typeface;
    private ArrayList<User> friendList;
    private ArrayList<User> filteredList;

    /**
     * FriendListAdapter(MatchingActivity activity, ArrayList<User> friendList)
     * Initialize context variables
     * @param activity MatchingActivity
     * @param friendList user list
     */
    public FriendListAdapter(MatchingActivity activity, ArrayList<User> friendList) {
        this.activity = activity;
        this.friendList = friendList;
        this.filteredList = friendList;
//        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/vegur_2.otf");

        getFilter();
    }

    /**
     * Method getCount()
     * Get size of user list
     * @return userList size
     */
    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Method getItem(int i)
     * Get specific item from user list
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Method getItemId(int i)
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Method getView(int position, View view, ViewGroup parent)
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final User user = (User) getItem(position);

        //TODO 이미지 수정후 제거
        user.setUserImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.square_profile_default));

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.search_list_item, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.search_user_image);
            holder.email = (TextView) view.findViewById(R.id.search_user_email);
            holder.name = (TextView) view.findViewById(R.id.search_user_name);
//            holder.iconText.setTypeface(typeface, Typeface.BOLD);
//            holder.iconText.setTextColor(activity.getResources().getColor(R.color.white));
//            holder.name.setTypeface(typeface, Typeface.NORMAL);

            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
        holder.image.setImageResource(R.drawable.square_profile_default);
        holder.email.setText(user.getUserEmail());
        holder.name.setText(user.getUserName());
//        view.setBackgroundResource(R.drawable.friend_list_selector);

        return view;
    }

    /**
     * Method getFilter()
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }
        return friendFilter;
    }

    /**
     * Class ViewHolder : searched user data
     * Keep reference to children view to avoid unnecessary calls
     */
    private static class ViewHolder {
        ImageView image;
        TextView email;
        TextView name;
    }

    //TODO 제거
    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<User> tempList = new ArrayList<User>();

                // search content in friend list
                for (User user : friendList) {
                    if (user.getUserEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(user);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = friendList.size();
                filterResults.values = friendList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }

}