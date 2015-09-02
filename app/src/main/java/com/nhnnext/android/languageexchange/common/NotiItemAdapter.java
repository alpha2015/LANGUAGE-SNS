package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.net.Network;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
    private ImageLoader mImageLoader;

    /**
     * NotiItemAdapter(Context context, int resource, List<MessageInfo> objects)
     * Initialize context variables
     *
     * @param context  Activity context
     * @param resource notification item layout
     * @param objects  message info list
     * @param imageLoader networkImageLoader
     */
    public NotiItemAdapter(Context context, int resource, List<MessageInfo> objects, ImageLoader imageLoader) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        this.mImageLoader = imageLoader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.notification_list_item, parent, false);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.noti_user_image);
            holder.name = (TextView) convertView.findViewById(R.id.noti_user_name);
            holder.message = (TextView) convertView.findViewById(R.id.noti_message);
            holder.time = (TextView) convertView.findViewById(R.id.notin_user_send_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageInfo messageInfo = getItem(position);
        holder.image.setImageUrl(UrlFactory.getProfileImagePath(messageInfo), mImageLoader);
        holder.name.setText("" + messageInfo.getSenderName());
        holder.message.setText(messageInfo.getMessage());
        holder.time.setText(messageInfo.getSendTime().substring(0, 16));
        return convertView;
    }

    private static class ViewHolder {
        NetworkImageView image;
        TextView name;
        TextView message;
        TextView time;
    }
}
