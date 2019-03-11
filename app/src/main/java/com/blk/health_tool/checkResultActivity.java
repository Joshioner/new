package com.blk.health_tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.blk.R;
import com.blk.common.util.FileUtil;
import com.blk.common.util.WeiboDialogUtils;

import java.io.File;
import java.util.regex.Pattern;

public class checkResultActivity extends AppCompatActivity {
    private Dialog weiboDialogUtils;                         //加载框
    private static final int REQUEST_CODE_CAMERA = 102;        //拍照的请求码
    private Dialog mWeiboDialog;
    private TextView checkResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);
        checkResult = (TextView) findViewById(R.id.result);
               /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();

        int data=bundle.getInt("data");


        //data ==0 || data == 1 表示是通过拍照识别，data == 2 表示点击病历查看具体病历详情
        if(data==1)
        {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(this,"识别中");
            Intent intent = new Intent(checkResultActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(checkResultActivity.this, "加载中...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
                }
            }).start();

        } else
            finish();
    }

    private void recGeneral(final String filePath) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (Word word : result.getWordList()) {
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                final String content = sb.toString().replace("国药准字", "国药准字#");
                final String pattern = ".*国药准字.*";
                String resultData = readTxtContent(pattern,content);
                checkResult.setText(resultData);
            }

            @Override
            public void onError(OCRError error) {
                //infoTextView.setText(error.getMessage());
            }
        });
    }

    public String readTxtContent(String pattern1, String content) {
        String data = "";
        String[] goal = content.split("\n");
        String pattern2 = ".*#.*";
        for (int i = 0; i < goal.length; i++) {
            //第一步:先判断句子里面是否包含关键词国药准字
            boolean isMatch1 = Pattern.matches(pattern1, goal[i]);
            //包含关键词
            if (isMatch1) {
                //第二步：再次判断句子里面是否包含界定符（比如：xxxx医院这个界定符不好定）
                //句子包含界定符
                if (Pattern.matches(pattern2, goal[i])) {
                    char[] first = goal[i].toCharArray();
                    int q = 0;
                    for (int m = 0; m < first.length; m++) {
                        //从界定符位置开始读取
                        if (first[m] == '#') {
                            for (q = m + 1; q < first.length; q++) {
                                data = data + first[q];
                            }
                            break;
                        }

                    }
                    break;
                }

            }
        }
        return data;
    }



}
