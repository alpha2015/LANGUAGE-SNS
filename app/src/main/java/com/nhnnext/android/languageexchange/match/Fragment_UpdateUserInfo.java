package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.MySingleton;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;
import com.nhnnext.android.languageexchange.common.UrlFactory;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class Fragment_UpdateUserInfo : 회원정보 수정 fragment
 */
public class Fragment_UpdateUserInfo extends Fragment implements View.OnClickListener {
    //TODO 사진찍기 Activity 호출, 프로필 사진으로 저장 구현(서버DB 저장)
    //TODO 갤러리 접근, 프로필 사진으로 저장 구현(서버DB 저장)
    private MySqliteOpenHelper mDbHelper;
    private SQLiteDatabase db;
    private Context mContext;
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;

    private LinearLayout passwordLayout;
    private TextView viewEmail;
    private EditText editName;
    private EditText editPassword;
    private static TextView editAge;
    private static TextView editGender;
    private Button saveButton;
    private TextView editIntro;
    private ImageView imageCancelView;
    private ImageButton logoutButton;
    private Bitmap profileBitmap;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private UserParcelable user;
    private Uri fileUri;

    /**
     * Method newInstance(UserParcelable user)
     * caller activity로부터 전달 받은 user 값 bundle로 저장
     *
     * @param user 수정할 회원정보
     * @return Fragment_UpdateUserInfo instance
     */
    public static Fragment_UpdateUserInfo newInstance(UserParcelable user) {
        Fragment_UpdateUserInfo f = new Fragment_UpdateUserInfo();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        f.setArguments(args);

        return f;
    }

    /**
     * Method getShownIndex()
     * user data getter
     *
     * @return activity로부터 전달 받은 data 값
     */
    private UserParcelable getShownIndex() {
        return getArguments().getParcelable("user");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO DB에서 가져온 회원정보 보여주기

        mContext = getActivity();
        mDbHelper = new MySqliteOpenHelper(mContext);
        user = getShownIndex();

        //레이아웃 view
        View view = inflater.inflate(R.layout.fragment_update_userinfo, container, false);
        mNetworkImageView = (NetworkImageView) view.findViewById(R.id.imageView01);
        viewEmail = (TextView) view.findViewById(R.id.setting_email);
        editName = (EditText) view.findViewById(R.id.setting_edit_name);
        editPassword = (EditText) view.findViewById(R.id.setting_edit_password);
        editAge = (TextView) view.findViewById(R.id.setting_edit_age);
        editGender = (TextView) view.findViewById(R.id.setting_edit_gender);
        saveButton = (Button) view.findViewById(R.id.setting_save);
        editIntro = (TextView) view.findViewById(R.id.setting_edit_intro);
        imageCancelView = (ImageView) view.findViewById(R.id.imageView02);
        logoutButton = (ImageButton) view.findViewById(R.id.logout_btn);
        passwordLayout = (LinearLayout) view.findViewById(R.id.update_password_layout);

        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();

        mNetworkImageView.setImageUrl(user.getImage(), mImageLoader);

        Log.d("testtt9", user.getImage());

        if (user.getOauth() != null && user.getOauth().equals("facebook"))
            passwordLayout.setVisibility(View.GONE);

        //각 회원정보에 대한 클릭, 저장 이벤트 등록
        editAge.setOnClickListener(this);
        editGender.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        mNetworkImageView.setOnClickListener(this);
        imageCancelView.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        //DB에서 가져온 user data view에 설정
        viewEmail.setText(user.getEmail());
        editName.setText(user.getName());
        editPassword.setText(user.getPassword());
        editAge.setText(Integer.toString(user.getAge()));
        editGender.setText(user.getGenderForKorean());
        editIntro.setText(user.getIntro());
        return view;
    }

    /**
     * Method onClick(View v)
     *
     * @param v : clicked view
     *          프로필 이미지 클릭시) 카메라 Activity 호출
     *          로그아웃 버튼 클릭시) app db user data 삭제, activity 종료
     *          프로필 이미지 삭제 클릭시) 저장된 프로필 이미지 제거
     *          성별 view 클릭시) gender dialog fragment 호출
     *          나이 view 클릭시) age dialog fragment 호출
     *          저장하기 버튼 클릭시) 수정된 user 정보 server db, app db에 update
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imageView01:
                if (checkCameraHardware(getActivity().getApplicationContext())) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "카메라에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.logout_btn:
                //TODO DB지우기 , oauth면 oauth logout
                deleteUserFromDb();
                LoginManager.getInstance().logOut();

                getActivity().finish();

                break;
            case R.id.imageView02:
                mNetworkImageView.setImageResource(R.drawable.square_profile_default);
                profileBitmap = null;
                break;
            case R.id.setting_edit_age:
                AgePickerDialog.newInstance(user.getAge()).show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_edit_gender:
                GenderRadioDialog.newInstance(user.getGender()).show(getFragmentManager(), "dialog");
                break;
            case R.id.setting_save:
                //TODO App, SERVER DB 저장 구현(Bitmap profileBitmap, user info)
                if (profileBitmap == null)
                    profileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square_profile_default);
                user.setName(editName.getText().toString());
                if (user.getOauth() == null)
                    user.setPassword(editPassword.getText().toString());
                user.setAge(Integer.parseInt(editAge.getText().toString()));
                if (editGender.getText().equals("남성"))
                    user.setGender("male");
                else
                    user.setGender("female");
                user.setIntro(editIntro.getText().toString());

                RequestParams params = new RequestParams();
                ByteArrayOutputStream bos;
                bos = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapData = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);
                params.put("userImage", bs, user.getEmail() + ".png");
                params.put("userEmail", user.getEmail());
                params.put("userName", user.getName());
                params.put("userPassword", user.getPassword());
                params.put("userGender", user.getGender());
                params.put("userAge", "" + user.getAge());
                if (!user.getIntro().equals("")) params.put("userIntro", user.getIntro());
                params.put("oAuth", user.getOauth());

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(UrlFactory.UPDATE_USER, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            user.setImage(new String(responseBody, "UTF-8"));
                            saveUserIntoDb(new User(user));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("UPDATE", "Http Post Fail");
                    }
                });
                break;
        }
    }

    //TODO Fragment 스터디후 반드시 수정(리팩토링)

    /**
     * Class AgePickerDialog
     * 나이 수정을 위한 DialogFragment
     */
    public static class AgePickerDialog extends DialogFragment {
        private NumberPicker npView;

