package com.example.car.SalePage.Like;

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
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.Chat.MessageCard;
import com.example.car.SalePage.SaleCard;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
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

public class SaleLikeActivity extends AppCompatActivity {
    CardView sale_like_back;
    SmartRefreshLayout sale_like_fresh;
    RecyclerView sale_like_rv;

    List<SaleCard> list = new ArrayList<>();
    SaleLikeAdapter adapter;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_like);

        InitView();
    }

    private void InitView() {
        sale_like_back = findViewById(R.id.sale_like_back);
        sale_like_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sale_like_rv = findViewById(R.id.sale_like_rv);
        adapter = new SaleLikeAdapter(list,this);
        sale_like_rv.setAdapter(adapter);
        sale_like_rv.setLayoutManager(new LinearLayoutManager(this));

        sale_like_fresh = findViewById(R.id.sale_like_fresh);
        sale_like_fresh.setRefreshHeader(new ClassicsHeader(this));
        sale_like_fresh.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                sale_like_fresh.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                getLikeList();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        sale_like_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                sale_like_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                getLikeList();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getLikeList() {
        Call<ResponseBody> call = apiService.getLikeList(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            list.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("likes"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String sellId = jsonObj.getString("sellId");
                                    String price = jsonObj.getString("price");
                                    String brand = jsonObj.getString("brand");
                                    String model = jsonObj.getString("model");
                                    String carName = brand + " " +model;
                                    String description = jsonObj.getString("description");
                                    String stuNumber = jsonObj.getString("stuNumber");
                                    list.add(new SaleCard(carName,description,price,sellId,stuNumber));
                                }
                                adapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        getLikeList();
    }
}