package com.example.car.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;
import com.github.gzuliyujiang.wheelpicker.widget.CarPlateWheelLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindActivity extends AppCompatActivity {
    CardView home_find_back;
    CarPlateWheelLayout home_find_number_selector;
    EditText home_find_license;
    CardView home_find_search;
    LottieAnimationView home_find_animation_searching;
    RelativeLayout home_find_info;
    TextView home_find_name;
    TextView home_find_car_name;
    TextView home_find_phone;
    TextView home_find_stuNumber;
    TextView home_find_state;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        InitView();
    }

    private void InitView() {
        home_find_back = findViewById(R.id.home_find_back);
        home_find_number_selector = findViewById(R.id.home_find_number_selector);
        home_find_license = findViewById(R.id.home_find_license);
        home_find_search = findViewById(R.id.home_find_search);
        home_find_animation_searching = findViewById(R.id.home_find_animation_searching);
        home_find_info  = findViewById(R.id.home_find_info);
        home_find_name = findViewById(R.id.home_find_name);
        home_find_car_name = findViewById(R.id.home_find_car_name);
        home_find_phone = findViewById(R.id.home_find_phone);
        home_find_stuNumber = findViewById(R.id.home_find_stuNumber);
        home_find_state  = findViewById(R.id.home_find_state);

        home_find_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_find_license.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(6)});
        home_find_number_selector.setData(new NewCarPlateProvider());
        home_find_number_selector.setDefaultValue("陕","A","");

        home_find_search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (home_find_license.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"车牌号不能为空",Toast.LENGTH_LONG).show();
                } else {
                    // searching
                    home_find_info.setVisibility(View.GONE);
                    home_find_animation_searching.setVisibility(View.VISIBLE);

                    // start search
                    String carNumber = home_find_number_selector.getFirstWheelView().getCurrentItem().toString() + home_find_number_selector.getSecondWheelView().getCurrentItem().toString() + home_find_license.getText();
                    // waiting 1s
                    new Handler().postDelayed(() -> {
                        Call<ResponseBody> call = apiService.findUser(carNumber);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        String responseString = response.body().string();
                                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                        if(jsonMap.get("code").equals("1")){
                                            // search finish
                                            home_find_name.setText("车主："+jsonMap.get("username"));
                                            home_find_car_name.setText("车辆："+jsonMap.get("brand")+" "+jsonMap.get("model"));
                                            home_find_phone.setText("电话："+jsonMap.get("phone"));
                                            home_find_stuNumber.setText("学号："+jsonMap.get("stuNumber"));
                                            String status;
                                            if (Objects.equals(jsonMap.get("status"), "1")) {
                                                status = "优秀";
                                            } else if (Objects.equals(jsonMap.get("status"), "0")) {
                                                status = "一般";
                                            } else  {
                                                status = "较差";
                                            }
                                            home_find_state.setText("信誉："+status);

                                            home_find_animation_searching.setVisibility(View.GONE);
                                            home_find_info.setVisibility(View.VISIBLE);
                                        } else if (jsonMap.get("code").equals("0")) {
                                            //search error
                                            home_find_animation_searching.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"未查到车主信息，请检查车牌号是否正确",Toast.LENGTH_LONG).show();
                                            home_find_info.setVisibility(View.GONE);
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
                    },1000);
                }
            }
        });




    }
}