package com.example.yuanyuanlai.drivenoworry.RetrofitInterface;

import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import com.example.yuanyuanlai.drivenoworry.Bean.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginInterface {

    @POST("api/user/dologin")
    Call<StatusBean<UserInfoBean>> login(@Query("telephone") String username, @Query("password") String password);

}
