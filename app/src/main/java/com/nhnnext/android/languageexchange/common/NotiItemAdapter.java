package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhnnext.android.languageexchange.Model.MessageInfo;
import com.nhnnext.android.languageexchange.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Alpha on 2015. 8. 6..
 * Class NotiItemAdapter : push notification list adapter
 */
public class NotiItemAdapter extends ArrayAdapter<MessageInfo> {
    private LayoutInflater mInflater;

    /**
     * NotiItemAdapter(Context context, int resource, List<MessageInfo> objects)
     * Initialize context variables
     * @param context Activity context
     * @param resource notification item layout
     * @param objects message info list
     */
    public NotiItemAdapter(Context context, int resource, List<MessageInfo> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.notification_list_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.noti_user_image);
            holder.name = (TextView) convertView.findViewById(R.id.noti_user_name);
            holder.message = (TextView) convertView.findViewById(R.id.noti_message);
            holder.time = (TextView) convertView.findViewById(R.id.notin_user_send_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageBitmap(getItem(position).getUserImage());
        holder.name.setText("" + getItem(position).getUserName());
        holder.message.setText(getItem(position).getMessage());
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        holder.time.setText("" + format.format(getItem(position).getSendTime()));
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView message;
        TextView time;
    }
}
