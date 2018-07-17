package com.example.yuanyuanlai.drivenoworry.Lock;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.yuanyuanlai.drivenoworry.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{

    private RelativeLayout mMoveView;
    private UnderView mUnderView;
    private float mStartX;
    private float mWidth;
    private boolean emergency_state;
    private SharedPreferences sharedPreferences;
    private ImageView callButton;
    private ImageView messageButton;
    private TextView textViewTime;
    private TextView textViewDate;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String weeks[] = {"天","一","二","三","四","五","六"};
    private String week;
    private static final int RC_CALL = 1;
    private static final int RC_MESSAGE = 2;
    private String phone, message;
    private Location location;
    private LocationManager locationManager;
    private String longitude, latitude;
    private ImageView lockBackgroundImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("emergency", MODE_PRIVATE);
        emergency_state = sharedPreferences.getBoolean("emergency_state", false);
        phone = sharedPreferences.getString("emergency_phone_number", "");
        message = sharedPreferences.getString("emergency_message", "");

        initStatusBar();
        registerTimeReceiver();
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        initView();
        setOnClickListener();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeRefreshReceiver);
    }

    private void initView() {
        textViewTime = (TextView) findViewById(R.id.time);
        textViewDate = (TextView) findViewById(R.id.date);
        mMoveView = (RelativeLayout) findViewById(R.id.mMoveView);
        lockBackgroundImageView = (ImageView) findViewById(R.id.lock_background);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this)
                .load(R.drawable.lock_background)
                .apply(options)
                .into(lockBackgroundImageView);
        mUnderView = (UnderView) findViewById(R.id.mUnderView);
        mUnderView.setMoveView(mMoveView);
        mUnderView.setSlideListener(new UnderView.OnSlideFinishListener() {
            @Override
            public void onSlideFinish() {
                finish();
            }
        });
        callButton = (ImageView) findViewById(R.id.emergency_call);
        messageButton = (ImageView) findViewById(R.id.emergency_text);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        initTime();
        initDate();
    }

    private void setOnClickListener(){

        callButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.emergency_call:
                if (!emergency_state){
                  Toast.makeText(LockScreenActivity.this, "你没有开启紧急呼叫功能", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()){
                    Toast.makeText(LockScreenActivity.this, "你还没有设置紧急电话", Toast.LENGTH_SHORT).show();
                }else {
                    call();
                }
                break;
            case R.id.emergency_text:
                if (!emergency_state){
                    Toast.makeText(LockScreenActivity.this, "你没有开启紧急呼叫功能", Toast.LENGTH_SHORT).show();
                }else if (!phone.isEmpty()&&!message.isEmpty()){
                    sendMessage();
                }else if (!phone.isEmpty()&&message.isEmpty()){
                    Toast.makeText(LockScreenActivity.this, "你还没有设置紧急电话", Toast.LENGTH_SHORT).show();
                }else if (phone.isEmpty()&&!message.isEmpty()){
                    Toast.makeText(LockScreenActivity.this, "你还没有设置紧急短信", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                 break;
        }

    }

    private void initTime(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        textViewTime.setText(simpleDateFormat.format(new Date()));
    }

    private void initDate(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        week = month+"月"+dayOfMonth+"日星期"+weeks[dayOfWeek-1];
        Log.d("LockScreenActivity","---->"+dayOfWeek);
        textViewDate.setText(week);
    }

    private void initStatusBar(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            View view = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            view.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);

        }

    }

    private void registerTimeReceiver(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeRefreshReceiver,intentFilter);

    }

    private final BroadcastReceiver timeRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)){

                initTime();

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if(requestCode==RC_CALL){

            Toast.makeText(LockScreenActivity.this,"打电话的权限同意",Toast.LENGTH_SHORT).show();

        }else if(requestCode==RC_MESSAGE){

            Toast.makeText(LockScreenActivity.this,"发短信的权限同意",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if(requestCode==RC_CALL){

            Toast.makeText(LockScreenActivity.this,"打电话的权限被拒绝",Toast.LENGTH_SHORT).show();

        }else if(requestCode==RC_MESSAGE){

            Toast.makeText(LockScreenActivity.this,"发短信的权限被拒绝",Toast.LENGTH_SHORT).show();

        }

        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){

            new AppSettingsDialog.Builder(this).build().show();

        }

    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_CALL)
    private void call(){

       if (EasyPermissions.hasPermissions(LockScreenActivity.this, Manifest.permission.CALL_PHONE)){

           Intent intent = new Intent(Intent.ACTION_CALL);
           Uri uri = Uri.parse("tel:"+phone);
           intent.setData(uri);
           startActivity(intent);

       }else{

           PermissionRequest permissionRequest = new PermissionRequest.Builder(LockScreenActivity.this,RC_CALL,Manifest.permission.CALL_PHONE).build();
           EasyPermissions.requestPermissions(permissionRequest);

       }

    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_MESSAGE)
    private void sendMessage(){

        if (EasyPermissions.hasPermissions(LockScreenActivity.this, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){

            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            //拆分短信内容（手机短信长度限制）,貌似长度限制为140个字符,就是
            //只能发送70个汉字,多了要拆分成多条短信发送
            //第四五个参数,如果没有需要监听发送状态与接收状态的话可以写null

            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Log.d("", ""+location.getLongitude()+" "+location.getLatitude());

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

            location = getLocation(locationManager);
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());


            List<String> divideContents = smsManager.divideMessage(message+"我在经度："+longitude+" 维度："+latitude);

            for (String text : divideContents) {
                smsManager.sendTextMessage(phone, null, text, null, null);
            }

        }else{

            PermissionRequest permissionRequest = new PermissionRequest.Builder(LockScreenActivity.this,RC_MESSAGE,Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).build();
            EasyPermissions.requestPermissions(permissionRequest);

        }

    }

    @SuppressLint("MissingPermission")
    private Location getLocation(LocationManager locationManager){

        Location result = null;
        if (locationManager != null){
            Log.d("LockScreenActivity","--->获取到locationManager");
            result = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (result != null){
                return result;
            }else {
                Log.d("LockScreenActivity","--->通过网络定位");
                    result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                return result;
            }
        }
        return result;
    }


}
