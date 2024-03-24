package com.example.car.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardActivity extends AppCompatActivity {

    CardView home_card_close;
    ImageView home_card_emo;
    TextView home_card_stuNumber;
    TextView home_card_username;
    TextView home_card_carName;
    TextView home_card_carNumber;
    TextView home_card_state;
    TextView home_card_date;
    TextView home_card_endDate;
    int emo = 1; // -1 0 1

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        
        InitView();
        InitData();
    }

    private void InitData() {
        Call<ResponseBody> call = apiService.getCardDetail(getIntent().getStringExtra("carId"));
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            home_card_carName.setText(jsonMap.get("brand") + " " + jsonMap.get("model"));
                            home_card_carNumber.setText(Objects.requireNonNull(jsonMap.get("carNumber")).substring(0,2)+"·"+ Objects.requireNonNull(jsonMap.get("carNumber")).substring(2));
                            home_card_date.setText(jsonMap.get("startDate"));
                            home_card_endDate.setText(jsonMap.get("endDate"));
                            emo = Integer.parseInt(Objects.requireNonNull(jsonMap.get("status")));
                            if(emo == -1){
                                home_card_emo.setImageResource(R.drawable.cry);
                                home_card_state.setText("较差");
                            } else if (emo == 0) {
                                home_card_emo.setImageResource(R.drawable.meh);
                                home_card_state.setText("一般");
                            }else {
                                home_card_emo.setImageResource(R.drawable.smile);
                                home_card_state.setText("优秀");
                            }
                        }else if(jsonMap.get("code").equals("0")){
                            Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
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
        home_card_close = findViewById(R.id.home_card_close);
        home_card_emo = findViewById(R.id.home_card_emo);
        home_card_stuNumber = findViewById(R.id.home_card_stuNumber);
        home_card_username = findViewById(R.id.home_card_username);
        home_card_carName = findViewById(R.id.home_card_carName);
        home_card_carNumber = findViewById(R.id.home_card_carNumber);
        home_card_state = findViewById(R.id.home_card_state);
        home_card_date = findViewById(R.id.home_card_date);
        home_card_endDate = findViewById(R.id.home_card_endDate);

        home_card_stuNumber.setText(UserInfo.UserStuNum);
        home_card_username.setText(UserInfo.UserName);

        home_card_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_card_emo.setOnClickListener(new View.OnClickListener() {
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
    }
}