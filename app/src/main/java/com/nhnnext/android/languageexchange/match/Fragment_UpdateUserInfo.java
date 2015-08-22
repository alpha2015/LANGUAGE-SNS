package com.nhnnext.android.languageexchange.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.MySqliteOpenHelper;
import com.nhnnext.android.languageexchange.common.NetworkUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alpha on 2015. 7. 22..
 */
//TODO 질문사항 1)
//TODO 코드리뷰시 질문하기) Fragment 내부에 dialog를 위한 fragment 구현이 올바른 설계인지(static으로 인한 문제)
//TODO 잘못된것이라면 어떤식으로 설계를 바꾸는것이 좋은지?
public class Fragment_UpdateUserInfo extends Fragment implements View.OnClickListener {
    //TODO 사진찍기 Activity 호출, 프로필 사진으로 저장 구현(서버DB 저장)
    //TODO 갤러리 접근, 프로필 사진으로 저장 구현(서버DB 저장)
    private MySqliteOpenHelper mDbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private TextView viewEmail;
    private EditText editName;
    private EditText editPassword;
    private static TextView editAge;
    private static TextView editGender;
    private Button saveButton;
    private TextView editIntro;
    private ImageView imageView;
    private ImageView imageCancelView;
    private ImageButton logoutButton;
    private Bitmap profileBitmap;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    //test를 위한 dummy user data(db구현시 제거)
//    private User user = new User(null, "test@naver.com", "김아무개", "1234", 30, "male", null, null, null);
    private UserParcelable user;

    public static Fragment_UpdateUserInfo newInstance(UserParcelable user) {
        Fragment_UpdateUserInfo f = new Fragment_UpdateUserInfo();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        f.setArguments(args);

        return f;
    }

    private UserParcelable getShownIndex() {
        return getArguments().getParcelable("user");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO DB에서 가져온 회원정보 보여주기
        user = getShownIndex();

        //레이아웃 view
        View view = inflater.inflate(R.layout.fragment_update_userinfo, container, false);
        viewEmail = (TextView) view.findViewById(R.id.setting_email);
        editName = (EditText) view.findViewById(R.id.setting_edit_name);
        editPassword = (EditText) view.findViewById(R.id.setting_edit_password);
        editAge = (TextView) view.findViewById(R.id.setting_edit_age);
        editGender = (TextView) view.findViewById(R.id.setting_edit_gender);
        saveButton = (Button) view.findViewById(R.id.setting_save);
        editIntro = (TextView) view.findViewById(R.id.setting_edit_intro);
        imageView = (ImageView) view.findViewById(R.id.imageView01);
        imageCancelView = (ImageView) view.findViewById(R.id.imageView02);
        logoutButton = (ImageButton) view.findViewById(R.id.logout_btn);

        //각 회원정보에 대한 클릭, 저장 이벤트 등록
        editAge.setOnClickListener(this);
        editGender.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageCancelView.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        //DB에서 가져온 user data view에 설정
        viewEmail.setText(user.getEmail());
        editName.setText(user.getName());
        editPassword.setText("********");
        editAge.setText(Integer.toString(user.getAge()));
        editGender.setText(user.getGenderForKorean());
        editIntro.setText(user.getIntro());
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imageView01:
                if (checkCameraHardware(getActivity().getApplicationContext())) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

//                    Toast.makeText(getActivity().getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//
//                    // start the image capture Intent
//                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "카메라에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.logout_btn:
                //TODO DB지우기 , oauth면 oauth logout
                mContext = getActivity();
                mDbHelper = new MySqliteOpenHelper(mContext);

                deleteUserFromDb();

                getActivity().finish();

                break;
            case R.id.imageView02:
                imageView.setImageResource(R.drawable.square_profile_default);
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
                user.setName(editName.getText().toString());
                user.setPassword(editPassword.getText().toString());
                user.setAge(Integer.parseInt(editAge.getText().toString()));
                if (editGender.getText().equals("남성"))
                    user.setGender("male");
                else
                    user.setGender("female");
                user.setIntro(editIntro.getText().toString());


                /*
                    서버에 회원정보 수정 요청
                 */
                new Thread(new Runnable() {
                    public void run() {
                        URL url = null;       // URL 설정
                        String result = null;
                        try {
                            url = new URL("http://10.0.3.2:8080/user/update");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();
                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);  // 읽기 모드 지정
                            http.setDoOutput(true); // 쓰기 모드 지정
                            http.setRequestMethod("POST");  // method POST
                            //Form tag 방식으로 처리
                            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                            List<Pair<String, String>> params = new ArrayList<>();
                            params.add(new Pair<>("userEmail", user.getEmail()));
                            params.add(new Pair<>("userName", user.getName()));
                            params.add(new Pair<>("userPassword", user.getPassword()));
                            params.add(new Pair<>("userGender", user.getGender()));
                            params.add(new Pair<>("userAge", "" + user.getAge()));
                            params.add(new Pair<>("userIntro", user.getIntro()));

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(NetworkUtil.getQuery(params));
                            writer.flush();
                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                                builder.append(str + "\n");
                            }
                            result = builder.toString();

                            /*
                               업데이트 실패
                             */
                            //TODO 실패사유 Toast로 표시

                            /*
                                업데이트 성공
                             */
                            //TODO 추후 자동로그인을 위해 App DB에 회원정보 UPDATE
                            if (result.equals("success\n")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "저장 완료!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;
        }
    }

    //TODO Fragment 스터디후 반드시 수정(리팩토링)
    public static class AgePickerDialog extends DialogFragment {
        private NumberPicker npView;

        public static AgePickerDialog newInstance(int age) {
            AgePickerDialog f = new AgePickerDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", age);
            f.setArguments(args);

            return f;
        }

        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

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
    public static class GenderRadioDialog extends DialogFragment {
        private RadioGroup npView;

        public static GenderRadioDialog newInstance(String gender) {
            GenderRadioDialog f = new GenderRadioDialog();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putString("index", gender);
            f.setArguments(args);

            return f;
        }

        public String getShownIndex() {
            return getArguments().getString("index", "null");
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && !data.equals(null)) {
                try {
                    profileBitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(profileBitmap);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch (Exception e) {
                    return;
                }
            }
        }

//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                // Image captured and saved to fileUri specified in the Intent
////                Toast.makeText(getActivity().getApplicationContext(), "Image saved to:\n" +
////                        data.getData(), Toast.LENGTH_LONG).show();
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // User cancelled the image capture
//            } else {
//                // Image capture failed, advise user
//            }
//        }
//
//        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                // Video captured and saved to fileUri specified in the Intent
//                Toast.makeText(getActivity().getApplicationContext(), "Video saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // User cancelled the video capture
//            } else {
//                // Video capture failed, advise user
//            }
//        }
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Check if this device has a camera
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

    private boolean deleteUserFromDb() {
        boolean result = false;
        db = mDbHelper.getWritableDatabase();
        if (db.delete(MySqliteOpenHelper.USER_TABLE_NAME, null, null) > 0)
            result = true;
        db.close();
        return result;
    }
}


