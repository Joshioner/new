package com.blk.health_tool;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.MedicalAuscultation;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.AndroidUploadFile;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.medical_record.AddMemberActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MedicalAuscultationActivity extends AppCompatActivity implements View.OnClickListener{
    // 语音文件
    private String fileName = null;
    // 音频文件保存的路径
    private String path = "";
    //界面控件
    private LinearLayout startRecord,finishRecord,reset,startPlay,stopPlay,pausePlay;
    private ImageView startRecordImage,finishRecordImage;
    private TextView startRecordText,finishRecordText,pausePlayText;
    private TextView time; //录音时长

    // 语音操作对象
    private MediaPlayer mPlayer = null;// 播放器
    private MediaRecorder mRecorder = null;// 录音器
    private boolean isPause = false;// 当前录音是否处于暂停状态
    private boolean isPausePlay = false;// 当前播放器是否处于暂停状态
    private ArrayList<String> mList = new ArrayList<String>();// 待合成的录音片段
    private ArrayList<String> list = new ArrayList<String>();// 已合成的录音片段
    private String deleteStr = null; // 列表中要删除的文件名
    private Timer timer;
    private String playFileName = null;// 选中的播放文件
    // 相关变量
    private int second = 0;
    private int minute = 0;
    private int hour = 0;
    private View whichSelecte = null;// 记录被选中的Item
    private long limitTime = 0;// 录音文件最短事件1秒
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息
    private Dialog weiboDialogUtils;                         //加载框
    private boolean isFinish = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.medical_auscultation);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo,User.class);
        // 初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Davie/" + getPackageName() + "/Record";
        //录音时长
        time = (TextView) findViewById(R.id.tv_record_time);
        //开始录音
        startRecord = (LinearLayout) findViewById(R.id.start_record);
        // 对按钮的可点击事件的控制是保证不出现空指针的重点！！
        startRecord.setOnClickListener(this);
        startRecordImage = (ImageView) findViewById(R.id.start_record_image);
        startRecordText = (TextView) findViewById(R.id.start_record_text);
        //完成录音
        finishRecord = (LinearLayout) findViewById(R.id.finish_record);
        finishRecord.setOnClickListener(this);
        finishRecord.setClickable(false);
        finishRecordImage = (ImageView) findViewById(R.id.finish_record_image);
        finishRecordText = (TextView) findViewById(R.id.finish_record_text);
        //重置
        reset = (LinearLayout) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        reset.setClickable(false);
        //开始播放
        startPlay = (LinearLayout) findViewById(R.id.start_play);
        startPlay.setOnClickListener(this);
        startPlay.setClickable(false);
        //暂停播放
        pausePlay = (LinearLayout) findViewById(R.id.pause_play);
        pausePlayText = (TextView) findViewById(R.id.pause_play_text);
        pausePlay.setOnClickListener(this);
        pausePlay.setClickable(false);
        //停止播放
        stopPlay = (LinearLayout) findViewById(R.id.stop_play);
        stopPlay.setOnClickListener(this);
        stopPlay.setClickable(false);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.start_record:   //开始录音
                // 判断SD卡是否存在
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡状态异常，请检查后重试！", Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                // 开始录音
                startRecord();
                // 录音计时
                recordTime();
                break;
            case R.id.finish_record:   //完成录音
                if (isPause) {
                    // 完成录音
                    finishRecord();
                } else {
                    // 暂停录音
                    try {
                        pauseRecord();
                    } catch (InterruptedException e) {
                        // 当一个线程处于等待，睡眠，或者占用，也就是说阻塞状态，而这时线程被中断就会抛出这类错误
                        // 上百次测试还未发现这个异常，但是需要捕获
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.start_play:    //开始播放
                // 播放录音
                playRecord();
                break;
            case R.id.stop_play:    //停止播放
                // 停止播放
                startPlay.setClickable(true);
                stopPlay.setClickable(false);
                startRecord.setClickable(true);
                pausePlay.setClickable(false);
                if (mPlayer != null) {
                    // 释放资源
                    // 对MediaPlayer多次使用而不释放资源就会出现MediaPlayer create faild 的异常
                    mPlayer.release();
                    mPlayer = null;
                }
                reset.setClickable(true);
                Toast.makeText(this,"停止播放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause_play:   //暂停播放
                if (isPausePlay) {
                    pausePlayText.setText("暂停播放");
                    pausePlay.setClickable(true);
                    isPausePlay = false;
                    mPlayer.start();
                } else {
                    if (mPlayer != null) {
                        mPlayer.pause();
                    }
                    pausePlayText.setText("继续播放");
                    pausePlay.setClickable(true);
                    isPausePlay = true;
                }
                Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset:
                // 删除录音文件
                deleteRecord();
                break;
            default:
                break;
        }
    }

    // 删除录音文件
    private void deleteRecord() {
        // 删除所选中的录音文件
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            time.setText("");
        }
        startPlay.setClickable(false);
        playFileName = null;
        reset.setClickable(false);
        startRecordImage.setImageResource(R.mipmap.record);
        startRecordText.setText("开始录音");
        startRecord.setClickable(true);
        finishRecordImage.setImageResource(R.mipmap.finish_record);
        finishRecordText.setText("完成录音");
        finishRecord.setClickable(true);
        time.setText("00:00:00");
        Toast.makeText(this,"重置成功",Toast.LENGTH_SHORT).show();
    }
    //开始播放
    private void playRecord() {
        // 对按钮的可点击事件的控制是保证不出现空指针的重点！！
        startRecord.setClickable(false);
        finishRecord.setClickable(false);
        reset.setClickable(false);
        stopPlay.setClickable(true);
        startPlay.setClickable(false);
        pausePlay.setClickable(true);
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        // 播放完毕的监听
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完毕改变状态，释放资源
                mPlayer.release();
                mPlayer = null;
                startRecord.setClickable(true);
                if (!isFinish){
                    finishRecord.setClickable(true);
                }

                startPlay.setClickable(true);
                stopPlay.setClickable(false);
                reset.setClickable(true);
                pausePlay.setClickable(false);
            }
        });
        try {
            // 播放所选中的录音
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            // 若出现异常被捕获后，同样要释放掉资源
            // 否则程序会不稳定，不适合正式项目上使用
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
            Toast.makeText(this, "播放失败,可返回重试！", Toast.LENGTH_SHORT).show();
            stopPlay.setClickable(false);
            reset.setClickable(true);
            pausePlay.setClickable(false);
        }
        Toast.makeText(this,"开始播放",Toast.LENGTH_SHORT).show();
    }
    // 完成录音
    private void finishRecord()  {
        if(mList == null || mList.size() <= 0){
            Toast.makeText(this,"文件已保存成功",Toast.LENGTH_SHORT).show();
            return;
        }
        weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(this,"保存中...");
        mRecorder.release();
        mRecorder = null;
        isPause = false;
        isFinish = true;
        startRecord.setClickable(true);
        startRecordText.setText("开始录音");
        startRecordImage.setImageResource(R.mipmap.record);
        finishRecord.setClickable(false);
        startPlay.setClickable(true);
        timer.cancel();
        // 最后合成的音频文件
        fileName = path + "/" + getTime() + ".m4a";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = null;
        try {
            for (int i = 0; i < mList.size(); i++) {
                File file = new File(mList.get(i));
                // 把因为暂停所录出的多段录音进行读取
                fileInputStream = new FileInputStream(file);
                byte[] mByte = new byte[fileInputStream.available()];
                int length = mByte.length;
                // 第一个录音文件的前六位是不需要删除的
                if (i == 0) {
                    while (fileInputStream.read(mByte) != -1) {
                        fileOutputStream.write(mByte, 0, length);
                    }
                }
                // 之后的文件，去掉前六位
                else {
                    while (fileInputStream.read(mByte) != -1) {
                        fileOutputStream.write(mByte, 6, length - 6);
                    }
                }
            }
        } catch (Exception e) {
            // 这里捕获流的IO异常，万一系统错误需要提示用户
            //关闭加载框
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
            e.printStackTrace();
            Toast.makeText(this, "录音合成出错，请重试！", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                fileOutputStream.flush();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 录音结束 、时间归零
            minute = 0;
            hour = 0;
            second = 0;
        }
        // 不管合成是否成功、删除录音片段
        for (int i = 0; i < mList.size(); i++) {
            File file = new File(mList.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        //文件清除
        mList.clear();
        try {
            String  address = ConfigUtil.getServerAddress() + "/uploadVideo/" + user.getUid();
            //上传音频至服务器并进行语音识别
            AndroidUploadFile.uploadFile(fileName, address, new HttpCallbackListener() {
               @Override
               public void onFinish(String response) {
                   com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                   int code = jsonObject.getIntValue("code");
                   if (code == 0){
                       //更新或者增加的时候进行广播
                       Intent intent = new Intent();
                       intent.setAction("action.refreshAuscultation");
                       sendBroadcast(intent);
                       WeiboDialogUtils.closeDialog(weiboDialogUtils);
                       Looper.prepare();
                       AlterUtil.alterTextShort(MedicalAuscultationActivity.this,"保存音频文件信息成功");
                       Looper.loop();
                   }else {
                       WeiboDialogUtils.closeDialog(weiboDialogUtils);
                       Looper.prepare();
                       AlterUtil.alterTextShort(MedicalAuscultationActivity.this,"保存音频文件信息失败");
                       Looper.loop();
                   }
               }

               @Override
               public void onError(Exception e) {
                   WeiboDialogUtils.closeDialog(weiboDialogUtils);
                   Looper.prepare();
                   AlterUtil.alterTextShort(MedicalAuscultationActivity.this,"保存音频文件信息失败");
                   Looper.loop();
               }
           });
        } catch (Exception e) {
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
            Looper.prepare();
            AlterUtil.alterTextShort(MedicalAuscultationActivity.this,"保存音频文件信息失败");
            Looper.loop();
        }

    }

    // 暂停录音
    private void pauseRecord() throws InterruptedException {
        if (System.currentTimeMillis()-limitTime<1100) {
            //录音文件不得低于一秒钟
            Toast.makeText(this, "录音时间长度不得低于1秒钟！", Toast.LENGTH_SHORT).show();
            return ;
        }
        startPlay.setClickable(true);
        finishRecord.setClickable(true);
        //停止录音
        mRecorder.stop();
        mRecorder.release();
        timer.cancel();
        isPause = true;
        // 将录音片段加入列表
        mList.add(fileName);
        startRecord.setClickable(true);
        startRecordText.setText("继续录音");
        startRecordImage.setImageResource(R.mipmap.record);
        finishRecordText.setText("完成录音");
        finishRecordImage.setImageResource(R.mipmap.finish_record);
        Toast.makeText(this,"暂停录音",Toast.LENGTH_SHORT).show();
    }
    // 开始录音
    private void startRecord() {
        finishRecordText.setText("暂停录音");
        finishRecordImage.setImageResource(R.mipmap.pausr_record);
        startRecordText.setText("录音中...");
        startRecordImage.setImageResource(R.mipmap.record_ing);
        startRecord.setClickable(false);
        finishRecord.setClickable(true);
        if (!isPause) {
            // 新录音清空列表
            mList.clear();
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = path + "/" + getTime() + ".m4a";
        isPause = false;
        //配置mRecorder相应参数
        //从麦克风采集声音数据
        mRecorder = new MediaRecorder();
        //从麦克风采集
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        // 选择amr格式
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        //保存文件为MP4格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置录音保存的文件
        mRecorder.setOutputFile(fileName);
//        //设置声音数据编码格式,音频通用格式是AMR_NB
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //通用的AAC编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置音质频率
        mRecorder.setAudioEncodingBitRate(96000);
        try {
            //准备录音
            mRecorder.prepare();
        } catch (Exception e) {
            // 若录音器启动失败就需要重启应用，屏蔽掉按钮的点击事件。 否则会出现各种异常。
            Toast.makeText(this, "录音器启动失败，请返回重试！", Toast.LENGTH_SHORT).show();
            startRecord.setClickable(false);
            finishRecord.setClickable(false);
            reset.setClickable(false);
            startPlay.setClickable(false);
            stopPlay.setClickable(false);
            pausePlay.setClickable(false);
            reset.setClickable(false);
            mRecorder.release();
            mRecorder = null;
            this.finish();
        }
        if (mRecorder != null) {
            //开始录音
            mRecorder.start();
            limitTime = System.currentTimeMillis();
        }
        Toast.makeText(this,"开始录音",Toast.LENGTH_SHORT).show();

    }

    // 计时器异步更新界面
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time.setText(String.format("%1$02d:%2$02d:%3$02d", hour, minute,
                    second));
            super.handleMessage(msg);
        }
    };

    // 录音计时
    private void recordTime() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                second++;
                if (second >= 60) {
                    second = 0;
                    minute++;
                    if (minute >= 60) {
                        minute = 0;
                        hour++;
                    }
                }
                handler.sendEmptyMessage(1);
            }

        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    // 获得当前时间
    private long getTime() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
//        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//        String time = formatter.format(curDate);
        return new Date().getTime();
    }

    // Activity被销毁的时候 释放资源
    @Override
    protected void onDestroy() {
        // 删除片段
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                File file = new File(mList.get(i));
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    // 来电暂停
    @Override
    protected void onPause() {
        if (mRecorder != null) {
            // 暂停录音
            try {
                pauseRecord();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (mPlayer != null) {
            // 暂停播放
            mPlayer.pause();
            isPausePlay = true;
            pausePlayText.setText("继续播放");
            pausePlay.setClickable(true);
        }
        super.onPause();
    }

    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
