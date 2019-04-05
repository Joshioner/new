package com.blk.common.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by asus on 2017/10/25.
 */

public class HttpRequestUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //设置长连接
                    connection.setRequestProperty("connection","keep-alive");
                    connection.setRequestProperty("Content-type","application/json");
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
