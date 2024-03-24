package com.example.car.HomePage.LicenseCheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LicenseCheckActivity extends AppCompatActivity {

    CardView home_license_check_back;
    SmartRefreshLayout home_license_check_fresh;
    RecyclerView home_license_check_rv;

    List<LicenseCard> list = new ArrayList<>();
    LicenseAdapter adapter = new LicenseAdapter(list,this);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_check);
        
        InitView();
    }

    private void InitView() {
        home_license_check_back = findViewById(R.id.home_license_check_back);
        home_license_check_fresh = findViewById(R.id.home_license_check_fresh);
        home_license_check_rv = findViewById(R.id.home_license_check_rv);

        home_license_check_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_license_check_rv.setAdapter(adapter);
        home_license_check_rv.setLayoutManager(new LinearLayoutManager(this));

        home_license_check_fresh.setRefreshHeader(new ClassicsHeader(this));
        home_license_check_fresh.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                home_license_check_fresh.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                getLicenseCard();
                adapter.notifyDataSetChanged();
            }
        });



    }

    private void getLicenseCard() {
        Call<ResponseBody> call = apiService.getRequest();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            list.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("cars"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String carId = jsonObj.getString("carId");
                                    String username = jsonObj.getString("username");
                                    String carNumber = jsonObj.getString("carNumber");
                                    String brand = jsonObj.getString("brand");
                                    String model = jsonObj.getString("model");
                                    list.add(new LicenseCard(username, brand, model, carNumber, carId));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }else if(jsonMap.get("code").equals("0")){
                            Toast.makeText(getApplicationContext(),jsonMap.get("获取失败"),Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        getLicenseCard();
    }
}