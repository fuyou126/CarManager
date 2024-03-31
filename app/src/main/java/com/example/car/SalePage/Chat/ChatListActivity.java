package com.example.car.SalePage.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;
import com.example.car.SalePage.SaleCard;
import com.example.car.SalePage.SaleCardAdapter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView sale_chat_list_rv;
    private SmartRefreshLayout sale_chat_list_fresh;
    CardView sale_chat_list_back;

    List<MessageCard> list = new ArrayList<>();
    MessageAdapter adapter;

    String newsTotal = "0";
    String newsDetail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        InitView();
    }

    private void InitView(){
        sale_chat_list_rv = findViewById(R.id.sale_chat_list_rv);
        sale_chat_list_fresh = findViewById(R.id.sale_chat_list_fresh);
        sale_chat_list_back = findViewById(R.id.sale_chat_list_back);

        sale_chat_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new MessageAdapter(list,this);
        sale_chat_list_rv.setAdapter(adapter);
        sale_chat_list_rv.setLayoutManager(new LinearLayoutManager(this));

        sale_chat_list_fresh.setRefreshHeader(new ClassicsHeader(this));
//        sale_chat_list_fresh.setRefreshFooter(new ClassicsFooter(this));
        sale_chat_list_fresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                sale_chat_list_fresh.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                getNews();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
//        sale_chat_list_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                sale_chat_list_fresh.finishLoadMore(0);
//                //添加一条新数据，再最后的位置
//                list.add(new MessageCard("张樊","底部刷新","","",0));
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNews();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getNews() {
        Intent intent = getIntent();
        newsTotal = intent.getStringExtra("newsTotal");
        newsDetail = intent.getStringExtra("newsDetail");

        list.clear();
        JSONArray jsonArray = JSONObject.parseArray(newsDetail);
        if (jsonArray != null) {
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                String username = jsonObj.getString("username");
                String sellId = jsonObj.getString("sellId");
                String stuNumber = jsonObj.getString("stuNumber");
                String news = jsonObj.getString("news");
                String lastMessage = jsonObj.getString("lastMessage");
                list.add(new MessageCard(username, lastMessage, stuNumber, sellId, Integer.parseInt(news)));
            }
        }

        adapter.notifyDataSetChanged();
    }
}