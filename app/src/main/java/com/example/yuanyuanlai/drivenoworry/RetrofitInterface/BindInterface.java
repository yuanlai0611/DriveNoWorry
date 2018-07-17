package com.example.yuanyuanlai.drivenoworry.RetrofitInterface;

import com.example.yuanyuanlai.drivenoworry.Bean.StatusBean;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BindInterface {

    @POST("api/user/docompleteInformation")
    Call<StatusBean> bindInformation(@Query("id")int id, @Query("height")double height
            , @Query("weight") double weight, @Query("gender") String gender
            , @Query("age") int age, @Query("province") String province
            , @Query("isHighblood") boolean isHighblood
            , @Query("isHeartillness") boolean isHeartillness);

}
