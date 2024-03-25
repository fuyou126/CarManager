package com.example.car.HomePage.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.car.Car.CarCard;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyReportActivity extends AppCompatActivity {

    CardView home_my_report_back;
    RecyclerView home_my_report_rv;
    ImageView home_my_report_emo;
    int emo = 1; // -1 0 1

    List<MyReportCard> reportCardList = new ArrayList<>();
    MyReportAdapter adapter = null;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);

        InitView();
        getReportList();
    }

    private void getReportList() {
        Call<ResponseBody> call = apiService.getMyReport(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            reportCardList.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("reports"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String date = jsonObj.getString("date");
                                    String type = jsonObj.getString("type");

                                    reportCardList.add(new MyReportCard(date,type));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            emo = Integer.parseInt(Objects.requireNonNull(jsonMap.get("status")));
                            if(emo == -1){
                                home_my_report_emo.setImageResource(R.drawable.cry);
                            } else if (emo == 0) {
                                home_my_report_emo.setImageResource(R.drawable.meh);
                            }else {
                                home_my_report_emo.setImageResource(R.drawable.smile);
                            }
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

    private void InitView() {
        home_my_report_back = findViewById(R.id.home_my_report_back);
        home_my_report_rv = findViewById(R.id.home_my_report_rv);
        home_my_report_emo = findViewById(R.id.home_my_report_emo);

        home_my_report_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_my_report_emo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emo == -1){
                    Toast.makeText(getApplicationContext(),"当前守法信誉状态较差，停车证已失效",Toast.LENGTH_LONG).show();
                } else if (emo == 0) {
                    Toast.makeText(getApplicationContext(),"当前守法信誉状态一般，请及时学法加分",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"当前守法信誉状态优秀，请继续保持",Toast.LENGTH_LONG).show();
                }
            }
        });

        adapter = new MyReportAdapter(reportCardList,this);
        home_my_report_rv.setAdapter(adapter);
        home_my_report_rv.setLayoutManager(new LinearLayoutManager(this));
    }
}