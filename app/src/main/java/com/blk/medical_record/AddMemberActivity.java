package com.blk.medical_record;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.blk.LoginActivity;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.TimePickerView;
import com.blk.common.ToolBarSet;
import com.blk.common.alterdialog.ActionSheetDialog;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.HealthRecord;
import com.blk.common.entity.User;
import com.blk.common.identify.XCRoundImageView;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.AndroidUploadFile;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.FileUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.health_tool.HealthRecordDetailActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton maleRadio,femaleRadio;  //男、女单选框
    private EditText birthday,memberName,relation;
    private ImageView uploadPhoto;   //上传头像
    private static final int REQUEST_CODE_PICK_IMAGE = 101;    //选择图片的请求码
    private static final int REQUEST_CODE_CAMERA = 102;        //拍照的请求码
    private static final int CROP_PHOTO = 103;  //裁剪的请求码
    private Uri imageUri;  //图片路径
    private String imagePath;
    private String fileName = null;
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息
    private Button addBtn;  //保存按钮
    private Dialog weiboDialogUtils;                         //加载框
    private int fid = 0;
    private FamilyMember memberDetail;    //家庭成员信息
    private TextView member_id;
    private final int Init_UI = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_UI:
                    FamilyMember memberDetail = (FamilyMember) msg.obj;
                    //设置家庭成员编号
                    member_id.setText(String.valueOf(memberDetail.getFid()));
                    //头像
                    String fileNamePath = memberDetail.getProfile();
                    if(fileNamePath != null){
                        fileName = fileNamePath.substring(fileNamePath.lastIndexOf("\\") + 1);
                        File file = FileUtil.getMemberPhoto(AddMemberActivity.this,fileName);
                        //异步给图片初始化
                        uploadPhoto.setImageBitmap(BitmapFactoryUtil.getBitmap(file.getAbsolutePath()));
                    }
                    //姓名
                    memberName.setText(memberDetail.getName());
                    //关系
                    if (memberDetail.getRelation() != null && memberDetail.getRelation() != ""){
                        String[] str = memberDetail.getRelation().split(":");
                        relation.setText(str[1].trim());
                    }

                    //性别
                    int gender = memberDetail.getGender();
                    if (gender == 1){
                        maleRadio.setChecked(true);
                    }else {
                        femaleRadio.setChecked(true);
                    }
                    //出生日期
                    birthday.setText(memberDetail.getBirthday());
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.add_member);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo,User.class);

        //初始化控件
        initView();
        //初始化单选按钮控件
        initRadio();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            fid = bundle.getInt("fid");
        }
        //执行更新操作
        if (fid > 0 ){
            new MemberThread().execute();
        }
    }

    class MemberThread extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(AddMemberActivity.this,"加载中...");
        }

        @Override
        protected Void doInBackground(Void... integers) {
            String url = ConfigUtil.getServerAddress() + "/member/findById/" + fid ;
            HttpRequestUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        memberDetail = JSONObject.parseObject(data,FamilyMember.class);
                        Message message = Message.obtain();
                        message.what = Init_UI;
                        message.obj = memberDetail;
                        //执行更新UI操作
                        handler.sendMessage(message);
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(AddMemberActivity.this,"加载成员信息失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextShort(AddMemberActivity.this,"加载成员信息失败");
                    Looper.loop();
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
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
        //编号
        member_id = (TextView) findViewById(R.id.family_member_id);
        //性别
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
        //出生日期
        birthday = (EditText) findViewById(R.id.birthday_text_hint);
        birthday.setOnClickListener(this);
        //上传头像
        uploadPhoto = (ImageView) findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(this);
        //成员姓名
        memberName = (EditText) findViewById(R.id.member_name_hint);
        //关系
        relation = (EditText) findViewById(R.id.member_relation_hint);
        //添加按钮
        addBtn = (Button) findViewById(R.id.finish_btn);
        addBtn.setOnClickListener(this);
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
                //照片文件名（一直使用）
                if (fileName == null){
                    fileName = user.getUid() +"_" +  new Date().getTime() + ".jpg";
                }
                new ActionSheetDialog(AddMemberActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setTitle("提示")
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //家庭成员照片
                                File outputImage = new File(Environment.getExternalStorageDirectory(),fileName);
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
            case R.id.finish_btn:
                //添加家庭成员
                new CommomDialog(AddMemberActivity.this, R.style.dialog, "您确定"  +  ( fid > 0 ? "更新":"保存") + "此家庭成员信息么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //保存成功
                        if (confirm) {
                            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(AddMemberActivity.this,"保存中");
                            if (fid <= 0){
                                //保存家庭成员信息
                                addMember(dialog);
                            }else {
                               // 更新家庭成员信息
                                updateMember(dialog);
                            }
                        }
                        //取消保存
                        else {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
                break;
            default:
                break;
        }
    }
    //添加家庭成员
    private void addMember(Dialog dialog){
        FamilyMember member = new FamilyMember();
        //用户编号
      if (user.getUid() <= 0){
          dialog.dismiss();
          WeiboDialogUtils.closeDialog(weiboDialogUtils);
          AlterUtil.alterTextShort(this,"请先登陆");
          Intent intent = new Intent(this,LoginActivity.class);
          startActivity(intent);
      }
      //成员姓名
      member.setUid(user.getUid());
      if (TextUtils.isEmpty(memberName.getText())){
          dialog.dismiss();
          WeiboDialogUtils.closeDialog(weiboDialogUtils);
          AlterUtil.alterTextShort(this,"请填写家庭成员姓名");
          return;
      }
        member.setName(memberName.getText().toString());
      //性别
        member.setGender(maleRadio.isChecked() ? 1 : 0);
        //头像
        member.setProfile(fileName);
        //关系
        if (!TextUtils.isEmpty(relation.getText().toString())){
            member.setRelation("关系 : " + relation.getText().toString());
        }
        //出生日期
        member.setBirthday(birthday.getText().toString());
        String address = ConfigUtil.getServerAddress() + "/member/registerMember";
        //用户设置了头像
       if (fileName != null){
           //上传图片到服务器
           String url = ConfigUtil.getServerAddress() + "/uploadImage/3" ;
           try {
               AndroidUploadFile.uploadFile(FileUtil.getMemberPhoto(this,fileName).getAbsolutePath(), url, new HttpCallbackListener() {
                   @Override
                   public void onFinish(String response) {
                       JSONObject jsonObject = JSONObject.parseObject(response);
                       int code = jsonObject.getIntValue("code");
                       if (code == 0){
                           //上传用户信息到服务器持久化
                           addMember(dialog,member,address);
                       }else if (code ==1){
                           WeiboDialogUtils.closeDialog(weiboDialogUtils);
                           Looper.prepare();
                           AlterUtil.alterTextLong(AddMemberActivity.this,"头像为空");
                           Looper.loop();
                       }else {
                           WeiboDialogUtils.closeDialog(weiboDialogUtils);
                           Looper.prepare();
                           AlterUtil.alterTextLong(AddMemberActivity.this,"头像上传失败");
                           Looper.loop();

                       }
                   }
                   @Override
                   public void onError(Exception e) {
                       WeiboDialogUtils.closeDialog(weiboDialogUtils);
                       Looper.prepare();
                       AlterUtil.alterTextLong(AddMemberActivity.this,"头像上传失败");
                       Looper.loop();
                   }
               });
           } catch (IOException e) {
               e.printStackTrace();
           }
       }else {
           //上传用户信息到服务器持久化
         addMember(dialog,member,address);
       }
    }

    //上传家庭成员信息
    public void addMember(Dialog dialog,FamilyMember member,String address){
        try {
            HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(member),new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        //更新或者增加的时候进行广播
                        Intent intent = new Intent();
                        intent.setAction("action.refreshMemberList");
                        sendBroadcast(intent);
                        //家庭成员信息保存成功，对话框消失，跳转到家庭成员列表
                        dialog.dismiss();
                        Intent confirmIntent = new Intent(AddMemberActivity.this, MemberManageActivity.class);
                        AddMemberActivity.this.finish();
                        startActivity(confirmIntent);
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(AddMemberActivity.this,"家庭成员信息"  + (fid > 0 ?"更新":"保存") + "成功");
                        Looper.loop();
                    }else {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(AddMemberActivity.this,"家庭成员信息"  + (fid > 0 ?"更新":"保存") + "失败");
                        Looper.loop();
                    }

                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextLong(AddMemberActivity.this,"家庭成员信息"  + (fid > 0 ?"更新":"保存") + "失败");
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新家庭成员信息
    private void updateMember(Dialog dialog){
        String address = ConfigUtil.getServerAddress() + "/member/updateMember";
        FamilyMember member = new FamilyMember();
        //用户编号
        if (user.getUid() <= 0){
            dialog.dismiss();
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
            AlterUtil.alterTextShort(this,"请先登陆");
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        if (fid <= 0){
            AlterUtil.alterTextShort(this,"更新家庭成员信息失败");
            return;
        }
        //家庭成员信息编号
        member.setFid(fid);
        //成员姓名
        member.setUid(user.getUid());
        if (TextUtils.isEmpty(memberName.getText())){
            dialog.dismiss();
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
            AlterUtil.alterTextShort(this,"请填写家庭成员姓名");
            return;
        }
        member.setName(memberName.getText().toString());
        //性别
        member.setGender(maleRadio.isChecked() ? 1 : 0);
        //头像
        member.setProfile(fileName);
        //关系
        if (!TextUtils.isEmpty(relation.getText().toString())){
            member.setRelation("关系 : " + relation.getText().toString());
        }
        //出生日期
        member.setBirthday(birthday.getText().toString());
        //用户设置了头像
        if (fileName != null){
            //上传图片到服务器
            String url = ConfigUtil.getServerAddress() + "/uploadImage/3" ;
            try {
                AndroidUploadFile.uploadFile(FileUtil.getMemberPhoto(this,fileName).getAbsolutePath(), url, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if (code == 0){
                            //上传用户信息到服务器持久化
                            addMember(dialog,member,address);
                        }else if (code ==1){
                            WeiboDialogUtils.closeDialog(weiboDialogUtils);
                            Looper.prepare();
                            AlterUtil.alterTextLong(AddMemberActivity.this,"头像为空");
                            Looper.loop();
                        }else {
                            WeiboDialogUtils.closeDialog(weiboDialogUtils);
                            Looper.prepare();
                            AlterUtil.alterTextLong(AddMemberActivity.this,"头像上传失败");
                            Looper.loop();

                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(AddMemberActivity.this,"头像上传失败");
                        Looper.loop();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //上传用户信息到服务器持久化
            addMember(dialog,member,address);
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
            String personPhotoPath  = getRealPathFromURI(uri);
            try{
                Bitmap bitmap = BitmapFactory.decodeFile(personPhotoPath);
                imagePath = FileUtil.getMemberPhoto(getApplicationContext(),fileName).getAbsolutePath();
                File memberPhoto = new File(imagePath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(memberPhoto));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                bos.flush();
                bos.close();
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
                imagePath = FileUtil.getMemberPhoto(getApplicationContext(),fileName).getAbsolutePath();
                File personPhoto = new File(imagePath);
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(personPhoto));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                bos.flush();
                bos.close();
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
}

