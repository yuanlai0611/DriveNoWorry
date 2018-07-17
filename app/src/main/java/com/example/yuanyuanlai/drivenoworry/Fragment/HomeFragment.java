package com.example.yuanyuanlai.drivenoworry.Fragment;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.yuanyuanlai.drivenoworry.BackgroundService.PollingReceiver;
import com.example.yuanyuanlai.drivenoworry.Base.BaseFragment;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.Utils.PollingUtils;
import com.example.yuanyuanlai.drivenoworry.View.SpeedView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout detectionSwitchLinearLayout, sleepDetectionSwitchLinearLayout, alcoholDetectionSwitchLinearLayout;
    private TextView textViewTemperature;
    private boolean isSwitchOn, isSleepSwitchOn, isAlcoholSwitchOn;
    private MsgReceiver msgReceiver;
    private TextView detectionNameTextView, detectionSignTextView, sleepDetectionNameTextView, sleepDetectionSignTextView,
    alcoholDetectionNameTextView, alcoholDetectionSignTextView;
    private SpeedView detectionSpeedView, sleepDetectionSpeedView, alcoholDetectionSpeedView;
    private ValueAnimator detectionValueAnimator, sleepDetectionValueAnimator, alcoholDetectionValueAnimator;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView temperatureDataTextView, heartDataTextView, highBloodDataTextView, lowBloodDataTextView, alcoholDataTextView;
    private TextView heartUpdateTextView, bloodUpdateTextView, alcoholUpdateTextView;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }



    @Override
    public void onResume() {
        super.onResume();

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.RECEIVER");
        getActivity().registerReceiver(msgReceiver, intentFilter);

    }

