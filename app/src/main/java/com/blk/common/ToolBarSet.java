package com.blk.common;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by asus on 2018/1/29.
 */

public class ToolBarSet {
    public static void setBar(Window window)
    {
        //设置透明栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
