package com.blk.medical_record;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.blk.LoginActivity;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.RegisterActivity;
import com.blk.common.CommomDialog;
import com.blk.common.MyApplication;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.AndroidUploadFile;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.FileUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.ToolBarSet;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.fragment.medicalRecord_Fragment;
import com.blk.health_tool.HealthRecordDetailActivity;
import com.blk.medical_record.Adapter.CaseHistoryDetailBaseAdapter;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class medicalRecordContentActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;    //选择图片的请求码
    private static final int REQUEST_CODE_CAMERA = 102;        //拍照的请求码
    private static final int SAVE = 103;
    private static final int SHOW_DETAIL = 104;
    private static final int CROP_PHOTO = 105;  //裁剪的请求码
    private Uri imageUri;  //图片路径
    private Dialog weiboDialogUtils;                         //加载框
    private ImageView imageView;
    private EditText patient_name;                       //病人姓名
    private EditText date;                               //看病日期
    private EditText hospital_name;                      //就诊医院
    private EditText department;                         //科室
    private EditText doctor_name;                        //就诊医生
    public  EditText chiefComplaint;                     //主诉
    public  EditText presentIllness;                     //现状
    public  EditText physicalExamination;                //体格检查
    public  EditText diagnosisResult;                    //诊断结果
    public  EditText handleAdvice;                      //处理意见
    private Button save;
    private byte[] bytes;
    private ImageView icon_back;                       //返回键按钮
    private Map<String,String> map;
    private String imagePath;                         //图片路径
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息
    private String fileName; //病历文件名
    private int cid = 0;
    private TextView cidText;
    private CaseHistory caseHistoryDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = JSONObject.parseObject(userInfo,User.class);
        //防止软键盘遮挡
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.medical_record_content);

        //初始化组件
        InitView();
        //事件
        InitEvent();
        /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();

        int data=bundle.getInt("data");
        cid = bundle.getInt("cid",0);

        //data ==0 || data == 1 表示是通过拍照识别，data == 2 表示点击病历查看具体病历详情
        //选择图片
        if(data==0)
        {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(this,"识别中");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int ret = ActivityCompat.checkSelfPermission(medicalRecordContentActivity.this, Manifest.permission
                        .READ_EXTERNAL_STORAGE);
                if (ret != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(medicalRecordContentActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                    return;
                }
            }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

        }
        //进行拍照
        else if(data==1)
        {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(this,"识别中");
            File outputImage = new File(Environment.getExternalStorageDirectory(),
                    "tempMedicalImage" + ".jpg");
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
        else if(data == 2){
            //加载病历信息
            if (cid <= 0){
                AlterUtil.alterTextLong(this,"加载病历信息失败");
            }else {
                new CaseHistoryThread().execute();
            }

        }

    }

    class CaseHistoryThread extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(medicalRecordContentActivity.this,"加载中...");
        }

        @Override
        protected Void doInBackground(Void... integers) {
            String address = ConfigUtil.getServerAddress() + "/caseHistory/findById/" + cid ;

            HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        caseHistoryDetail = JSONObject.parseObject(data,CaseHistory.class);
                        //设置病历编号
                        cidText.setText(String.valueOf(caseHistoryDetail.getCid()));
                        //图片信息
                        String fileName = caseHistoryDetail.getPhoto();
                        Bitmap bitmap = BitmapFactory.decodeFile(MyApplication.getContext().getFilesDir().getAbsolutePath() + "/image/" + fileName.substring(fileName.lastIndexOf("\\") + 1));
                        imageView.setImageBitmap(bitmap);
                        //就诊人
                        patient_name.setText(caseHistoryDetail.getVisitName());
                        //就诊日期
                        date.setText(caseHistoryDetail.getVisitDate());
                        //医院名称
                        hospital_name.setText(caseHistoryDetail.getHospitalName());
                        //科室
                        department.setText(caseHistoryDetail.getOffice());
                        //就诊医生
                        doctor_name.setText(caseHistoryDetail.getDoctorName());
                        //主诉
                        chiefComplaint.setText(caseHistoryDetail.getMainSuit());
                        //现病史
                        presentIllness.setText(caseHistoryDetail.getHistoryNow());
                        //体格检查
                        physicalExamination.setText(caseHistoryDetail.getCheckup());
                        //诊断结果
                        diagnosisResult.setText(caseHistoryDetail.getDiagnosisResult());
                        //处理意见
                        handleAdvice.setText(caseHistoryDetail.getSuggestion());
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"加载病历信息失败");
                        Looper.loop();
                    }
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    AlterUtil.alterTextShort(medicalRecordContentActivity.this,"加载病历信息失败");
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
    //初始化控件
    private void InitView() {
        save = (Button) findViewById(R.id.save);
        imageView= (ImageView) findViewById(R.id.imageView);
        icon_back = (ImageView) findViewById(R.id.icon_back);
        patient_name= (EditText) findViewById(R.id.patient_name);
        date= (EditText)findViewById(R.id.date);
        hospital_name= (EditText) findViewById(R.id.hospital_name);
        department= (EditText) findViewById(R.id.department);
        doctor_name= (EditText) findViewById(R.id.doctor_name);
        chiefComplaint = (EditText) findViewById(R.id.ChiefComplaint);
        presentIllness = (EditText) findViewById(R.id.PresentIllness);
        physicalExamination = (EditText) findViewById(R.id.PhysicalExamination);
        diagnosisResult = (EditText) findViewById(R.id.DiagnosisResult);
        handleAdvice = (EditText) findViewById(R.id.HandleAdvice);
        map = new HashMap<String,String>();
        cidText = (TextView) findViewById(R.id.cid);
    }

    //事件
    private void InitEvent() {
        //照片点击事件
        //TouchImage();
        //保存按钮
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CommomDialog(medicalRecordContentActivity.this, R.style.dialog, "您确定"  +  (cid > 0 ? "更新":"保存") + "此病历么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //保存成功
                        if (confirm) {
                            dialog.dismiss();
                            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(medicalRecordContentActivity.this,"保存中");
                             if (cid <= 0){
                                 //保存病历信息
                                 saveCaseHistory(dialog);
                             }else {
                                 //更新病历信息
                                 updateCaseHistory(dialog);
                             }

                        }
                        //取消保存
                        else {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
            }
        });

        //返回按钮
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new CommomDialog(medicalRecordContentActivity.this, R.style.dialog, "您确定退出，不保存此病历么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //不保存，确认退出
                        if (confirm) {
                            //病历保存成功，对话框消失，跳转到病历详情页面
                            dialog.dismiss();
                            Intent confirmIntent = new Intent(medicalRecordContentActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("data", 1);
                            confirmIntent.putExtras(bundle);
                            medicalRecordContentActivity.this.finish();
                            startActivity(confirmIntent);
                        }
                        //取消退出
                        else {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();


            }
        });
    }

    //保存病历信息
    public void  saveCaseHistory(Dialog dialog){
        if (user.getUid() <= 0){
            AlterUtil.alterTextLong(medicalRecordContentActivity.this,"请先登陆");
            Intent intent = new Intent(medicalRecordContentActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        //保存病历图片到本地
        downloadImage();
        //上传图片到服务器
        String address = ConfigUtil.getServerAddress() + "/uploadImage/2/" + fileName.substring(fileName.lastIndexOf("/") + 1);
        try {
            AndroidUploadFile.uploadFile(fileName, address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        CaseHistory caseHistory = new CaseHistory();
                        //用户信息
                        caseHistory.setUid(user.getUid());
                        //图片路径
                        caseHistory.setPhoto(fileName.substring(fileName.lastIndexOf("/") + 1));
                        //就诊人
                        caseHistory.setVisitName(patient_name.getText().toString());
                        //医院名称
                        caseHistory.setHospitalName(hospital_name.getText().toString());
                        //科室
                        caseHistory.setOffice(department.getText().toString());
                        //就诊医生
                        caseHistory.setDoctorName(doctor_name.getText().toString());
                        //就诊日期
                        caseHistory.setVisitDate(date.getText().toString());
                        //体格检查
                        caseHistory.setCheckup(physicalExamination.getText().toString());
                        //诊断结果
                        caseHistory.setDiagnosisResult(diagnosisResult.getText().toString());
                        //主诉
                        caseHistory.setMainSuit(chiefComplaint.getText().toString());
                        //现病史
                        caseHistory.setHistoryNow(presentIllness.getText().toString());
                        //处理意见
                        caseHistory.setSuggestion(handleAdvice.getText().toString());
                        try {
                            String address = ConfigUtil.getServerAddress() + "/caseHistory/addCaseHistory";
                            HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(caseHistory),new HttpCallbackListener() {
                                @Override
                                public void onFinish(String response) {
                                    JSONObject  jsonObject = JSONObject.parseObject(response);
                                    int code = jsonObject.getIntValue("code");
                                    if (code == 0){
                                        //更新或者增加的时候进行广播
                                        Intent intent = new Intent();
                                        intent.setAction("action.refreshCaseHistory");
                                        sendBroadcast(intent);
                                        //病历保存成功，对话框消失，跳转到病历详情页面
                                        dialog.dismiss();
                                        Intent confirmIntent = new Intent(medicalRecordContentActivity.this, MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("data", 1);
                                        confirmIntent.putExtras(bundle);
                                        medicalRecordContentActivity.this.finish();
                                        startActivity(confirmIntent);
                                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                                        Looper.prepare();
                                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历保存成功");
                                        Looper.loop();
                                    }else {
                                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                                        Looper.prepare();
                                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历保存失败");
                                        Looper.loop();
                                    }

                                }

                                @Override
                                public void onError(Exception e) {
                                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                                    Looper.prepare();
                                    AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历保存失败");
                                    Looper.loop();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (code ==1){
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"头像为空");
                        Looper.loop();
                    }else {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"头像上传失败");
                        Looper.loop();

                    }
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextLong(medicalRecordContentActivity.this,"头像上传失败");
                    Looper.loop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     //更新病历信息
    public void  updateCaseHistory(Dialog dialog){
        if (user.getUid() <= 0){
            AlterUtil.alterTextLong(medicalRecordContentActivity.this,"请先登陆");
            Intent intent = new Intent(medicalRecordContentActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        CaseHistory caseHistory = new CaseHistory();
        //病历编号
        caseHistory.setCid(Integer.valueOf(cidText.getText().toString()));
        //用户信息
        caseHistory.setUid(user.getUid());
        //图片路径
        caseHistory.setPhoto(caseHistoryDetail.getPhoto());
        //就诊人
        caseHistory.setVisitName(patient_name.getText().toString());
        //医院名称
        caseHistory.setHospitalName(hospital_name.getText().toString());
        //科室
        caseHistory.setOffice(department.getText().toString());
        //就诊医生
        caseHistory.setDoctorName(doctor_name.getText().toString());
        //就诊日期
        caseHistory.setVisitDate(date.getText().toString());
        //体格检查
        caseHistory.setCheckup(physicalExamination.getText().toString());
        //诊断结果
        caseHistory.setDiagnosisResult(diagnosisResult.getText().toString());
        //主诉
        caseHistory.setMainSuit(chiefComplaint.getText().toString());
        //现病史
        caseHistory.setHistoryNow(presentIllness.getText().toString());
        //处理意见
        caseHistory.setSuggestion(handleAdvice.getText().toString());
        try {
            String address = ConfigUtil.getServerAddress() + "/caseHistory/updateCaseHistory";
            HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(caseHistory),new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        //更新或者增加的时候进行广播
                        Intent intent = new Intent();
                        intent.setAction("action.refreshCaseHistory");
                        sendBroadcast(intent);
                        //病历保存成功，对话框消失，跳转到病历详情页面
                        dialog.dismiss();
                        Intent confirmIntent = new Intent(medicalRecordContentActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("data", 1);
                        confirmIntent.putExtras(bundle);
                        medicalRecordContentActivity.this.finish();
                        startActivity(confirmIntent);
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历信息更新成功");
                        Looper.loop();
                    }else {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历信息更新失败");
                        Looper.loop();
                    }

                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextLong(medicalRecordContentActivity.this,"病历信息更新失败");
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择图片，sd卡上
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            imagePath = getRealPathFromURI(imageUri);
            //识别图片并给ui赋值
            recGeneral(imagePath);

        }

        //拍照
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
            //识别图片并给ui赋值
//            recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//            //图片路径
//            imagePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
        } else if (requestCode == CROP_PHOTO && resultCode == Activity.RESULT_OK) {
                 imagePath = getRealPathFromURI(imageUri);
                //识别图片并给ui赋值
                recGeneral(imagePath);
        }
        else
            finish();

    }

    /**
     * 保存图片到本地data/files/image文件夹中
     * @return
     */
    private Void downloadImage(){
        try {
            //创建目录
            File dir = new File(getFilesDir().getAbsolutePath() + "/image/");
            if (!dir.exists()){
                dir.mkdir();
            }
            fileName = dir.getAbsolutePath() + "/" +  user.getUid() + "_" + new Date().getTime() + imagePath.substring(imagePath.lastIndexOf("."));

            File personPhoto = new File(fileName);
            //将uri中的图片转换为bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(imageUri));
            imagePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(personPhoto));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据uri获取图片的真实路径
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

    private void recGeneral(final String filePath) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
              //  Toast.makeText(medicalRecordContentActivity.this, "1111", Toast.LENGTH_SHORT).show();
                StringBuilder sb = new StringBuilder();
                String pattern1 = ".*姓名.*";
                String pattern2=".*时间.*";
                String pattern3=".*医院.*";
                String pattern4=".*科室.*";
                String pattern5=".*医生.*";
                String pattern6=".*主诉.*";
                String pattern7=".*现病史.*";
                String pattern8=".*体格检查.*";
                String pattern9=".*诊断.*";
                String pattern10=".*处理意见.*";
                String time = null;
               for (Word word : result.getWordList()) {
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                String content=sb.toString().replace("性别","#性别");
                content=content.replace("姓名","#姓名");
                content=content.replace("门诊病历","#门诊病历");
                content=content.replace("年龄","#年龄");
                content=content.replace("科室","#科室");
                content=content.replace("就诊时间","#就诊时间");
                content=content.replace("主诉","#主诉");
                content=content.replace("现病史","#现病史");
                content=content.replace("既往史","#既往史");
                content=content.replace("个人史","#个人史");
                content=content.replace("家族史","#家族史");
                content=content.replace("婚育史","#婚育史");
                content=content.replace("过敏史","#过敏史");
                content=content.replace("辅助检查","#辅助检查");
                content=content.replace("体格检查","#体格检查");
                content=content.replace("初步诊断","#初步诊断");
                content=content.replace("处理意见","#处理意见");
                content=content.replace("医生","#医生");
                //根据图片路径获取图片
                File file=new File(filePath);

                if(file.exists())
                {
                    //信息提取，并且赋值
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                    bytes = out.toByteArray();
                    imageView.setImageBitmap(bitmap);
                    patient_name.setText(readTxtContent(pattern1,content));
                    time=readTxtContent(pattern2,content);
                    date.setText(time);
                    hospital_name.setText(readTxtContent(pattern3,content));
                    department.setText(readTxtContent(pattern4,content));
                    doctor_name.setText(readTxtContent(pattern5,content));
                    chiefComplaint.setText(readTxtContent(pattern6,content));
                    presentIllness.setText(readTxtContent(pattern7,content));
                    physicalExamination.setText( readTxtContent(pattern8,content));
                    diagnosisResult.setText(readTxtContent(pattern9,content));
                    handleAdvice.setText(readTxtContent(pattern10,content));
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }
            }


            @Override
            public void onError(OCRError error) {
             //   handleAdvice.setText(error.getMessage());
              //  Toast.makeText(medicalRecordContentActivity.this, "22222", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  String readTxtContent(String pattern1,String content) {
        String data = "";
        String[]  goal=content.split("\n");
        String pattern2=".*#.*";
        for(int i=0;i<goal.length;i++)
        {
            //第一步:先判断句子里面是否包含关键词
            boolean isMatch1=Pattern.matches(pattern1,goal[i]);
            //包含关键词
            if(isMatch1)
            {
                //第二步：再次判断句子里面是否包含界定符（比如：xxxx医院这个界定符不好定）
                //句子包含界定符
                if(Pattern.matches(pattern2,goal[i]))
                {
                    char[] first = goal[i].toCharArray();
                    int q=0;
                    for(int m=0;m<first.length;m++)
                    {
                        //从界定符位置开始读取
                        if(first[m]=='#')
                        {
                            for(q=m+1;q<first.length;q++)
                            {
                                //判断是否到达另外一个关键词的界定符，如果是的话，直接终止,如果不是的话，直接读取下一个字符
                                if(first[q]=='#')
                                    break;
                                else
                                {
                                    data = data + first[q];
                                }
                            }
                            break;
                        }

                    }
                    //判断上一行有没读取完，如果读取完了，则继续读取下一行
                    if(q>=first.length)
                    {
                        for(int j=i+1;j<goal.length;j++)
                        {
                            boolean isMatch3=Pattern.matches(pattern2,goal[j]);
                            if(!isMatch3)
                            {
                                data=data+goal[j];
                            }
                            else
                            {
                                char[] second=goal[j].toCharArray();
                                for(int k=0;k<second.length;k++)
                                {
                                    if(second[k]!='#')
                                        data=data+second[k];
                                    else
                                        break;
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
                else
                {
                    data=goal[i];
                    break;
                }
            }
        }
        String pattern3=".*:.*";
        boolean isMatch4= Pattern.matches(pattern3,data);
        if(isMatch4)
        {
            String[]  str=data.split(":");
            data = data.substring(str[0].length()+1,data.length());
        }
        return data;
    }

}
