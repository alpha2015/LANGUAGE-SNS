package com.nhnnext.android.languageexchange;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alpha on 2015. 8. 10..
 * Class OauthFragment : oauth login을 위한 facebook api fragment
 */
public class OauthFragment extends Fragment {
    private MySqliteOpenHelper mDbHelper;
    private SQLiteDatabase db;
    private Context mContext;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private User user;
    private RequestQueue queue;
    private GsonRequest<User> loginRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        queue = Volley.newRequestQueue(getActivity());
        mContext = getActivity();
        mDbHelper = new MySqliteOpenHelper(mContext);
    }

    /**
     * Method onCreateView
     * facebook login button 등록
     * login 성공후 서버 db user 조회
     * user 존재하지 않을 경우) user 정보 server db에 create, app db에 저장, MatchingActivity 호출
     * user 존재할 경우) user 정보 server db에 재갱신, app db에 저장, MatchingActivity 호출
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);
        loginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Bundle fbParams = new Bundle();
                fbParams.putString("fields", "id, email, name, birthday, gender");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me",
                        fbParams,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */
                                JSONObject jsonObject = response.getJSONObject();

                                //if) 등록되지 않은 사용자라면 서버 DB로 저장하며 가입시킨다.
                                user = new User();
                                try {
                                    //TODO oauth user id 필요한지 검토
                                    jsonObject.getInt("id");
                                } catch (JSONException e) {
                                    Log.d("oauth", "bring ID from jsonObject error");
                                }

                                try {
                                    //User 정보 저장
                                    //facebook 로그인의 경우 비밀번호가 필요없음
                                    user.setUserEmail(jsonObject.getString("email"));
                                    user.setUserName(jsonObject.getString("name"));
                                    user.setUserGender(jsonObject.getString("gender"));
                                    String[] birthday = jsonObject.getString("birthday").split("/");
                                    int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday[2]) + 1;
                                    user.setUserAge(age);
                                    user.setoAuth("facebook");

                                    /*
                                        서버에 로그인 요청
                                     */
                                    String loginUrl = "http://10.0.3.2:8080/user/login";
                                    loginRequest = new GsonRequest<>(loginUrl, User.class, null,
                                            new Response.Listener<User>() {
                                                @Override
                                                public void onResponse(User user) {
                                                    deleteUserFromDb();
                                                    saveUserIntoDb(user);
                                                    Log.d("loginuser1", "" + user);
                                                    Intent intent = new Intent();
                                                    intent.setAction("com.nhnnext.android.action.MATCH");
                                                    UserParcelable parcelUser = new UserParcelable(user);
                                                    intent.putExtra("user", parcelUser);
                                                    startActivity(intent);
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
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
                                                            if (response.equals("success")) {
//                                                        progressDialog.dismiss();   //progressDialog dismiss
                                                                deleteUserFromDb();
                                                                Log.d("loginuser2", "" + user);
                                                                saveUserIntoDb(user);
                                                                Intent intent = new Intent();
                                                                intent.setAction("com.nhnnext.android.action.MATCH");
                                                                UserParcelable parcelUser = new UserParcelable(user);
                                                                intent.putExtra("user", parcelUser);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                /*
                                                    회원가입 실패
                                                 */
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
//                                            progressDialog.dismiss();
                                                    // 가입 실패 Toast로 표시
                                                    Toast.makeText(getActivity().getApplicationContext(), "가입 실패!", Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("userEmail", user.getUserEmail());
                                                    params.put("userName", user.getUserName());
                                                    params.put("userPassword", "");
                                                    params.put("userGender", user.getUserGender());
                                                    params.put("userAge", "" + user.getUserAge());
                                                    params.put("oAuth", user.getOAuth());
                                                    return params;
                                                }
                                            };
                                            queue.add(myReq);
                                        }
                                    });

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userEmail", user.getUserEmail());
                                    params.put("userPassword", user.getUserPassword());
                                    params.put("oAuth", user.getOAuth());
                                    loginRequest.setParams(params);
                                    queue.add(loginRequest);

                                } catch (JSONException e) {
                                    Log.d("oauth", "error : bring jsonObject from facebook");
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("oauth", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("oauth", "error");
            }
        });
        return view;
    }

    /**
     * Method onActivityResult(int requestCode, int resultCode, Intent data)
     * facebook login activity 호출 결과 등록을 위해 재정의
     * @param requestCode 요청 코드
     * @param resultCode 응답 코드
     * @param data data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Method saveUserIntoDb(User user)
     * db에 user 정보 저장
     * @param user
     */
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
        values.put(MySqliteOpenHelper.KEY_OAUTH, user.getOAuth());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MySqliteOpenHelper.USER_TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Method deleteUserFromDb()
     * db에서 user 정보 삭제
     * @return delete 성공 유무
     */
    private boolean deleteUserFromDb() {
        boolean result = false;
        db = mDbHelper.getWritableDatabase();
        if (db.delete(MySqliteOpenHelper.USER_TABLE_NAME, null, null) > 0)
            result = true;
        db.close();
        return result;
    }
}
