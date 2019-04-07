package com.blk.health_tool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blk.LoginActivity;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.RegisterActivity;
import com.blk.common.CommomDialog;
import com.blk.common.MyApplication;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.HealthRecord;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.medical_record.medicalRecordContentActivity;

import java.util.Calendar;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class HealthRecordDetailActivity extends Activity implements View.OnClickListener {

    private FloatingActionButton add, back, delete;
    private TextView health_record_title,health_record_id;
    private EditText time,address,food,sports,symptom;
    private ImageView back_imageView;
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息
    private Dialog weiboDialogUtils;                         //加载框
    private int hid;   //康复记录信息编号
    private HealthRecord healthRecordDetail; //康复记录详细信息
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
        setContentView(R.layout.healthy_record_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            hid = bundle.getInt("hid");
        }
        //初始化控件
        initView();
        //执行更新操作
        if (hid > 0 ){
            new HealthRecordThread().execute();
        }
    }

    class HealthRecordThread extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(HealthRecordDetailActivity.this,"加载中...");
        }

        @Override
        protected Void doInBackground(Void... integers) {
            String url = ConfigUtil.getServerAddress() + "/healthRecord/findById/" + hid ;
            HttpRequestUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        healthRecordDetail = JSONObject.parseObject(data,HealthRecord.class);
                        //设置康复记录编号
                        health_record_id.setText(String.valueOf(healthRecordDetail.getHid()));
                        //时间
                        time.setText(String.valueOf(healthRecordDetail.getTime()));
                        //地点
                        address.setText(healthRecordDetail.getAddress());
                        //食物
                        food.setText(healthRecordDetail.getFood());
                        //运动
                        sports.setText(healthRecordDetail.getSport());
                        //症状
                        symptom.setText(healthRecordDetail.getSymptom());
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"加载康复记录信息失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"加载康复记录信息失败");
                    Looper.loop();
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
        }
    }
    public void initView() {
        //标题
        health_record_title = (TextView) findViewById(R.id.health_record_title);
        health_record_title.setText("添加康复记录");
        //康复记录编号
        health_record_id = (TextView) findViewById(R.id.hid);
        //时间
        time = (EditText) findViewById(R.id.health_record_time);
        time.setOnClickListener(this);
        //地点
        address = (EditText) findViewById(R.id.health_record_address);
        //食物、饮料摄入
        food = (EditText) findViewById(R.id.health_record_food);
        //运动情况描述
        sports = (EditText) findViewById(R.id.health_record_sport);
        //症状
        symptom = (EditText) findViewById(R.id.health_record_symptom);
        //添加按钮
        add = (FloatingActionButton) findViewById(R.id.health_record_add);
        add.setOnClickListener(this);
        //删除按钮
        delete = (FloatingActionButton) findViewById(R.id.health_record_delete);
        if (hid > 0){
            delete.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener(this);
//        //返回按钮
        back = (FloatingActionButton) findViewById(R.id.health_record_back_icon);
        back.setOnClickListener(this);
        //标题栏的返回按钮
        back_imageView = (ImageView) findViewById(R.id.health_record_back);
        back_imageView.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //选择时间
            case R.id.health_record_time:
                TimePicker timePicker = getTimePicker();
                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        time.setFocusable(true);
                        time.setText(hour + ":" + minute);
                        time.setFocusable(false);
                    }
                });
                timePicker.show();
                break;
            //返回按钮
            case R.id.health_record_back_icon:
                //弹出提示框
                new CommomDialog(HealthRecordDetailActivity.this, R.style.dialog, "您确定不保存此康复记录？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                             HealthRecordDetailActivity.this.finish();
                            Intent intent = new Intent(HealthRecordDetailActivity.this,HealthRecordActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
                break;
            //添加按钮
            case R.id.health_record_add:
                new CommomDialog(HealthRecordDetailActivity.this, R.style.dialog, "您确定"  +  (hid > 0 ? "更新":"保存") + "此康复记录信息么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //保存成功
                        if (confirm) {
                            dialog.dismiss();
                            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(HealthRecordDetailActivity.this,"保存中");
                            if (hid <= 0){
                                //保存康复记录信息
                                saveHealthRecord(dialog);
                                HealthRecordDetailActivity.this.finish();
                            }else {
                                //更新康复记录信息
                                updateHealthRecord(dialog);
                                HealthRecordDetailActivity.this.finish();

                            }
                        }
                        //取消保存
                        else {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();

                break;
                //删除按钮
            case R.id.health_record_delete:
                //弹出提示框
                new CommomDialog(HealthRecordDetailActivity.this, R.style.dialog, "您确定删除此康复记录？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            deleteHealthRecord();
                            HealthRecordDetailActivity.this.finish();
                            Intent intent = new Intent(HealthRecordDetailActivity.this,HealthRecordActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
                break;
        }
    }


    //保存康复记录信息
    public void  saveHealthRecord(Dialog dialog){
        if (user.getUid() <= 0){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请先登陆");
            Intent intent = new Intent(HealthRecordDetailActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        HealthRecord healthRecord = new HealthRecord();
        //用户编号
        healthRecord.setUid(user.getUid());
        //时间
         if (TextUtils.isEmpty(time.getText())){
             AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请填写具体时间点");
             return;
         }
         healthRecord.setTime(time.getText().toString());
         //地点
        if (TextUtils.isEmpty(address.getText())){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请填写详细地点");
            return;
        }
        healthRecord.setAddress(address.getText().toString());
        //症状描述
        if (TextUtils.isEmpty(symptom.getText())){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请描述具体的症状");
            return;
        }
        healthRecord.setSymptom(symptom.getText().toString());
        //食物
        healthRecord.setFood(food.getText().toString());
        //运动
        healthRecord.setSport(sports.getText().toString());
        try {
            String address = ConfigUtil.getServerAddress() + "/healthRecord/addHealthRecord";
            HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(healthRecord),new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        //病历保存成功，对话框消失，跳转到病历详情页面
                        dialog.dismiss();
                        //更新或者增加的时候进行广播
                        Intent intent = new Intent();
                        intent.setAction("action.refreshHealthRecord");
                        sendBroadcast(intent);
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息保存成功");
                        Looper.loop();
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息保存失败");
                        Looper.loop();
                    }
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息保存失败");
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //更新康复记录信息
    public void updateHealthRecord(Dialog dialog){
        if (user.getUid() <= 0){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请先登陆");
            Intent intent = new Intent(HealthRecordDetailActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        if (hid <= 0){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"更新康复记录信息失败");
            return;
        }
        HealthRecord healthRecord = new HealthRecord();
        //时间
        if (TextUtils.isEmpty(time.getText())){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请填写具体时间点");
            return;
        }
        healthRecord.setTime(time.getText().toString());
        //地点
        if (TextUtils.isEmpty(address.getText())){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请填写详细地点");
            return;
        }
        healthRecord.setAddress(address.getText().toString());
        //症状描述
        if (TextUtils.isEmpty(symptom.getText())){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"请描述具体的症状");
            return;
        }
        healthRecord.setSymptom(symptom.getText().toString());
        //食物
        healthRecord.setFood(food.getText().toString());
        //运动
        healthRecord.setSport(sports.getText().toString());
        //用户编号
        healthRecord.setUid(user.getUid());
        //康复记录表编号
        healthRecord.setHid(hid);
        try {
            String address = ConfigUtil.getServerAddress() + "/healthRecord/updateHealthRecord";
            HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(healthRecord),new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    JSONObject  jsonObject = JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        //病历保存成功，对话框消失，跳转到病历详情页面
                        dialog.dismiss();
                        //更新或者增加的时候进行广播
                        Intent intent = new Intent();
                        intent.setAction("action.refreshHealthRecord");
                        sendBroadcast(intent);
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息更新成功");
                        Looper.loop();
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息更新失败");
                        Looper.loop();
                    }
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"康复记录信息保存失败");
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     //删除康复记录信息
    public void  deleteHealthRecord(){
         if (hid < 0){
            AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"删除康复记录信息失败");
            return;
          }
            String address = ConfigUtil.getServerAddress() + "/healthRecord/deleteHealthRecord/" + hid;
            HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"删除康复记录信息成功");
                        Looper.loop();
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"删除康复记录信息失败");
                        Looper.loop();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextShort(HealthRecordDetailActivity.this,"删除康复记录信息失败");
                    Looper.loop();
                }
            });
    }
    public TimePicker getTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setTextPadding(ConvertUtils.toPx(this, 15));
        return picker;
    }
}
