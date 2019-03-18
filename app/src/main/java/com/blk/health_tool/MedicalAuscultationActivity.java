package com.blk.health_tool;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.yuyin_fanyi.DictationResult;
import com.blk.common.yuyin_fanyi.HttpCallBack;
import com.blk.common.yuyin_fanyi.RequestUtils;
import com.blk.health_tool.util.ResultBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MedicalAuscultationActivity extends Activity implements View.OnClickListener{
    private ImageView auscultation_back;
    private EditText editText;
    private RelativeLayout btnStartSpeak;
    private TextView save;
  //  private LinearLayout transfer_english;
    private static final String APP_ID = "20170913000082585";
    private static final String SECURITY_KEY = "Y0brcHKoldDb9MobxcXU";
    private String from="auto";
    private String to="auto";

    private String path;
    //有动画效果
    private RecognizerDialog iatDialog;
    //无动画效果
    //private SpeechRecognizer mIat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_auscultation);
        // 语音配置对象初始化(如果只使用 语音识别 或 语音合成 时都得先初始化这个)
        SpeechUtility.createUtility(MedicalAuscultationActivity.this, SpeechConstant.APPID + "=578f1af7");
        Window window = getWindow();
        ToolBarSet.setBar(window);

        initView();
        initEvent();
    }
    private void initView() {
        auscultation_back = (ImageView)findViewById(R.id.auscultation_back);
        editText = (EditText) findViewById(R.id.editText);
        btnStartSpeak = (RelativeLayout) findViewById(R.id.btnStartSpeak);
        save = (TextView) findViewById(R.id.save);
      //  transfer_english = (LinearLayout)findViewById(R.id.transfer_english);

    }
    private void initEvent() {
        //返回
        auscultation_back.setOnClickListener(this);
        //开始语音识别
        btnStartSpeak.setOnClickListener(this);
        save.setOnClickListener(this);
        //翻译
    //    transfer_english.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auscultation_back:
                Intent intent = new Intent(this, AuscultationDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data", 1);
                intent.putExtras(bundle);
                this.finish();
                startActivity(intent);
                break;
            case R.id.btnStartSpeak:
                // 有交互动画的语音识别器
                iatDialog = new RecognizerDialog(MedicalAuscultationActivity.this, mInitListener);

                 path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/Davie/" + getPackageName() + "/Record";
                iatDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, path + "/" + getTime() + ".wav");    //识别完成后在本地保存一个音频文
                // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
                iatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
                // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
                iatDialog.setParameter(SpeechConstant.VAD_EOS, "4000");
                // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
                // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
                //这句话必须加,否则音频文件无法播放
                iatDialog.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");

                iatDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                        if (isLast) {
                            return;
                        }
                        Gson mGson = new Gson();
                        ResultBean resultBean = mGson.fromJson(recognizerResult.getResultString(), ResultBean.class);
                        List<ResultBean.WsBean> ws = resultBean.getWs();
                        String result = "";
                        for (int i = 0; i < ws.size(); i++) {
                            List<ResultBean.WsBean.CwBean> cw = ws.get(i).getCw();
                            for (int j = 0; j < cw.size(); j++) {
                                result += cw.get(j).getW();
                            }
                        }
                            editText.append(result);
                           // editText.setText(result);
                            //获取焦点
                            editText.requestFocus();
                            //将光标定位到文字最后，以便修改
                            editText.setSelection(result.length());
//                        }
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        //自动生成的方法存根
                        speechError.getPlainDescription(true);
                    }
                });
                //开始听写，需将sdk中的assets文件下的文件夹拷入项目的assets文件夹下（没有的话自己新建）
                iatDialog.show();
                break;
            case R.id.save:
                /* 获得MeidaPlayer对象 */
                MediaPlayer mediaPlayer = new MediaPlayer();

                /* 得到文件路径 *//* 注：文件存放在SD卡的根目录，一定要进行prepare()方法，使硬件进行准备 */
                File file = new File(path);

                try{
                    /* 为MediaPlayer 设置数据源 */
                    mediaPlayer.setDataSource(file.getAbsolutePath());

                    /* 准备 */
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                break;
        }
    }



    // 获得当前时间
    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        return time;
    }


    public static final String TAG = "MainActivity";
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(MedicalAuscultationActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}
