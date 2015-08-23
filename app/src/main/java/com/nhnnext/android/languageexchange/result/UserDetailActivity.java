package com.nhnnext.android.languageexchange.result;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by Alpha on 2015. 8. 6..
 * Class UserDetailActivity : 사용자 정보 상세보기 activity
 */
public class UserDetailActivity extends Activity implements View.OnClickListener {
    private ImageView userImage;
    private TextView userName;
    private TextView userGender;
    private TextView userAge;
    private Button requestSendEmail;
    private Button requestFriend;
    private UserParcelable user;

    //TODO 이전 Activity로 돌아가기 위해 Action bar가 필요할지 검토
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_user_detail);

        //레이아웃 view
        userImage = (ImageView) findViewById(R.id.mached_user_image_view);
        userName = (TextView) findViewById(R.id.mached_user_name_view);
        userGender = (TextView) findViewById(R.id.mached_user_gender_view);
        userAge = (TextView) findViewById(R.id.mached_user_age_view);
        requestSendEmail = (Button) findViewById(R.id.request_send_email_button);
        requestFriend = (Button) findViewById(R.id.request_friend_button);

        //메일보내기, 친구요청 버튼 리스너 등록
        requestSendEmail.setOnClickListener(this);
        requestFriend.setOnClickListener(this);

        /*
            intent로 전달받은 parcelable user object 담아서 view에 설정
            Activity가 불린 직후의 user값은 변하지 않으므로 onCreate에서 view data 설정
         */
        user = getIntent().getExtras().getParcelable("user");
        userImage.setImageBitmap(user.getImage());
        userName.setText(user.getName());
        userGender.setText(user.getGenderForKorean());
        userAge.setText(Integer.toString(user.getAge()));

    }

    /**
     * Method onClick()
     * @param v : clicked view
     * 이메일 보내기 버튼 클릭시) main send activity 호출
     * 메세지 보내기 버튼 클릭시) 대상 유저에게 PUSH MESSAGE 보내기
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.request_send_email_button:
                //메일 보내기 버튼 클릭시 mail send activity 호출
//                Uri uri = Uri.parse("mailto:" + user.getEmail());
//                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//                startActivity(intent);

                //TODO 대상 유저에게 PUSH MESSAGE 보내기
                ParsePush push = new ParsePush();
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("email", "test@naver.com");
                push.setQuery(pushQuery); // Set our Installation query

                push.setMessage("test@naver.com" + "hello!!!");
                push.sendInBackground();
                break;
            case R.id.request_friend_button:
                //TODO 페이스북 친구 요청이나 타 메신저 친구 요청 기능 구현
                Toast.makeText(UserDetailActivity.this, "친구요청 하기를 수행합니다.",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
