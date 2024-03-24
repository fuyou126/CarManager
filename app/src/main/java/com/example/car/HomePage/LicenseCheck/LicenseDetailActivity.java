package com.example.car.HomePage.LicenseCheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LicenseDetailActivity extends AppCompatActivity {
    Context context = this;

    CardView home_license_check_detail_back;
    ImageView home_license_check_detail_pic;
    TextView home_license_check_detail_stuNumber;
    TextView home_license_check_detail_username;
    TextView home_license_check_detail_carNumber;
    TextView home_license_check_detail_phone;
    TextView home_license_check_detail_carName;
    CardView home_license_check_detail_no;
    CardView home_license_check_detail_yes;

    String carId = null;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_detail);

        InitView();
    }

    private void InitView() {
        home_license_check_detail_back = findViewById(R.id.home_license_check_detail_back);
        home_license_check_detail_pic = findViewById(R.id.home_license_check_detail_pic);
        home_license_check_detail_stuNumber = findViewById(R.id.home_license_check_detail_stuNumber);
        home_license_check_detail_username = findViewById(R.id.home_license_check_detail_username);
        home_license_check_detail_carNumber = findViewById(R.id.home_license_check_detail_carNumber);
        home_license_check_detail_phone = findViewById(R.id.home_license_check_detail_phone);
        home_license_check_detail_carName = findViewById(R.id.home_license_check_detail_carName);
        home_license_check_detail_no = findViewById(R.id.home_license_check_detail_no);
        home_license_check_detail_yes = findViewById(R.id.home_license_check_detail_yes);

        home_license_check_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getInfo();

        home_license_check_detail_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo no
                clickNo();

                finish();
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
            }
        });
        home_license_check_detail_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo yes
                clickYes();

                finish();
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clickYes() {
        Call<ResponseBody> call = apiService.checkRequest(carId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            finish();
                            Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                        }else if(jsonMap.get("code").equals("0")){
                            Toast.makeText(getApplicationContext(),jsonMap.get("网络错误"),Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理失败的情况
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clickNo() {
        Call<ResponseBody> call = apiService.rejectRequest(carId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            finish();
                            Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                        }else if(jsonMap.get("code").equals("0")){
                            Toast.makeText(getApplicationContext(),jsonMap.get("网络错误"),Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理失败的情况
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getInfo() {
        Intent intent = getIntent();
        carId = intent.getStringExtra("carId");

        Call<ResponseBody> call = apiService.getRequestInfo(carId);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            home_license_check_detail_stuNumber.setText("学号:"+jsonMap.get("stuNumber"));
                            home_license_check_detail_carNumber.setText("车牌:"+ Objects.requireNonNull(jsonMap.get("carNumber")).substring(0,2)+"·"+ Objects.requireNonNull(jsonMap.get("carNumber")).substring(2));
                            home_license_check_detail_carName.setText("车辆:"+jsonMap.get("brand")+" "+jsonMap.get("model"));
                            home_license_check_detail_username.setText("姓名:"+jsonMap.get("username"));
                            home_license_check_detail_phone.setText("电话:"+jsonMap.get("phone"));
                            Glide.with(context)
                                    .load("http://182.92.87.107:8080/CarServerFile/carRequest/"+carId)
                                    .into(home_license_check_detail_pic);
                        }else if(jsonMap.get("code").equals("0")){
                            Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理失败的情况
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
            }
        });
    }
}