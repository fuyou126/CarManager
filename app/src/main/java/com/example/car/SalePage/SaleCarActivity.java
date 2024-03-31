package com.example.car.SalePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.View;
import android.widget.ImageView;
import android.widget.Magnifier;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.Chat.ChatDetailActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SaleCarActivity extends AppCompatActivity {

    Boolean isLiked = false;

    CardView sale_car_big_pic;
    ImageView sale_car_big_pic_image;
    TextView sale_car_name;
    TextView sale_car_price;
    TextView sale_car_description;
    CardView sale_car_detail_back;
    CardView sale_car_like_1;
    CardView sale_car_message_1;
    RelativeLayout sale_car_bottom_linear_1;
    ImageView sale_car_heart_1;

    CardView sale_car_like_2;
    CardView sale_car_message_2;
    RelativeLayout sale_car_bottom_linear_2;
    ImageView sale_car_heart_2;
    CardView sale_car_delete_2;

    String carName;
    String description;
    String price;
    String sellId;
    String stuNumber;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_car);

        InitView();
    }

    @SuppressLint("SetTextI18n")
    private void InitView(){
        sale_car_big_pic = findViewById(R.id.sale_car_big_pic);
        sale_car_big_pic_image = findViewById(R.id.sale_car_big_pic_image);
        sale_car_name = findViewById(R.id.sale_car_name);
        sale_car_price = findViewById(R.id.sale_car_price);
        sale_car_description = findViewById(R.id.sale_car_description);
        sale_car_detail_back = findViewById(R.id.sale_car_detail_back);
        sale_car_like_1 = findViewById(R.id.sale_car_like_1);
        sale_car_message_1 = findViewById(R.id.sale_car_message_1);
        sale_car_heart_1 = findViewById(R.id.sale_car_heart_1);
        sale_car_bottom_linear_1 = findViewById(R.id.sale_car_bottom_linear_1);
        sale_car_like_2 = findViewById(R.id.sale_car_like_2);
        sale_car_message_2 = findViewById(R.id.sale_car_message_2);
        sale_car_heart_2 = findViewById(R.id.sale_car_heart_2);
        sale_car_bottom_linear_2 = findViewById(R.id.sale_car_bottom_linear_2);
        sale_car_delete_2 = findViewById(R.id.sale_car_delete_2);

        Intent intent = getIntent();
        carName = intent.getStringExtra("carName");
        description = intent.getStringExtra("description");
        price = intent.getStringExtra("price");
        sellId = intent.getStringExtra("sellId");
        stuNumber = intent.getStringExtra("stuNumber");
        sale_car_name.setText(carName);
        sale_car_price.setText("￥"+price);
        sale_car_description.setText(description);
        Glide.with(this)
                .load("http://182.92.87.107:8080/CarServerFile/sellCar/"+ sellId)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(sale_car_big_pic_image);

        displayDelete();
        sale_car_delete_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete car
                SweetAlertDialog pDialog = new SweetAlertDialog(SaleCarActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog
                        .setTitleText("确认删除该出售信息吗?")
                        .setContentText("删除后无法恢复")
                        .setContentTextSize(14)
                        .setCancelText("✖")
                        .setConfirmText("✔")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#FF0000"))
                        .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                // delete car
                                // refresh once
                                Call<ResponseBody> call = apiService.deleteSell(sellId);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                String responseString = response.body().string();
                                                Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                                if(jsonMap.get("code").equals("1")){
                                                    Toast.makeText(getApplicationContext(),"车辆信息已删除",Toast.LENGTH_LONG).show();
                                                    finish();
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
                        })
                        .show();
            }
        });

        sale_car_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getLiked();
        sale_car_like_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked){
                    deleteLike();
                }else{
                    likeSell();
                }
            }
        });
        sale_car_like_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked){
                    deleteLike();
                }else{
                    likeSell();
                }
            }
        });

        sale_car_message_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start chat
                Intent intent = new Intent(SaleCarActivity.this, ChatDetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) SaleCarActivity.this,sale_car_big_pic,"sale_chat_detail_car_card");
                intent.putExtra("yourStuNumber",stuNumber);
                intent.putExtra("sellId",sellId);
                startActivity(intent,optionsCompat.toBundle());
            }
        });
        sale_car_message_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start chat
                Intent intent = new Intent(SaleCarActivity.this, ChatDetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) SaleCarActivity.this,sale_car_big_pic,"sale_chat_detail_car_card");
                intent.putExtra("yourStuNumber",stuNumber);
                intent.putExtra("sellId",sellId);
                startActivity(intent,optionsCompat.toBundle());
            }
        });
    }

    private void getLiked() {
        isLiked = false;
        Call<ResponseBody> call = apiService.isLiked(UserInfo.UserStuNum,sellId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            if (jsonMap.get("isLiked").equals("1")) {
                                isLiked = true;
                                sale_car_heart_1.setImageResource(R.drawable.liked);
                                sale_car_heart_2.setImageResource(R.drawable.liked);
                            } else  {
                                isLiked = false;
                                sale_car_heart_1.setImageResource(R.drawable.sale_button_like);
                                sale_car_heart_2.setImageResource(R.drawable.sale_button_like);
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
    private void likeSell() {
        Call<ResponseBody> call = apiService.likeSell(UserInfo.UserStuNum,sellId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            isLiked = true;
                            sale_car_heart_1.setImageResource(R.drawable.liked);
                            sale_car_heart_2.setImageResource(R.drawable.liked);
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
    private void deleteLike() {
        Call<ResponseBody> call = apiService.deleteLike(UserInfo.UserStuNum, sellId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            isLiked = false;
                            sale_car_heart_1.setImageResource(R.drawable.sale_button_like);
                            sale_car_heart_2.setImageResource(R.drawable.sale_button_like);
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

    private void displayDelete() {
        if(UserInfo.Admin || stuNumber.equals(UserInfo.UserStuNum)){
            sale_car_bottom_linear_1.setVisibility(View.GONE);
            sale_car_bottom_linear_2.setVisibility(View.VISIBLE);
        }else{
            sale_car_bottom_linear_1.setVisibility(View.VISIBLE);
            sale_car_bottom_linear_2.setVisibility(View.GONE);
        }
    }
}