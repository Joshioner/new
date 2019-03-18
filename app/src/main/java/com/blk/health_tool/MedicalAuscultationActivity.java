package com.blk.health_tool;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.yuyin_fanyi.DictationResult;
import com.blk.common.yuyin_fanyi.HttpCallBack;
import com.blk.common.yuyin_fanyi.RequestUtils;
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

import java.util.List;

public class MedicalAuscultationActivity extends Activity implements View.OnClickListener{
    private ImageView auscultation_back;
    private EditText editText;
    private RelativeLayout btnStartSpeak;
  //  private LinearLayout transfer_english;
    private static final String APP_ID = "20170913000082585";
    private static final String SECURITY_KEY = "Y0brcHKoldDb9MobxcXU";
    private String from="auto";
    private String to="auto";

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
      //  transfer_english = (LinearLayout)findViewById(R.id.transfer_english);

    }
    private void initEvent() {
        //返回
        auscultation_back.setOnClickListener(this);
        //开始语音识别
        btnStartSpeak.setOnClickListener(this);
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
//            case R.id.transfer_english:
//                final String request = editText.getText().toString();
//                RequestUtils requestUtils=new RequestUtils();
//                if (!request.isEmpty()){
//                    try {
//                        requestUtils.translate(request, from, to, new HttpCallBack() {
//                            @Override
//                            public void onSuccess(String result) {
//                                editText.setText(result);
//                            }
//                            @Override
//                            public void onFailure(String exception) {
//                                editText.setText(exception);
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    Toast.makeText(medical_auscultation.this,"请输入要翻译的内容!",Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.btnStartSpeak:
                // 有交互动画的语音识别器
                iatDialog = new RecognizerDialog(MedicalAuscultationActivity.this, mInitListener);
                //1.创建SpeechRecognizer对象(没有交互动画的语音识别器)，第2个参数：本地听写时传InitListener
                //mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);
//                // 2.设置听写参数
//                mIat.setParameter(SpeechConstant.DOMAIN, "iat"); // domain:域名
//                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//                mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); // mandarin:普通话
                //保存音频文件到本地（有需要的话）   仅支持pcm和wav
                //  mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/mIat.wav");
                //mIat.startListening(mRecognizerListener);//

                iatDialog.setListener(new RecognizerDialogListener() {
                    String resultJson = "[";           //放置在外边做类的变量则报错，会造成json格式不对（？）

                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                        System.out.println("-----------------   onResult   -----------------");
                        if (!isLast) {
                            resultJson += recognizerResult.getResultString() + ",";
                        } else {
                            resultJson += recognizerResult.getResultString() + "]";
                        }

                        if (isLast) {
                            //解析语音识别后返回的json格式的结果
                            Gson gson = new Gson();
                            List<DictationResult> resultList = gson.fromJson(resultJson,
                                    new TypeToken<List<DictationResult>>() {
                                    }.getType());
                            String result = "";
                            for (int i = 0; i < resultList.size() - 1; i++) {
                                result += resultList.get(i).toString();
                            }
                            editText.setText(result);
                            //获取焦点
                            editText.requestFocus();
                            //将光标定位到文字最后，以便修改
                            editText.setSelection(result.length());
                        }
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
        }
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
