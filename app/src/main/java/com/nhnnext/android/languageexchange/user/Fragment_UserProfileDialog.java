package com.nhnnext.android.languageexchange.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.ImageLoadHelper;
import com.nhnnext.android.languageexchange.common.UrlFactory;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 8. 27..
 */
public class Fragment_UserProfileDialog extends DialogFragment implements View.OnClickListener {
    private UserParcelable receiver;
    private UserParcelable sender;
    private EditText messageView;
    private Button messageSendButton;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    /**
     * Method newInstance(int receiver)
     * caller activity로부터 전달 받은 receiver 값 bundle로 저장
     *
     * @param receiver UserParcelable
     * @param sender UserParcelable
     * @return Fragment_UserProfileDialog instance
     */
    public static Fragment_UserProfileDialog newInstance(UserParcelable receiver, UserParcelable sender) {
        Fragment_UserProfileDialog f = new Fragment_UserProfileDialog();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("receiver", receiver);
        args.putParcelable("sender", sender);
        f.setArguments(args);

        return f;
    }

    /**
     * Method getShownReceiver()
     * user값 getter
     *
     * @return activity로부터 전달 받은 receiver 값
     */
    private UserParcelable getShownReceiver() {
        return getArguments().getParcelable("receiver");
    }

    /**
     * Method getShownSender()
     * user값 getter
     *
     * @return activity로부터 전달 받은 receiver 값
     */
    private UserParcelable getShownSender() {
        return getArguments().getParcelable("sender");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        receiver = getShownReceiver();
        sender = getShownSender();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_result_user_detail, null);
        ((NetworkImageView) view.findViewById(R.id.mached_user_image_view)).setImageUrl(receiver.getImage(), ImageLoadHelper.getInstance(getActivity()).getImageLoader());
        ((TextView) view.findViewById(R.id.mached_user_name_view)).setText(receiver.getName());
        ((TextView) view.findViewById(R.id.mached_user_email_view)).setText(receiver.getEmail());
        ((TextView) view.findViewById(R.id.mached_user_gender_view)).setText(receiver.getGenderForKorean());
        ((TextView) view.findViewById(R.id.mached_user_age_view)).setText("" + receiver.getAge());
        messageView = (EditText) view.findViewById(R.id.request_push_message);
        messageSendButton = (Button) view.findViewById(R.id.request_send_message);

        view.findViewById(R.id.request_send_email_button).setOnClickListener(this);
        messageView.addTextChangedListener(textBarWatcher);
        messageSendButton.setOnClickListener(this);

        builder.setView(view)
                .setPositiveButton(R.string.close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.request_send_email_button:
                //메일 보내기 버튼 클릭시 mail send activity 호출
                Uri uri = Uri.parse("mailto:" + receiver.getEmail());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);

                break;
            case R.id.request_send_message:
                //대상 유저에게 PUSH MESSAGE 보내기
                ParsePush push = new ParsePush();
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("email", receiver.getEmail());
                //TODO oAuth까지 같은사람
                //TODO 페이스북 친구 요청이나 타 메신저 친구 요청 기능 구현
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage(messageView.getText() + "\n by " + sender.getName());
                push.sendInBackground();

                StringRequest myReq = new StringRequest(Request.Method.POST, UrlFactory.SEND_MESSAGE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                if (response.equals("success")) {
                                    ;
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "네트워크 설정을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("senderEmail", sender.getEmail());
                        params.put("senderName", sender.getName());
                        if(sender.getOauth() != null) params.put("senderOAuth", sender.getOauth());
                        params.put("receiverEmail", receiver.getEmail());
                        if(receiver.getOauth() != null) params.put("receiverOAuth", receiver.getOauth());
                        params.put("message", "" + messageView.getText());
                        return params;
                    }
                };
                queue.add(myReq);
                break;
        }

    }

    /**
     * Email/Password EditText에 대해 입력값 없을 경우 버튼 비활성화
     */
    private TextWatcher textBarWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (messageView.length() > 0) {
                messageSendButton.setEnabled(true);
            } else {
                messageSendButton.setEnabled(false);
            }
        }
    };
}
