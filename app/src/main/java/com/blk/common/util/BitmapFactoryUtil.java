package com.blk.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blk.R;

public class BitmapFactoryUtil {

    //根据图片路径获取bitmap
    public static Bitmap getBitmap(String imagePath){
        return BitmapFactory.decodeFile(imagePath);
    }

    /**
     *  根据 资源位置 以及id 获取bitmap
     * @param context
     * @param id
     * @return
     */
    public static Bitmap getBitmapByRes(Context context,int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }
}
