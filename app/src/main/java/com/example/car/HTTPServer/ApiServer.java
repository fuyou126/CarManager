package com.example.car.HTTPServer;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {
    @POST("login/login")
    Call<ResponseBody> login(@Query("stuNumber") String stuNumber,@Query("password") String password);

    @POST("login/sign")
    Call<ResponseBody> sign(@Query("stuNumber") String stuNumber,@Query("username") String username,@Query("phone") String phone,@Query("password") String password);

    @Multipart
    @POST("car/addCar")
    Call<ResponseBody> addCar(@Part MultipartBody.Part img, @Query("stuNumber") String stuNumber, @Query("carNumber") String carNumber, @Query("brand") String brand, @Query("model") String model, @Query("username") String username);

    @POST("car/addRequest")
    Call<ResponseBody> addRequest(@Query("carId") String carId);

    @POST("car/addOther")
    Call<ResponseBody> addOther(@Query("stuNumber") String stuNumber, @Query("carNumber") String carNumber, @Query("brand") String brand, @Query("model") String model, @Query("type") String type, @Query("username") String username);

    @POST("car/getRequest")
    Call<ResponseBody> getRequest();

    @POST("car/getRequestInfo")
    Call<ResponseBody> getRequestInfo(@Query("carId") String carId);

    @POST("car/checkRequest")
    Call<ResponseBody> checkRequest(@Query("carId") String carId);

    @POST("car/rejectRequest")
    Call<ResponseBody> rejectRequest(@Query("carId") String carId);

    @POST("car/getMyCar")
    Call<ResponseBody> getMyCar(@Query("stuNumber") String stuNumber);

    @POST("car/getCardInfo")
    Call<ResponseBody> getCardInfo(@Query("stuNumber") String stuNumber);

    @POST("car/getCardDetail")
    Call<ResponseBody> getCardDetail(@Query("carId") String carId);

    @POST("car/deleteCar")
    Call<ResponseBody> deleteCar(@Query("carId") String carId);

    @POST("info/getInfo")
    Call<ResponseBody> getInfo(@Query("stuNumber") String stuNumber);

    @Multipart
    @POST("info/uploadIcon")
    Call<ResponseBody> uploadIcon(@Part MultipartBody.Part img, @Query("stuNumber") String stuNumber);
}
