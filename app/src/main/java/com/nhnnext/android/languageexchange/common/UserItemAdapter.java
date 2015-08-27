package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.R;

import java.util.List;

/**
 * Created by Alpha on 2015. 8. 6..
 * Class UserItemAdapter : matched user item list adapter
 */
public class UserItemAdapter extends ArrayAdapter<User> {
    private LayoutInflater mInflater;

    public UserItemAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_list_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_user_image);
            holder.name = (TextView) convertView.findViewById(R.id.item_user_name);
            holder.gender = (TextView) convertView.findViewById(R.id.item_user_gender);
            holder.age = (TextView) convertView.findViewById(R.id.item_user_age);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.image.setImageBitmap(getItem(position).getUserImage());
        holder.name.setText("" + getItem(position).getUserName());
        holder.gender.setText(getItem(position).getGenderForKorean());
        holder.age.setText(Integer.toString(getItem(position).getUserAge()));
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView gender;
        TextView age;
    }

}
