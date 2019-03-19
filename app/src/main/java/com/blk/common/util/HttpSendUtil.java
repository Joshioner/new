package com.blk.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by asus on 2017/10/25.
 */

public class HttpSendUtil {
    //data:post请求的参数
    public static void sendHttpRequest(final String address,final String data ,final HttpCallbackListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //设置长连接
                    connection.setRequestProperty("connection","keep-alive");
                    OutputStream out = connection.getOutputStream();
                    if (data != null){
                        out.write(data.getBytes());
                    }
                    out.flush();
                    out.close();
                    if(connection.getResponseCode() == 200)
                    {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine())!= null)
                        {
                            response.append(line);
                        }
                        if(listener != null)
                        {
                            listener.onFinish(response.toString());
                        }
                        reader.close();
                    }

                }catch (Exception e)
                {
                    if(listener != null)
                    {
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //上传文件
    public static void uploadFile(final String address,final String path ,final HttpCallbackListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //设置长连接
                    connection.setRequestProperty("connection","keep-alive");
                    //application/octet-stream 能上传图片文件等的编码格式
                   // connection.setRequestProperty("Content-Type", "application/octet-stream");
                    //mp4格式是扩展名：.mp4，内容类型：video/mp4
                    connection.setRequestProperty("Content-Type", "video/mp4");
                    OutputStream out = connection.getOutputStream();
                    //设置DataOutputStream
                    DataOutputStream ds = new DataOutputStream(out);

                    //取得文件的FileInputStream
                    FileInputStream fileInputStream = new FileInputStream(new File(path));
                    //读取和写入的信息长度
                    int length = -1;
                    //设置缓冲区
                    byte[] bytes = new byte[1024];
                    while ((length = fileInputStream.read(bytes)) != -1){
                        ds.write(bytes,0,bytes.length);
                        //将缓冲区的数据刷新到outStream上
                        ds.flush();
                    }
                    if (null != fileInputStream){
                        fileInputStream.close();
                    }
                    if (null != ds){
                        ds.close();
                    }
                    if(connection.getResponseCode() == 200)
                    {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine())!= null)
                        {
                            response.append(line);
                        }
                        if(listener != null)
                        {
                            listener.onFinish(response.toString());
                        }
                        reader.close();
                    }

                }catch (Exception e)
                {
                    if(listener != null)
                    {
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
