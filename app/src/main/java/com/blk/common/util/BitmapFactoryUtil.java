package com.blk.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.blk.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static void setNetworkBitmap(Context context,ImageView imageView,String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    InputStream in = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(bitmap);
                    //(同时保存到本地上/healthNews)
                    //创建目录
                    File dir = new File(context.getFilesDir().getAbsolutePath() + "/healthNews/");
                    if (!dir.exists()){
                        dir.mkdir();
                    }
                    String imagePath = url.substring(url.lastIndexOf("/") + 1);
                    File netWorkImage = new File(dir.getAbsolutePath(),imagePath);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(netWorkImage));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                    bos.flush();
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
