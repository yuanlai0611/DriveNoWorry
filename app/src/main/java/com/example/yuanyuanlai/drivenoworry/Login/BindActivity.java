package com.example.yuanyuanlai.drivenoworry.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.yuanyuanlai.drivenoworry.Activity.MainActivity;
import com.example.yuanyuanlai.drivenoworry.Base.BaseActivity;
import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.RetrofitInterface.BindInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BindActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private Button finishButton;
    private TextView heightTextView, weightTextView, ageTextView, placeTextView;
    private Switch sexSwitch, heartSwitch, bloodSwitch;
    private List<String> heightOptions, weightOptions, ageOptions, placeOptions;
    private String[] places = {"河北省", "山西省", "吉林省", "辽宁省", "黑龙江省", "陕西省"
            , "甘肃省", "青海省", "山东省", "福建省", "浙江省", "台湾省", "河南省", "湖北省"
            , "湖南省", "江西省", "江苏省", "安徽省", "广东省", "海南省", "四川省", "贵州省"
            , "云南省", "北京市", "上海市", "天津市", "重庆市", "内蒙古自治区", "新疆维吾尔自治区"
            , "宁夏回族自治区", "广西壮族自治区", "西藏自治区", "香港特别行政区", "澳门特别行政区"};
    private OptionsPickerView heightOptionsPickerView, weightOptionsPickerView, ageOptionsPickerView, placeOptionsPickerView;
    private String tempHeight, tempWeight, tempGender = "0", tempPlace;
    private int tempAge;
    private boolean isHeartDisease, isBloodDisease;
    private Retrofit retrofit;
    private BindInterface bindInterface;
    private Call<StatusBean> bindInformation;
    private String baseUrl = "http://47.92.48.100:8099/carHealth/";
    private TextView sexTextView, highBloodTextView, heartTextView;


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, BindActivity.class);
        return intent;
    }

    /*
    河北省、山西省、吉林省、辽宁省、黑龙江省、陕西省、甘肃省、青海省、山东省、福建省、浙江省、台湾省、河南省、湖北省、湖南省、江西省、江苏省、安徽省、广东省、海南省、四川省、贵州省、云南省
    北京市、上海市、天津市、重庆市
    内蒙古自治区、新疆维吾尔自治区、宁夏回族自治区、广西壮族自治区、西藏自治区
    香港特别行政区、澳门特别行政区
     */

    @Override
    protected void setListener() {
        finishButton.setOnClickListener(this);
        heightTextView.setOnClickListener(this);
        weightTextView.setOnClickListener(this);
        ageTextView.setOnClickListener(this);
        placeTextView.setOnClickListener(this);
        sexSwitch.setOnCheckedChangeListener(this);
        bloodSwitch.setOnCheckedChangeListener(this);
        heartSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {

        heightOptions = new ArrayList<>();
        weightOptions = new ArrayList<>();
        ageOptions = new ArrayList<>();
        placeOptions = new ArrayList<>();
        for (int i=0; i<60; i++){
            heightOptions.add(""+(150+i)+"cm");
        }
        for (int i=0; i<100; i++){
            weightOptions.add(""+(40+i)+"kg");
        }
        for (int i=0; i<60; i++){
            ageOptions.add(""+(18+i)+"岁");
        }
        for (String place : places){
            placeOptions.add(place);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bindInterface = retrofit.create(BindInterface.class);


    }

    @Override
    protected void setData() {

    }

    @Override
    protected void initViews() {

       finishButton = (Button)findViewById(R.id.finish);
       heightTextView = (TextView)findViewById(R.id.height_textView);
       weightTextView = (TextView)findViewById(R.id.weight_textView);
       ageTextView = (TextView)findViewById(R.id.age_textView);
       placeTextView = (TextView)findViewById(R.id.place_textView);
       sexSwitch = (Switch)findViewById(R.id.sex_switch);
       heartSwitch = (Switch)findViewById(R.id.heart_switch);
       bloodSwitch = (Switch)findViewById(R.id.high_blood_switch);
       sexTextView = (TextView)findViewById(R.id.sex_textView);
       highBloodTextView = (TextView)findViewById(R.id.high_blood_textView);
       heartTextView = (TextView)findViewById(R.id.heart_textView);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_bind);
    }

    @Override
    protected void onMultiClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.sex_switch:
                if (isChecked){
                    sexTextView.setText("男");
                }else {
                    sexTextView.setText("女");
                }
                break;
            case R.id.high_blood_switch:
                if (isChecked){
                    highBloodTextView.setText("有高血压病史");
                }else {
                    highBloodTextView.setText("无高血压病史");
                }
                break;
            case R.id.heart_switch:
                if (isChecked){
                    heartTextView.setText("有心脏病史");
                }else {
                    heartTextView.setText("无心脏病史");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish:
                if (tempHeight!=null&&tempWeight!=null&&tempGender!=null&&tempPlace!=null){

                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("userNumber", 0);
                    if (sexSwitch.isChecked()){
                        tempGender = "0";
                    }else {
                        tempGender = "1";
                    }
                    if (bloodSwitch.isChecked()){
                        isBloodDisease = true;
                    }else {
                        isBloodDisease = false;
                    }
                    if (heartSwitch.isChecked()){
                        isHeartDisease = true;
                    }else {
                        isHeartDisease = false;
                    }
                    bindInformation = bindInterface.bindInformation(userId, Double.valueOf(tempHeight.replace("cm", "")), Double.valueOf(tempWeight.replace("kg", "")), tempGender, tempAge, tempPlace, isBloodDisease, isHeartDisease);
                    Log.d("BindActivity", ""+userId);
                    Log.d("BindActivity", tempHeight);
                    Log.d("BindActivity", tempWeight);
                    Log.d("BindActivity", tempGender);
                    Log.d("BindActivity", ""+tempAge);
                    Log.d("BindActivity", tempPlace);
                    Log.d("BindActivity", ""+isBloodDisease);
                    Log.d("BindActivity", ""+isHeartDisease);

                    bindInformation.enqueue(new Callback<StatusBean>() {
                        @Override
                        public void onResponse(Call<StatusBean> call, Response<StatusBean> response) {
                            boolean isSuccess = response.body().isSuccess();
                            if (isSuccess){
                                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isBind", true);
                                editor.commit();

                                Intent intent = MainActivity.newIntent(BindActivity.this);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(BindActivity.this, "绑定失败" ,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusBean> call, Throwable t) {
                            Toast.makeText(BindActivity.this, "绑定失败" ,Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(BindActivity.this, "信息需要填写完整", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.height_textView:
                heightOptionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tempHeight = heightOptions.get(options1);
                        heightTextView.setText(tempHeight);
                    }
                }).setTitleText("选择身高")
                        .setTitleSize(16)
                        .setDividerColor(Color.LTGRAY)
                        .setContentTextSize(20)
                        .setSelectOptions(0)
                        .setTitleColor(Color. BLACK)
                        .setSubmitColor(getColor(R.color.colorOrange))
                        .setCancelColor(getColor(R.color.colorOrange))
                        .setBackgroundId(0x44444444)
                        .build();
                heightOptionsPickerView.setPicker(heightOptions);
                heightOptionsPickerView.show();
                break;
            case R.id.weight_textView:
                weightOptionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tempWeight = weightOptions.get(options1);
                        weightTextView.setText(tempWeight);
                    }
                }).setTitleText("选择身高")
                        .setTitleSize(16)
                        .setDividerColor(Color.LTGRAY)
                        .setContentTextSize(20)
                        .setSelectOptions(0)
                        .setTitleColor(Color. BLACK)
                        .setSubmitColor(getColor(R.color.colorOrange))
                        .setCancelColor(getColor(R.color.colorOrange))
                        .setBackgroundId(0x44444444)
                        .build();
                weightOptionsPickerView.setPicker(weightOptions);
                weightOptionsPickerView.show();
                break;
            case R.id.age_textView:
                ageOptionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tempAge = Integer.parseInt(ageOptions.get(options1).replace("岁",""));
                        ageTextView.setText(""+tempAge);
                    }
                }).setTitleText("选择身高")
                        .setTitleSize(16)
                        .setDividerColor(Color.LTGRAY)
                        .setContentTextSize(20)
                        .setSelectOptions(0)
                        .setTitleColor(Color. BLACK)
                        .setSubmitColor(getColor(R.color.colorOrange))
                        .setCancelColor(getColor(R.color.colorOrange))
                        .setBackgroundId(0x44444444)
                        .build();
                ageOptionsPickerView.setPicker(ageOptions);
                ageOptionsPickerView.show();
                break;
            case R.id.place_textView:
                placeOptionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tempPlace = placeOptions.get(options1);
                        placeTextView.setText(tempPlace);
                    }
                }).setTitleText("选择身高")
                        .setTitleSize(16)
                        .setDividerColor(Color.LTGRAY)
                        .setContentTextSize(20)
                        .setSelectOptions(0)
                        .setTitleColor(Color. BLACK)
                        .setSubmitColor(getColor(R.color.colorOrange))
                        .setCancelColor(getColor(R.color.colorOrange))
                        .setBackgroundId(0x44444444)
                        .build();
                placeOptionsPickerView.setPicker(placeOptions);
                placeOptionsPickerView.show();
                break;
            default:
                break;
        }
    }
}
