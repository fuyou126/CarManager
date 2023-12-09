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

import com.example.car.R;
import com.example.car.SalePage.Chat.MessageCard;
import com.example.car.SalePage.SaleCard;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class SaleLikeActivity extends AppCompatActivity {
    CardView sale_like_back;
    SmartRefreshLayout sale_like_fresh;
    RecyclerView sale_like_rv;

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

        List<SaleCard> s = new ArrayList<>();
        s.add(new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
        s.add(new SaleCard("特斯拉","这是一辆好车，能跑很远啊实打实大大大飒飒的","999","22"));
        s.add(new SaleCard("奥迪A8","这","1000000","6"));
        s.add(new SaleCard("奥迪A8","这","1000000","6"));

        sale_like_rv = findViewById(R.id.sale_like_rv);
        SaleLikeAdapter adapter = new SaleLikeAdapter(s,this);
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
                s.add(0,new SaleCard("奥迪A8","这","1000000","6"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        sale_like_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                sale_like_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                s.add(new SaleCard("奥迪A8","这","1000000","6"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });


    }
}