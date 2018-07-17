package com.example.yuanyuanlai.drivenoworry.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuanyuanlai.drivenoworry.Activity.MainActivity;
import com.example.yuanyuanlai.drivenoworry.Base.BaseActivity;
import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import com.example.yuanyuanlai.drivenoworry.Bean.UserInfoBean;
import com.example.yuanyuanlai.drivenoworry.Login.Register.RegisterActivity;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.RetrofitInterface.LoginInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private Button buttonGoToRegister, loginButton;
    private EditText phoneEditText, passwordEditText;
    private String baseUrl = "http://47.92.48.100:8099/carHealth/";
    private Retrofit retrofit;
    private LoginInterface loginInterface;
    private Call<StatusBean<UserInfoBean>> login;

    @Override
    protected void setListener() {
        buttonGoToRegister.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginInterface = retrofit.create(LoginInterface.class);

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void initViews() {

        initStatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        buttonGoToRegister = (Button)findViewById(R.id.go_to_register);
        loginButton = (Button)findViewById(R.id.login_button);
        phoneEditText = (EditText)findViewById(R.id.phone);
        passwordEditText = (EditText)findViewById(R.id.password);

    }

    @Override
    protected void setContentView() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        boolean isBind = sharedPreferences.getBoolean("isBind", false);
        if (isLogin&&isBind){
            Intent intent = MainActivity.newIntent(this);
            startActivity(intent);
            finish();
        }else if (isLogin&&!isBind){
            Intent intent = BindActivity.newIntent(this);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onMultiClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_to_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!phone.isEmpty()&&!password.isEmpty()){

                    login = loginInterface.login(phone, password);
                    login.enqueue(new Callback<StatusBean<UserInfoBean>>() {
                        @Override
                        public void onResponse(Call<StatusBean<UserInfoBean>> call, Response<StatusBean<UserInfoBean>> response) {
                            boolean isSuccess = response.body().isSuccess();

                            if (isSuccess){
                                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLogin", true);
                                editor.putInt("userNumber", response.body().getData().getId());
                                editor.commit();

                                Intent intent = BindActivity.newIntent(LoginActivity.this);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<StatusBean<UserInfoBean>> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "请把信息填写完整", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}
