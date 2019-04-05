package com.blk.medical_record.util;


import android.content.Context;
import android.os.Looper;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

/**
 * 单例模式，初始化accessToken
 */
public class GetAccessTokenUtil {
    private static GetAccessTokenUtil instance;
    private  Context context;
    private GetAccessTokenUtil(Context context){
          this.context = context;
    }

    public static GetAccessTokenUtil getInstance(Context context){
        if (instance == null){
            synchronized (GetAccessTokenUtil.class){
                if (instance == null){
                    instance = new GetAccessTokenUtil(context);
                }
            }
        }
        return instance;
    }

    public  void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, context);
    }

    private void alertText(String title, String message) {
         android.support.v7.app.AlertDialog.Builder alertDialog  = new android.support.v7.app.AlertDialog.Builder(context);
        boolean isNeedLoop = false;
        if (Looper.myLooper() == null) {
            Looper.prepare();
            isNeedLoop = true;
        }
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
        if (isNeedLoop) {
            Looper.loop();
        }
    }
}
