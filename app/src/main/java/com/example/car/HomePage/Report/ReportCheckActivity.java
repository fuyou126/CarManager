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

import com.example.car.R;
import com.example.car.SalePage.SaleCard;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ReportCheckActivity extends AppCompatActivity {

    CardView home_report_check_back;
    SmartRefreshLayout home_report_check_fresh;
    RecyclerView home_report_check_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check);

        InitView();
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

        List<ReportCard> s = new ArrayList<>();
        s.add(new ReportCard("1","张樊","危险驾驶","危险驾驶危险驾驶危险驾驶"));
        s.add(new ReportCard("2","张樊","危险驾驶","危险驾驶"));
        ReportAdapter adapter = new ReportAdapter(s,this);
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
                s.add(0,new ReportCard("1","张樊","危险驾驶","危险驾驶危险驾驶危险驾驶"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        home_report_check_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                home_report_check_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                s.add(new ReportCard("2","张樊","危险驾驶","危险驾驶"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}