//
    @Override
    protected void initData() {

      detectionValueAnimator = ValueAnimator.ofInt(40, 60);
      detectionValueAnimator.setDuration(1000);
      detectionValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
      detectionValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
      detectionValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      detectionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
              int tempProgress = (int)animation.getAnimatedValue();
              detectionSpeedView.setProgress(tempProgress);
          }
      });

      sleepDetectionValueAnimator = ValueAnimator.ofInt(40, 60);
      sleepDetectionValueAnimator.setDuration(1000);
      sleepDetectionValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
      sleepDetectionValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
      sleepDetectionValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      sleepDetectionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
              int tempProgress = (int)animation.getAnimatedValue();
              sleepDetectionSpeedView.setProgress(tempProgress);
          }
      });

      alcoholDetectionValueAnimator = ValueAnimator.ofInt(40, 60);
      alcoholDetectionValueAnimator.setDuration(1000);
      alcoholDetectionValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
      alcoholDetectionValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
      alcoholDetectionValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      alcoholDetectionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
              int tempProgress = (int)animation.getAnimatedValue();
              alcoholDetectionSpeedView.setProgress(tempProgress);
          }
      });

      sharedPreferences = getActivity().getSharedPreferences("switch_state", Context.MODE_PRIVATE);
      editor = sharedPreferences.edit();
      isSwitchOn = sharedPreferences.getBoolean("isSwitchOn", false);
      isSleepSwitchOn = sharedPreferences.getBoolean("isSleepSwitchOn", false);
      isAlcoholSwitchOn = sharedPreferences.getBoolean("isAlcoholSwitchOn", false);
      if (isSwitchOn){
          PollingUtils.startPollingService(getActivity(), 1, PollingReceiver.class, PollingUtils.ACTION);
      }

      initSwitch();

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setListener() {
        detectionSwitchLinearLayout.setOnClickListener(this);
        sleepDetectionSwitchLinearLayout.setOnClickListener(this);
        alcoholDetectionSwitchLinearLayout.setOnClickListener(this);
    }

    @Override
    protected void initView(View view) {

        detectionSwitchLinearLayout = (LinearLayout)view.findViewById(R.id.linearLayout_detection_switch);
        textViewTemperature = (TextView)view.findViewById(R.id.body_temperature);
        detectionNameTextView = (TextView)view.findViewById(R.id.detection_switch_name);
        detectionSignTextView = (TextView)view.findViewById(R.id.detection_sign);
        sleepDetectionSwitchLinearLayout = (LinearLayout)view.findViewById(R.id.sleep_detection_switch_linearLayout);
        sleepDetectionNameTextView = (TextView)view.findViewById(R.id.sleep_detection_switch_name);
        sleepDetectionSignTextView = (TextView)view.findViewById(R.id.sleep_detection_sign);
        alcoholDetectionSwitchLinearLayout = (LinearLayout)view.findViewById(R.id.alcohol_detection_switch_linearLayout);
        alcoholDetectionNameTextView = (TextView)view.findViewById(R.id.alcohol_detection_switch_name);
        alcoholDetectionSignTextView = (TextView)view.findViewById(R.id.alcohol_detection_sign);
        detectionSpeedView = (SpeedView)view.findViewById(R.id.detection_speedView);
        sleepDetectionSpeedView = (SpeedView)view.findViewById(R.id.sleep_detection_speedView);
        alcoholDetectionSpeedView = (SpeedView)view.findViewById(R.id.alcohol_detection_speedView);
        temperatureDataTextView = (TextView)view.findViewById(R.id.body_temperature);
        heartDataTextView = (TextView)view.findViewById(R.id.heart_data);
        highBloodDataTextView = (TextView)view.findViewById(R.id.high_blood_pressure_data);
        lowBloodDataTextView = (TextView)view.findViewById(R.id.low_blood_pressure_data);
        alcoholDataTextView = (TextView)view.findViewById(R.id.alcohol_data);
        heartUpdateTextView = (TextView)view.findViewById(R.id.heart_update_time);
        bloodUpdateTextView = (TextView)view.findViewById(R.id.blood_pressure_update_time);
        alcoholUpdateTextView = (TextView)view.findViewById(R.id.alcohol_update_time);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayout_detection_switch:

                if (isSwitchOn == true){

                    detectionValueAnimator.pause();
                    detectionSwitchLinearLayout.setSelected(false);
                    detectionSignTextView.setSelected(false);
                    detectionNameTextView.setSelected(false);
                    detectionSignTextView.setText("OFF");
                    PollingUtils.stopPollingService(getActivity(), PollingReceiver.class, PollingUtils.ACTION);
                    isSwitchOn = false;
                    editor.putBoolean("isSwitchOn", false);



                }else {

                    detectionValueAnimator.start();
                    detectionSwitchLinearLayout.setSelected(true);
                    detectionSignTextView.setSelected(true);
                    detectionNameTextView.setSelected(true);
                    detectionSignTextView.setText("ON");
                    PollingUtils.startPollingService(getActivity(), 1, PollingReceiver.class, PollingUtils.ACTION);
                    isSwitchOn = true;
                    editor.putBoolean("isSwitchOn", true);

                }

                editor.commit();
                break;
            case R.id.sleep_detection_switch_linearLayout:

                if (isSleepSwitchOn == true){

                    sleepDetectionValueAnimator.pause();
                    sleepDetectionSwitchLinearLayout.setSelected(false);
                    sleepDetectionSignTextView.setSelected(false);
                    sleepDetectionNameTextView.setSelected(false);
                    sleepDetectionSignTextView.setText("OFF");
                    isSleepSwitchOn = false;
                    editor.putBoolean("isSleepSwitchOn", false);

                }else {

                    sleepDetectionValueAnimator.start();
                    sleepDetectionSwitchLinearLayout.setSelected(true);
                    sleepDetectionSignTextView.setSelected(true);
                    sleepDetectionNameTextView.setSelected(true);
                    sleepDetectionSignTextView.setText("ON");
                    isSleepSwitchOn = true;
                    editor.putBoolean("isSleepSwitchOn", true);

                }
                editor.commit();

                break;
            case R.id.alcohol_detection_switch_linearLayout:

                if (isAlcoholSwitchOn == true){

                    alcoholDetectionValueAnimator.pause();
                    alcoholDetectionSwitchLinearLayout.setSelected(false);
                    alcoholDetectionSignTextView.setSelected(false);
                    alcoholDetectionNameTextView.setSelected(false);
                    alcoholDetectionSignTextView.setText("OFF");
                    alcoholDataTextView.setText("--");
                    isAlcoholSwitchOn = false;
                    editor.putBoolean("isAlcoholSwitchOn", false);

                }else {

                    alcoholDetectionValueAnimator.start();
                    alcoholDetectionSwitchLinearLayout.setSelected(true);
                    alcoholDetectionSignTextView.setSelected(true);
                    alcoholDetectionNameTextView.setSelected(true);
                    alcoholDetectionSignTextView.setText("ON");
                    isAlcoholSwitchOn = true;
                    editor.putBoolean("isAlcoholSwitchOn", true);

                }
                editor.commit();
                break;

            default:
                break;
        }
    }



    private void initSwitch(){

        if (isSwitchOn == false){

            detectionValueAnimator.pause();
            detectionSwitchLinearLayout.setSelected(false);
            detectionSignTextView.setSelected(false);
            detectionNameTextView.setSelected(false);
            detectionSignTextView.setText("OFF");

        }else {

            detectionValueAnimator.start();
            detectionSwitchLinearLayout.setSelected(true);
            detectionSignTextView.setSelected(true);
            detectionNameTextView.setSelected(true);
            detectionSignTextView.setText("ON");

        }

        if (isSleepSwitchOn == false){

            sleepDetectionValueAnimator.pause();
            sleepDetectionSwitchLinearLayout.setSelected(false);
            sleepDetectionSignTextView.setSelected(false);
            sleepDetectionNameTextView.setSelected(false);
            sleepDetectionSignTextView.setText("OFF");

        }else {

            sleepDetectionValueAnimator.start();
            sleepDetectionSwitchLinearLayout.setSelected(true);
            sleepDetectionSignTextView.setSelected(true);
            sleepDetectionNameTextView.setSelected(true);
            sleepDetectionSignTextView.setText("ON");

        }

        if (isAlcoholSwitchOn == false){

            alcoholDetectionValueAnimator.pause();
            alcoholDetectionSwitchLinearLayout.setSelected(false);
            alcoholDetectionSignTextView.setSelected(false);
            alcoholDetectionNameTextView.setSelected(false);
            alcoholDetectionSignTextView.setText("OFF");

        }else {

            alcoholDetectionValueAnimator.start();
            alcoholDetectionSwitchLinearLayout.setSelected(true);
            alcoholDetectionSignTextView.setSelected(true);
            alcoholDetectionNameTextView.setSelected(true);
            alcoholDetectionSignTextView.setText("ON");

        }

    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //拿到进度，更新UI
            double tempTemperature = intent.getDoubleExtra("tempTemperature", 0);
            int tempHeartRate = intent.getIntExtra("tempHeartRate", 0);
            int tempAlcoholConcentration = intent.getIntExtra("tempAlcoholConcentration", 0);
            int tempDiastolicPressure = intent.getIntExtra("tempDiastolicPressure", 0);
            int tempSystolicPressure = intent.getIntExtra("tempSystolicPressure", 0);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            String currentTime = simpleDateFormat.format(date);

            if (isSwitchOn){

                temperatureDataTextView.setText(""+tempTemperature);
                heartDataTextView.setText(""+tempHeartRate);
                highBloodDataTextView.setText(""+tempSystolicPressure);
                lowBloodDataTextView.setText(""+tempDiastolicPressure);
                heartUpdateTextView.setText(currentTime);
                bloodUpdateTextView.setText(currentTime);

            }

            if (isAlcoholSwitchOn){
                alcoholDataTextView.setText(""+tempAlcoholConcentration);
                alcoholUpdateTextView.setText(currentTime);
            }

        }

    }
}
