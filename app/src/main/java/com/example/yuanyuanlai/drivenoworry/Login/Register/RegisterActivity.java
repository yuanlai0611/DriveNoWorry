package com.example.yuanyuanlai.drivenoworry.Login.Register;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.print.PrinterId;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.yuanyuanlai.drivenoworry.Base.BaseActivity;
import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.RetrofitInterface.RegisterInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton imageButtonBackToLogin;
    private CheckBox agreeCheckBox;
    private EditText phoneEditText, captureEditText, passWordEditText;
    private ValueAnimator valueAnimator;
    private Button getCaptureButton, registerButton;
    private Retrofit retrofit;
    private String baseUrl = "http://47.92.48.100:8099/carHealth/";
    private String tempPhone, tempCapture, tempPassWord;
    private RegisterInterface registerinterface;
    private Call<StatusBean> getCapture;
    private Call<StatusBean<Integer>> register;

    @Override
    protected void onMultiClick(View view) {

    }

    @Override
    protected void setListener() {

        imageButtonBackToLogin.setOnClickListener(this);
        getCaptureButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        valueAnimator = ValueAnimator.ofInt(0, 60);
        valueAnimator.setDuration(60000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = 60 - (int)animation.getAnimatedValue();
                if (currentValue == 0){
                    getCaptureButton.setTextColor(getResources().getColor(R.color.colorCaptureEnable));
                    getCaptureButton.setText("获取验证码");
                    getCaptureButton.setEnabled(true);
                }else {
                    getCaptureButton.setText(currentValue+"秒后重试");
                }
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        registerinterface = retrofit.create(RegisterInterface.class);


    }

    @Override
    protected void setData() {

    }

    @Override
    protected void initViews() {

        initStatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        imageButtonBackToLogin = (ImageButton) findViewById(R.id.back_to_login);
        getCaptureButton = (Button)findViewById(R.id.get_capture);
        phoneEditText = (EditText)findViewById(R.id.phone);
        captureEditText = (EditText)findViewById(R.id.capture);
        passWordEditText = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.register_button);
        agreeCheckBox = (CheckBox)findViewById(R.id.agree_checkBox);

    }

    @Override
    protected void setContentView() {

        setContentView(R.layout.activity_register);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){

           case R.id.back_to_login:
               finish();
               break;
           case R.id.get_capture:
               tempPhone = phoneEditText.getText().toString();
               if (tempPhone.isEmpty()){
                   Toast.makeText(RegisterActivity.this, "请填入电话号码", Toast.LENGTH_SHORT).show();
               }else {
                   valueAnimator.start();
                   getCaptureButton.setTextColor(getResources().getColor(R.color.colorCaptureDisable));
                   getCaptureButton.setEnabled(false);
                   getCapture = registerinterface.getCapture(tempPhone);
                   getCapture.enqueue(new Callback<StatusBean>() {
                       @Override
                       public void onResponse(Call<StatusBean> call, Response<StatusBean> response) {

                           boolean isSuccess = response.body().isSuccess();
                           if (isSuccess){
                               Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                           }

                           Log.d("RegisterActivity", response.body().toString());

                       }

                       @Override
                       public void onFailure(Call<StatusBean> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                       }
                   });
               }


               break;
           case R.id.register_button:

               tempPhone = phoneEditText.getText().toString();
               tempCapture = captureEditText.getText().toString();
               tempPassWord = passWordEditText.getText().toString();
               if (!tempPhone.isEmpty()&&!tempCapture.isEmpty()&&!tempPassWord.isEmpty()){
                   register = registerinterface.registUser(tempPhone, tempCapture, tempPassWord);
                   register.enqueue(new Callback<StatusBean<Integer>>() {
                       @Override
                       public void onResponse(Call<StatusBean<Integer>> call, Response<StatusBean<Integer>> response) {

                           boolean isSuccess = response.body().isSuccess();

                           if (isSuccess){

                               SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               int userNumber = response.body().getData();
                               editor.putInt("userNumber", userNumber);
                               editor.commit();
                               finish();

                           }else {
                               Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                           }

                       }

                       @Override
                       public void onFailure(Call<StatusBean<Integer>> call, Throwable t) {
                               Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                       }
                   });
               }else {
                    Toast.makeText(RegisterActivity.this, "把信息填取完整", Toast.LENGTH_SHORT).show();
               }

               break;
           default:
               break;

       }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        valueAnimator.cancel();
    }
}
