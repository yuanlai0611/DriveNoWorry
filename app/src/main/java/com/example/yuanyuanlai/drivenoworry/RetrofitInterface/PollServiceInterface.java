package com.example.yuanyuanlai.drivenoworry.RetrofitInterface;

import com.example.yuanyuanlai.drivenoworry.Bean.DriverStatusBean;
import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PollServiceInterface {

    @POST("api/record/dogetLatestRecord")
    Call<StatusBean<DriverStatusBean>> getDriverStatus(@Query("userId")String userId);

}
