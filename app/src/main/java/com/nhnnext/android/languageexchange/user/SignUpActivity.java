package com.nhnnext.android.languageexchange.user;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 7. 21..
 */
public class SignUpActivity extends FragmentActivity implements View.OnClickListener {
    private MySqliteOpenHelper mDbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    //fragment에서 입력한 회원정보 저장
    private TextView backLogin;
    private Button requestButton;
    private User userForSignUp;
    private ProgressDialog progressDialog;

    private RequestQueue queue;

    //TODO 가입완료후 자동로그인시 BACK BUTTON 눌러도 회원가입으로 돌아오는 문제 해결(taskAffinity)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;
        mDbHelper = new MySqliteOpenHelper(mContext);

        queue = Volley.newRequestQueue(this);

        //회원정보 입력 fragment 연결
        //로그인 Activity 돌아가기 이벤트 등록
        backLogin = (TextView) findViewById(R.id.back_login_page);
        requestButton = (Button) findViewById(R.id.sign_up_request_btn);
        backLogin.setOnClickListener(this);
        requestButton.setOnClickListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = new Fragment_UserInfoForSignUp();
        transaction.add(R.id.fragment_container_signup, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back_login_page:
                //TODO 로그인 Activity로 돌아가기
                onBackPressed();
                break;
            case R.id.sign_up_request_btn:

                //progressBar 표시
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("회원가입 중");
                progressDialog.show();

                /*
                    서버에 회원가입 요청
                 */
                String url = "http://10.0.3.2:8080/user";
                StringRequest myReq = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            /*
                                회원가입 성공
                             */
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("signuptest", "success");
                                if (response.equals("success")) {
                                    progressDialog.dismiss();   //progressDialog dismiss
                                    deleteUserFromDb();
                                    saveUserIntoDb(userForSignUp);
                                    Intent intent = new Intent();
                                    intent.setAction("com.nhnnext.android.action.MATCH");
                                    UserParcelable parcelUser = new UserParcelable(userForSignUp);
                                    intent.putExtra("user", parcelUser);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                    /*
                        회원가입 실패
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // 가입 실패 Toast로 표시
                        Toast.makeText(getApplicationContext(), "가입 실패!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userEmail", userForSignUp.getUserEmail());
                        params.put("userName", userForSignUp.getUserName());
                        params.put("userPassword", userForSignUp.getUserPassword());
                        params.put("userGender", userForSignUp.getUserGender());
                        params.put("userAge", "" + userForSignUp.getUserAge());
                        params.put("oAuth", "");
                        return params;
                    }
                };
                queue.add(myReq);
                break;
        }
    }

    protected void enableSignUp(User userForSignUp) {
        requestButton.setVisibility(View.VISIBLE);
        this.userForSignUp = userForSignUp;
    }

    private void saveUserIntoDb(User user) {
        // Get the data repository in write mode
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MySqliteOpenHelper.KEY_EMAIL, user.getUserEmail());
        values.put(MySqliteOpenHelper.KEY_NAME, user.getUserName());
        values.put(MySqliteOpenHelper.KEY_PASSWORD, user.getUserPassword());
        values.put(MySqliteOpenHelper.KEY_AGE, user.getUserAge());
        values.put(MySqliteOpenHelper.KEY_GENDER, user.getUserGender());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MySqliteOpenHelper.USER_TABLE_NAME, null, values);
        db.close();
    }

    private boolean deleteUserFromDb() {
        boolean result = false;
        db = mDbHelper.getWritableDatabase();
        if (db.delete(MySqliteOpenHelper.USER_TABLE_NAME, null, null) > 0)
            result = true;
        db.close();
        return result;
    }
}

