package com.example.car.SalePage.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
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

public class ChatDetailActivity extends AppCompatActivity {

    CardView sale_chat_detail_back;
    EditText sale_chat_detail_input;
    CardView sale_chat_detail_send;
    SmartRefreshLayout sale_chat_detail_fresh;
    RecyclerView sale_chat_detail_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        InitView();
    }
    private void InitView(){
        sale_chat_detail_back = findViewById(R.id.sale_chat_detail_back);
        sale_chat_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sale_chat_detail_input = findViewById(R.id.sale_chat_detail_input);
        sale_chat_detail_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || i == EditorInfo.IME_ACTION_UNSPECIFIED || (keyEvent != null && keyEvent.getKeyCode() ==  KeyEvent.KEYCODE_ENTER)) {
                    // 执行发送操作

                    return true;
                }
                return false;
            }
        });

        sale_chat_detail_send = findViewById(R.id.sale_chat_detail_send);
        sale_chat_detail_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send
            }
        });

        sale_chat_detail_rv = findViewById(R.id.sale_chat_detail_rv);
        List<ChatDetailCard> list = new ArrayList<>();
        list.add(new ChatDetailCard("对方要说的话",null,false));
        list.add(new ChatDetailCard("我要说的话",null,true));
        list.add(new ChatDetailCard("对方要说的话2",null,false));
        ChatDetailAdapter adapter = new ChatDetailAdapter(list,this);
        sale_chat_detail_rv.setAdapter(adapter);
        sale_chat_detail_rv.setLayoutManager(new LinearLayoutManager(this));

        sale_chat_detail_fresh = findViewById(R.id.sale_chat_detail_fresh);
        sale_chat_detail_fresh.setRefreshHeader(new ClassicsHeader(this));
        sale_chat_detail_fresh.setRefreshFooter(new ClassicsFooter(this));
        sale_chat_detail_fresh.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                sale_chat_detail_fresh.finishRefresh(0);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                list.add(0,new ChatDetailCard("对方要说的话2",null,false));
                adapter.notifyDataSetChanged();
            }
        });
        sale_chat_detail_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                sale_chat_detail_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                list.add(new ChatDetailCard("对方要说的话2",null,false));
                adapter.notifyDataSetChanged();
            }
        });
    }
}