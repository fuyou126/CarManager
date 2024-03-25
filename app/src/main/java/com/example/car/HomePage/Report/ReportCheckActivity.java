package com.example.car.HomePage.Report;

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
import com.example.car.Car.CarCard;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.scwang.smart.refresh.footer.ClassicsFooter;
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

public class ReportCheckActivity extends AppCompatActivity {

    CardView home_report_check_back;
    SmartRefreshLayout home_report_check_fresh;
    RecyclerView home_report_check_rv;
    List<ReportCard> reportList = new ArrayList<>();
    ReportAdapter adapter = null;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check);

        InitView();
        getReportList();
    }

    private void getReportList() {
        Call<ResponseBody> call = apiService.getReportList();
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            reportList.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("reports"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String reportId = jsonObj.getString("reportId");
                                    String type = jsonObj.getString("type");
                                    String description = jsonObj.getString("description");
                                    String carNumber = jsonObj.getString("carNumber");
                                    String stuNumber = jsonObj.getString("stuNumber");
                                    String carId = jsonObj.getString("carId");
                                    reportList.add(new ReportCard(reportId,stuNumber,type,description,carId,carNumber));
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

    private void InitView() {
        home_report_check_back = findViewById(R.id.home_report_check_back);
        home_report_check_fresh = findViewById(R.id.home_report_check_fresh);
        home_report_check_rv = findViewById(R.id.home_report_check_rv);

        home_report_check_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new ReportAdapter(reportList,this);
        home_report_check_rv.setAdapter(adapter);
        home_report_check_rv.setLayoutManager(new LinearLayoutManager(this));
        home_report_check_fresh.setRefreshHeader(new ClassicsHeader(this));
        home_report_check_fresh.setRefreshFooter(new ClassicsFooter(this));
        home_report_check_fresh.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                home_report_check_fresh.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                getReportList();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        home_report_check_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                home_report_check_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                getReportList();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReportList();
    }
}