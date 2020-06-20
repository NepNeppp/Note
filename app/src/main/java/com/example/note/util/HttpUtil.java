package com.example.note.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://NepNeppp.com"+msg)//TODO
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
