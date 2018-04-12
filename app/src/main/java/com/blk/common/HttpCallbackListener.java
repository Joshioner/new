package com.blk.common;

/**
 * Created by asus on 2017/10/25.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
