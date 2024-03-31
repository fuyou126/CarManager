package com.example.car.SalePage.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.SaleCard;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatDetailActivity extends AppCompatActivity {

    CardView sale_chat_detail_back;
    EditText sale_chat_detail_input;
    CardView sale_chat_detail_send;
    SmartRefreshLayout sale_chat_detail_fresh;
    RecyclerView sale_chat_detail_rv;
    TextView sale_chat_detail_carName;
    TextView sale_chat_detail_username;
    CircleImageView sale_chat_detail_icon;
    ImageView sale_chat_detail_car_image;

    List<ChatDetailCard> list = new ArrayList<>();
    ChatDetailAdapter adapter;

    String yourStuNumber = "";
    String sellId = "";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    private ScheduledExecutorService scheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        InitView();
    }
    private void InitView(){
        sale_chat_detail_car_image = findViewById(R.id.sale_chat_detail_car_image);
        sale_chat_detail_icon = findViewById(R.id.sale_chat_detail_icon);
        sale_chat_detail_username = findViewById(R.id.sale_chat_detail_username);
        sale_chat_detail_carName = findViewById(R.id.sale_chat_detail_carName);

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
                    if (sale_chat_detail_input.getText().toString().isEmpty()) {
                        Toast.makeText(ChatDetailActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    } else {
                        sendMessage();
                    }
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
                if (sale_chat_detail_input.getText().toString().isEmpty()) {
                    Toast.makeText(ChatDetailActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage();
                }
            }
        });

        sale_chat_detail_rv = findViewById(R.id.sale_chat_detail_rv);
        adapter = new ChatDetailAdapter(list,this);
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
                getChatting();
            }
        });
        sale_chat_detail_fresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                sale_chat_detail_fresh.finishLoadMore(0);
                //添加一条新数据，再最后的位置
                getChatting();
            }
        });

        // 开启后台任务
        // 初始化 ScheduledExecutorService
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 在这个例子中，假设 getChatting 方法需要在主线程中运行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getChatting();
                    }
                });
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭后台任务
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    private void getChatting() {
        Intent intent = getIntent();
        yourStuNumber = intent.getStringExtra("yourStuNumber");
        sellId = intent.getStringExtra("sellId");

        Glide.with(this)
                .load("http://182.92.87.107:8080/CarServerFile/userIcon/"+yourStuNumber)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(sale_chat_detail_icon);
        Glide.with(this)
                .load("http://182.92.87.107:8080/CarServerFile/sellCar/"+sellId)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(sale_chat_detail_car_image);

        Call<ResponseBody> call = apiService.getChatting(UserInfo.UserStuNum, yourStuNumber, sellId);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            String carName = jsonMap.get("brand") + " " + jsonMap.get("model");
                            sale_chat_detail_carName.setText(carName);
                            String username = jsonMap.get("username");
                            sale_chat_detail_username.setText(username);
                            String iAmBuyer = jsonMap.get("iAmBuyer");
                            
                            list.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("messages"));
                            if (jsonArray != null && jsonArray.size() != 0) {
                                // 转换 JSONArray 到 List<JSONObject>
                                List<JSONObject> jsonList = new ArrayList<>(jsonArray.toJavaList(JSONObject.class));
                                // 对 List<JSONObject> 进行排序
                                jsonList.sort(Comparator.comparingInt(obj -> obj.getIntValue("number")));
                                for (int i=0;i<jsonList.size();i++){
                                    JSONObject jsonObject = jsonList.get(i);
                                    String isBuyer = jsonObject.getString("isBuyer");
                                    list.add(new ChatDetailCard(jsonObject.getString("message"),jsonObject.getString("time"),isBuyer.equals(iAmBuyer)));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else if (jsonMap.get("code").equals("0")) {
                            String carName = jsonMap.get("brand") + " " + jsonMap.get("model");
                            sale_chat_detail_carName.setText(carName);
                            String username = jsonMap.get("username");
                            sale_chat_detail_username.setText(username);
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

    private void sendMessage() {
        Intent intent = getIntent();
        yourStuNumber = intent.getStringExtra("yourStuNumber");
        sellId = intent.getStringExtra("sellId");
        String input = sale_chat_detail_input.getText().toString();
        sale_chat_detail_input.setText("");

        Call<ResponseBody> call = apiService.sendMessage(UserInfo.UserStuNum, yourStuNumber,sellId,input);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            getChatting();
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
}