package com.nhnnext.android.languageexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nhnnext.android.languageexchange.user.User;
import com.nhnnext.android.languageexchange.user.UserParcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alpha on 2015. 8. 10..
 */
public class OauthFragment extends Fragment {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

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
                Log.d("oauth", "success");

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
                                Log.d("oauth", "response : " + response.toString());
                                JSONObject jsonObject = response.getJSONObject();

                                //if) 등록되지 않은 사용자라면 서버 DB로 저장하며 가입시킨다.
                                User user = new User();
                                try {
                                    //TODO oauth user id 필요한지 검토
                                    jsonObject.getInt("id");
                                } catch (JSONException e) {
                                    Log.d("oauth", "bring ID from jsonObject error");
                                }

                                try {
                                    //User 정보 저장
                                    //facebook 로그인의 경우 비밀번호가 필요없음
                                    user.setEmail(jsonObject.getString("email"));
                                    user.setName(jsonObject.getString("name"));
                                    user.setGender(jsonObject.getString("gender"));
                                    String[] birthday = jsonObject.getString("birthday").split("/");
                                    int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday[2]) + 1;
                                    user.setAge(age);
                                } catch (JSONException e) {
                                    Log.d("oauth", "error : bring jsonObject from facebook");
                                }
                                user.setOauth("facebook");

                                Log.d("oauth", "user : " + user);

                                Intent intent = new Intent();
                                intent.setAction("com.nhnnext.android.action.MATCH");
                                UserParcelable parcelUser = new UserParcelable(user);
                                intent.putExtra("user", parcelUser);
                                startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
