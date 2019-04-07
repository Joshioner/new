package com.blk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.blk.common.TimePickerView;
import com.blk.common.ToolBarSet;
import com.blk.common.alterdialog.ActionSheetDialog;
import com.blk.common.entity.User;
import com.blk.common.identify.XCRoundImageView;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.AndroidUploadFile;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.FileUtil;

import com.blk.R;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.google.gson.JsonObject;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton maleRadio,femaleRadio;  //男、女单选框
    private EditText birthday;
    private XCRoundImageView uploadPhoto;   //上传头像
    private static final int REQUEST_CODE_PICK_IMAGE = 101;    //选择图片的请求码
    private static final int REQUEST_CODE_CAMERA = 102;        //拍照的请求码
    private static final int CROP_PHOTO = 103;  //裁剪的请求码
    private Uri imageUri;  //图片路径
    private String imagePath;
    private String fileName = null;
    private EditText accountName,password,repeatPassword,email,phone;
    private Button register;  //注册
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //防止软键盘遮挡
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.register);

        //初始化控件
        initView();
        //初始化点击事件
        initEvent();
        //初始化单选按钮控件
        initRadio();
    }

    private void initRadio() {
        RadioButton male = (RadioButton) findViewById(R.id.male);
        RadioButton female = (RadioButton) findViewById(R.id.female);

        //定义底部标签图片大小和位置
        Drawable male_image = getResources().getDrawable(R.drawable.register_sex_male);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        male_image.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        male.setCompoundDrawables(male_image, null, null, null);

        //定义底部标签图片大小和位置
        Drawable female_image = getResources().getDrawable(R.drawable.register_sex_female);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        female_image.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        female.setCompoundDrawables(female_image, null, null, null);
    }

    /**
     * 初始化控件
     */
    public void initView(){
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
        birthday = (EditText) findViewById(R.id.birthday_text_hint);
        uploadPhoto = (XCRoundImageView) findViewById(R.id.upload_photo);
        accountName = (EditText) findViewById(R.id.account_hint);
        password = (EditText) findViewById(R.id.password_hint);
        repeatPassword = (EditText) findViewById(R.id.repeat_password_hint);
        email = (EditText) findViewById(R.id.email_hint);
        phone = (EditText) findViewById(R.id.phone_hint);
        register = (Button) findViewById(R.id.register_btn);
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
    }

    /**
     * 初始化点击事件
     */
    public void initEvent(){
        //出生日期
       birthday.setOnClickListener(this);
       //上传头像
        uploadPhoto.setOnClickListener(this);
        //注册按钮
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            //出生日期
            case R.id.birthday_text_hint:
                TimePickerView pvTime = new TimePickerView.Builder(RegisterActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        String time = getTime(date);
                        birthday.setFocusable(true);
                        birthday.setText(time);
                        birthday.setFocusable(false);
                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
             //上传头像
            case R.id.upload_photo:
                new ActionSheetDialog(RegisterActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setTitle("提示")
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                File outputImage = new File(Environment.getExternalStorageDirectory(),
                                        "tempImage" + ".jpg");
                                try{
                                    if(outputImage.exists()){
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }

                                imageUri = Uri.fromFile(outputImage);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                            }
                        })
                        .addSheetItem("选择相册", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    int ret = ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission
                                            .READ_EXTERNAL_STORAGE);
                                    if (ret != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                                1000);
                                        return;
                                    }
                                }
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                            }
                        }).show();
                break;
            case R.id.register_btn:
                registerUser();
                break;
             default:
                 break;
        }
    }

    //注册用户
    private void registerUser(){
        User user = new User();
        //头像路径
        user.setProfile(fileName);
        //邮箱
        if (TextUtils.isEmpty(email.getText())){
            AlterUtil.alterTextLong(RegisterActivity.this,"邮箱不能为空");
            return;
        }
        user.setEmail(email.getText().toString());
        //密码
        if (TextUtils.isEmpty(password.getText())){
            AlterUtil.alterTextLong(RegisterActivity.this,"密码不能为空");
            return;
        }
        //确认密码
        if (TextUtils.isEmpty(repeatPassword.getText())){
            AlterUtil.alterTextLong(RegisterActivity.this,"确认密码不能为空");
            return;
        }
        if (!password.getText().toString().equals(repeatPassword.getText().toString())){
            AlterUtil.alterTextLong(RegisterActivity.this,"两次输入的密码不一致");
            return;
        }
        user.setPassword(password.getText().toString());
        //账号名
        if (TextUtils.isEmpty(accountName.getText())){
            AlterUtil.alterTextLong(RegisterActivity.this,"昵称不能为空");
            return;
        }
        user.setAccountName(accountName.getText().toString());
        //性别
        user.setGender(maleRadio.isChecked() ? 1 : 0);
        //出生日期
        user.setBirthday(birthday.getText().toString());
        //手机号码
        user.setPhone(phone.getText().toString());
        String address = ConfigUtil.getServerAddress() + "/user/register";
        //转user对象转为json字符串
        HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(user),new HttpCallbackListener(){
            @Override
            public void onFinish(String response) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    RegisterActivity.this.finish();
                    startActivity(intent);
                    Looper.prepare();
                    AlterUtil.alterTextLong(RegisterActivity.this,"注册成功");
                    Looper.loop();
                }else {
                    Looper.prepare();
                    AlterUtil.alterTextLong(RegisterActivity.this,"注册失败");
                    Looper.loop();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择图片，sd卡上
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String personPhotoPath  = getRealPathFromURI(uri);
           try{
               Bitmap bitmap = BitmapFactory.decodeFile(personPhotoPath);
               imagePath = FileUtil.getPersonPhoto(getApplicationContext()).getAbsolutePath();
               File personPhoto = new File(imagePath);
               BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(personPhoto));
               bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
               bos.flush();
               bos.close();
               //上传头像到服务器
               uploadPersonPhoto(imagePath);
               //填充用户头像
               uploadPhoto.setImageBitmap(bitmap);
           }catch (Exception e){
               e.printStackTrace();
           }
        }
        //拍照
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
        }  else if (requestCode == CROP_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageUri));
                imagePath = FileUtil.getPersonPhoto(getApplicationContext()).getAbsolutePath();
                File personPhoto = new File(imagePath);
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(personPhoto));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                bos.flush();
                bos.close();
                //上传头像到服务器
                uploadPersonPhoto(imagePath);
                uploadPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取拍照的真实路径
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * 根据图片路径，设置用户头像
     * @param imagePath
     */
    private void setPersonPhoto(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        uploadPhoto.setImageBitmap(bitmap);
    }

    public Integer uploadPersonPhoto(String imagePath){

        if (fileName == null){
            fileName =  new Date().getTime()  + imagePath.substring(imagePath.lastIndexOf("."));
        }
        String address = ConfigUtil.getServerAddress() + "/uploadImage/1" ;
        try {
            AndroidUploadFile.uploadFile(imagePath, address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        Looper.prepare();
                        AlterUtil.alterTextLong(RegisterActivity.this,"头像上传成功");
                        Looper.loop();
                    }else if (code ==1){
                        Looper.prepare();
                        AlterUtil.alterTextLong(RegisterActivity.this,"头像为空");
                        Looper.loop();
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(RegisterActivity.this,"头像上传失败");
                        Looper.loop();
                    }
                }

                @Override
                public void onError(Exception e) {

                   return;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return 0;
    }
}