        /**
         * Method newInstance(int age)
         * caller activity로부터 전달 받은 age 값 bundle로 저장
         *
         * @param age
         * @return AgePickerDialog instance
         */
        public static AgePickerDialog newInstance(int age) {
            AgePickerDialog f = new AgePickerDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", age);
            f.setArguments(args);

            return f;
        }

        /**
         * Method getShownIndex()
         * age값 getter
         *
         * @return activity로부터 전달 받은 age 값
         */
        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

        /**
         * Method onCreateDialog(Bundle savedInstanceState)
         * 나이 설정 다이얼로그 생성
         *
         * @param savedInstanceState
         * @return Age 설정 dialog
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            npView = (NumberPicker) inflater.inflate(R.layout.age_picker_dialog_layout, null);
            npView.setWrapSelectorWheel(false);
            npView.setMinValue(0);
            npView.setMaxValue(99);
            npView.setValue(getShownIndex());
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("나이를 설정하세요.");
            builder.setView(npView)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editAge.setText(Integer.toString(npView.getValue()));
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // DO NOTHING
                        }
                    });
            return builder.create();
        }
    }

    //TODO Fragment 스터디후 반드시 수정(리팩토링)

    /**
     * Class GenderRadioDialog
     * 성별 수정을 위한 DialogFragment
     */
    public static class GenderRadioDialog extends DialogFragment {
        private RadioGroup npView;

        /**
         * Method newInstance(String gender)
         * caller activity로부터 전달 받은 gender 값 bundle로 저장
         *
         * @param gender
         * @return GenderRadioDialog instance
         */
        public static GenderRadioDialog newInstance(String gender) {
            GenderRadioDialog f = new GenderRadioDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putString("index", gender);
            f.setArguments(args);

            return f;
        }

        /**
         * Method getShownIndex()
         * gender값 getter
         *
         * @return activity로부터 전달 받은 gender 값
         */
        public String getShownIndex() {
            return getArguments().getString("index", "null");
        }

        /**
         * Method onCreateDialog(Bundle savedInstanceState)
         * 성별 설정 다이얼로그 생성
         *
         * @param savedInstanceState
         * @return Gender 설정 dialog
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            npView = (RadioGroup) inflater.inflate(R.layout.gender_radio_dialog_layout, null);
            if (getShownIndex().equals("male")) {
                RadioButton maleRadio = (RadioButton) npView.findViewById(R.id.gender_male);
                maleRadio.setChecked(true);
            } else {
                RadioButton femaleRadio = (RadioButton) npView.findViewById(R.id.gender_female);
                femaleRadio.setChecked(true);
            }

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("성별을 설정하세요.");
            builder.setView(npView)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (npView.getCheckedRadioButtonId() == R.id.gender_male) {
                                editGender.setText("남성");
                            } else {
                                editGender.setText("여성");
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // DO NOTHING
                        }
                    });
            return builder.create();
        }
    }

    /**
     * Method onActivityResult(int requestCode, int resultCode, Intent data)
     * 카메라 activity 호출 결과에 따른 전달받은 image 표시
     *
     * @param requestCode 요청 코드
     * @param resultCode  응답 코드
     * @param data        data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && !data.equals(null)) {
                try {
                    profileBitmap = (Bitmap) data.getExtras().get("data");
                    mNetworkImageView.setImageBitmap(profileBitmap);
                    mNetworkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Method checkCameraHardware(Context context)
     * Check if this device has a camera
     *
     * @param context activity context
     * @return camera 존재 유무
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Method deleteUserFromDb()
     * db에서 user 정보 삭제
     *
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

    private void saveUserIntoDb(User user) {
        // Get the data repository in write mode
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MySqliteOpenHelper.KEY_IMAGE, user.getUserImage());
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

    private User readUserFromDb() {
        // Get the data repository in read mode
        db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "userImage", "userEmail", "userName", "userPassword", "userAge", "userGender", "oAuth"
        };
        // Table, Column, WHERE, ARGUMENTS, GROUPING, HAVING, SORTING
        Cursor cursor = db.query(MySqliteOpenHelper.USER_TABLE_NAME, projection, null, null, null, null, null);

        User user = null;
        while (cursor.moveToNext()) {
            user = new User();

            if (cursor.getString(0) != null)
                user.setUserImage(cursor.getString(0));
            if (cursor.getString(1) != null)
                user.setUserEmail(cursor.getString(1));
            if (cursor.getString(2) != null)
                user.setUserName(cursor.getString(2));
            if (cursor.getString(3) != null)
                user.setUserPassword(cursor.getString(3));
            if (cursor.getString(4) != null)
                user.setUserAge(cursor.getInt(4));
            if (cursor.getString(5) != null)
                user.setUserGender(cursor.getString(5));
            if (cursor.getString(6) != null)
                user.setoAuth(cursor.getString(6));
        }
        cursor.close();
        db.close();
        return user;
    }
}


