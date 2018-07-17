package com.example.yuanyuanlai.drivenoworry.RetrofitInterface;

import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterInterface {


        @POST("api/user/dosendPhoneNumber")
        Call<StatusBean> getCapture(@Query("telephone") String telephone);

        @POST("api/user/doregister")
        Call<StatusBean<Integer>> registUser(@Query("telephone") String phone, @Query("number") String number, @Query("password") String passWord);


}
