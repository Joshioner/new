package com.blk.health_tool.util;

import android.util.Log;

import com.blk.common.util.HttpCallbackListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class AndroidUploadFile {

    /**
     * @param filePath  文件地址
     * @param urlServer 服务器的地址
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */

    public static void uploadFile(final String filePath, final String urlServer, final HttpCallbackListener listener) throws ClientProtocolException, IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用HttpClient

                HttpClient httpClient = new DefaultHttpClient();

                //必须设置请求的协议

                httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                //设置http post请求

                HttpPost httpPost = new HttpPost(urlServer);

                //构建上传的文件的实体

                MultipartEntity multipartEntity = new MultipartEntity();

                //构建文件的File的对象

                File file = new File(filePath);

                //添加文件的

                ContentBody contentBody = new FileBody(file);

                multipartEntity.addPart("uploadFile", contentBody);//<input type="file" name="file">

                //把请求实体设置到HttpPost

                httpPost.setEntity(multipartEntity);

                //执行这个请求

                HttpResponse response = null;
                try {
                    response = httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //通过响应取到状态行

                StatusLine statusLine = response.getStatusLine();

                //通过状态码去判断

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                    HttpEntity entity = response.getEntity();

                    String result = null;
                    try {
                        result = EntityUtils.toString(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("TAG", "*******" + result);
                    if (listener != null){
                        listener.onFinish(result);
                    }

                } else {

                    Log.i("TAG", "请求出了问题");

                }

            }
        }).start();

    }

}