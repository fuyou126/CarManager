package com.example.car.HomePage.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.Car.CarCard;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportDetailActivity extends AppCompatActivity {

    String reportId;
    String carId;
    String stuNumber;
    String carNumber;
    String description;
    String type;

    CardView home_report_check_detail_back;
    CardView home_report_check_detail_yes;
    CardView home_report_check_detail_no;

    ImageView home_report_check_detail_pic;
    TextView home_report_check_detail_type;
    TextView home_report_check_detail_description;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check_detail);

        InitView();
    }

    private void InitView() {
        Intent thisIntent = getIntent();
        reportId = thisIntent.getStringExtra("reportId");
        carId = thisIntent.getStringExtra("carId");
        stuNumber = thisIntent.getStringExtra("stuNumber");
        carNumber = thisIntent.getStringExtra("carNumber");
        description = thisIntent.getStringExtra("description");
        type = thisIntent.getStringExtra("type");

        home_report_check_detail_back = findViewById(R.id.home_report_check_detail_back);
        home_report_check_detail_yes = findViewById(R.id.home_report_check_detail_yes);
        home_report_check_detail_no = findViewById(R.id.home_report_check_detail_no);

        home_report_check_detail_pic = findViewById(R.id.home_report_check_detail_pic);
        home_report_check_detail_type = findViewById(R.id.home_report_check_detail_type);
        home_report_check_detail_description = findViewById(R.id.home_report_check_detail_description);

        Glide.with(ReportDetailActivity.this)
                .load("http://182.92.87.107:8080/CarServerFile/reportImage/"+reportId)
                .into(home_report_check_detail_pic);
        home_report_check_detail_type.setText(type);
        home_report_check_detail_description.setText(description);


        home_report_check_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_report_check_detail_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = apiService.checkReport(reportId, carId);
                call.enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseString = response.body().string();
                                Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                if(jsonMap.get("code").equals("1")){
                                    Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                                    finish();
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
        });
        home_report_check_detail_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = apiService.rejectReport(reportId, carId);
                call.enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseString = response.body().string();
                                Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                if(jsonMap.get("code").equals("1")){
                                    Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                                    finish();
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
        });
    }
}