package com.blk.common.util;

import java.io.BufferedReader;
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
                    OutputStream out = connection.getOutputStream();
                    out.write(data.getBytes());
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
}
