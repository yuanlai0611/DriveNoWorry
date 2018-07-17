package com.example.yuanyuanlai.drivenoworry.BackgroundService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.yuanyuanlai.drivenoworry.Bean.DriverStatusBean;
import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import com.example.yuanyuanlai.drivenoworry.RetrofitInterface.PollServiceInterface;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PollingService extends IntentService {

    private static final String TAG = "PollingService";
    private String fileName;
    private boolean isMakeSound;
    private Retrofit retrofit;
    private PollServiceInterface pollServiceInterface;
    private String baseUrl = "http://47.92.48.100:8099/carHealth/";
    private Call<StatusBean<DriverStatusBean>> getDriverStatus;
    private AssetManager assetManager;
    private AssetFileDescriptor assetFileDescriptor;

    @Override
    public void onCreate() {

        super.onCreate();
        isMakeSound = false;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pollServiceInterface = retrofit.create(PollServiceInterface.class);
        getDriverStatus = pollServiceInterface.getDriverStatus("2");
        assetManager = getAssets();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "启动了服务");
        getDriverStatus.enqueue(new Callback<StatusBean<DriverStatusBean>>() {
            @Override
            public void onResponse(Call<StatusBean<DriverStatusBean>> call, Response<StatusBean<DriverStatusBean>> response) {

              boolean isSuccess = response.body().isSuccess();
              if (isSuccess){

                  Intent intent = new Intent("com.example.communication.RECEIVER");
                  intent.putExtra("temperature", response.body().getData().getTemperature());

                  int tempHeartStatus = response.body().getData().getHeartSatus();
                  int tempBloodStatus = response.body().getData().getPressureStatus();
                  int tempAlcoholStatus = response.body().getData().getAlcoholStatus();

                  double tempTemperature = response.body().getData().getTemperature();
                  int tempHeartRate = response.body().getData().getHeartRate();
                  int tempAlcoholConcentration = response.body().getData().getAlcoholConcentration();
                  int tempDiastolicPressure = response.body().getData().getDiastolicPressure();
                  int tempSystolicPressure = response.body().getData().getSystolicPressure();

                  if (tempHeartStatus == 8){
                        playSound("high_heart_rate.wav");
                        isMakeSound = true;
                  }else if (tempHeartStatus == 11){
                        playSound("low_heart_rate.wav");

                        isMakeSound = true;
                  }

                  if (tempBloodStatus == 12 && !isMakeSound){
                      playSound("high_blood_pressure.wav");
                      isMakeSound = true;
                  }else if (tempBloodStatus ==13){
                      playSound("low_blood_pressure.wav");
                      isMakeSound = true;
                  }

                  if (tempAlcoholStatus == 17&& !isMakeSound){
                      playSound("alcohol_driving.wav");
                      isMakeSound = true;
                  }

                  Log.d(TAG, ""+tempHeartStatus);
                  Log.d(TAG, ""+tempBloodStatus);
                  Log.d(TAG, ""+tempAlcoholStatus);

                  intent.putExtra("tempTemperature", tempTemperature);
                  intent.putExtra("tempHeartRate", tempHeartRate);
                  intent.putExtra("tempAlcoholConcentration", tempAlcoholConcentration);
                  intent.putExtra("tempDiastolicPressure", tempDiastolicPressure);
                  intent.putExtra("tempSystolicPressure", tempSystolicPressure);

                  sendBroadcast(intent);


              }else {
                  Log.d(TAG, "获取身体信息失败");
              }

            }

            @Override
            public void onFailure(Call<StatusBean<DriverStatusBean>> call, Throwable t) {
                Log.d(TAG, "获取身体信息失败");
            }
        });


    }

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context, PollingService.class);
        return intent;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public PollingService() {
        super(TAG);
    }

    private void playSound(String fileName){

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.release();
                    return false;
                }
            });

        try {

            assetFileDescriptor = assetManager.openFd(fileName);

            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
