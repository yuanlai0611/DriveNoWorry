package com.example.yuanyuanlai.drivenoworry.Utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUtil {

    private static OkhttpUtil sOkhttpUtil;
    private OkHttpClient okHttpClient;

    public OkhttpUtil() {
        okHttpClient = new OkHttpClient();
    }

    public synchronized static OkhttpUtil getInstance(){
        if (sOkhttpUtil == null){
            sOkhttpUtil = new OkhttpUtil();
        }
        return sOkhttpUtil;
    }

    public void getDriverStatus(String id, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("userId", id)
                .build();
        Request request = new Request.Builder()
                .url("http://47.92.48.100:8099/carHealth/api/record/dogetLatestRecord")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }
}
