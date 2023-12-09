package com.example.car.SalePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.car.R;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class SaleSearchActivity extends AppCompatActivity {

    private SmartRefreshLayout smartRefreshLayout;
    RecyclerView saleCard_rv;
    String searchFor;
    CardView sale_detail_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_search);

        getWindow().setStatusBarColor(Color.rgb(76,57,204));
        InitView();
    }

    private void InitView(){
        sale_detail_back = findViewById(R.id.sale_detail_back);
        sale_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saleCard_rv = findViewById(R.id.sale_detail_rv);
        List<SaleCard> s = new ArrayList<>();
        s.add(new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
        // 获取适配器实例
        SaleCardAdapter adapter = new SaleCardAdapter(s,this);
        //配置适配器
        saleCard_rv.setAdapter(adapter);
        //配置布局管理器
        saleCard_rv.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout = findViewById(R.id.sale_detail_fresh);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                smartRefreshLayout.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                s.add(0,new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                s.add(new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}