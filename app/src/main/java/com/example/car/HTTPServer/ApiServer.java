package com.example.car.HTTPServer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {
    @GET("login/login")
    Call<ResponseBody> login(@Query("stuNumber") String stuNumber,@Query("password") String password);

    @GET("login/sign")
    Call<ResponseBody> sign(@Query("stuNumber") String stuNumber,@Query("username") String username,@Query("phone") String phone,@Query("password") String password);

    @GET("car/addRequest")
    Call<ResponseBody> addRequest(@Query("stuNumber") String stuNumber, @Query("carNumber") String carNumber, @Query("brand") String brand, @Query("model") String model, @Query("type") String type);

    @GET("car/getRequest")
    Call<ResponseBody> getRequest();

    @GET("car/getRequestInfo")
    Call<ResponseBody> getRequestInfo(@Query("carId") String carId);

    @GET("car/checkRequest")
    Call<ResponseBody> checkRequest(@Query("carId") String carId);

    @GET("car/rejectRequest")
    Call<ResponseBody> rejectRequest(@Query("carId") String carId);
}
