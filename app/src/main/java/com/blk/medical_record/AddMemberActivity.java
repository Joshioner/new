package com.blk.medical_record;

import android.Manifest;
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
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.blk.R;
import com.blk.common.TimePickerView;
import com.blk.common.ToolBarSet;
import com.blk.common.alterdialog.ActionSheetDialog;
import com.blk.common.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton maleRadio,femaleRadio;  //男、女单选框
    private EditText birthday;
    private ImageView uploadPhoto;   //上传头像
    private static final int REQUEST_CODE_PICK_IMAGE = 101;    //选择图片的请求码
    private static final int REQUEST_CODE_CAMERA = 102;        //拍照的请求码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.add_member);

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
        uploadPhoto = (ImageView) findViewById(R.id.upload_photo);

    }

    /**
     * 初始化点击事件
     */
    public void initEvent(){
        //出生日期
        birthday.setOnClickListener(this);
        //上传头像
        uploadPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            //出生日期
            case R.id.birthday_text_hint:
                TimePickerView pvTime = new TimePickerView.Builder(AddMemberActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        String time = getTime(date);
                        Toast.makeText(AddMemberActivity.this,time,Toast.LENGTH_LONG).show();
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
                new ActionSheetDialog(AddMemberActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setTitle("提示")
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(AddMemberActivity.this, CameraActivity.class);
                                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                        FileUtil.getMemberPhoto(getApplication()).getAbsolutePath());
                                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                                        CameraActivity.CONTENT_TYPE_GENERAL);
                                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                            }
                        })
                        .addSheetItem("选择相册", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    int ret = ActivityCompat.checkSelfPermission(AddMemberActivity.this, Manifest.permission
                                            .READ_EXTERNAL_STORAGE);
                                    if (ret != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(AddMemberActivity.this,
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
            default:
                break;
        }
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
            String imagePath = getRealPathFromURI(uri);
            //填充用户头像
            setPersonPhoto(imagePath);
        }

        //拍照
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            //图片路径
            String  imagePath = FileUtil.getMemberPhoto(getApplicationContext()).getAbsolutePath();
            //填充用户头像
            setPersonPhoto(imagePath);
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
}

