package com.example.car.SalePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.View;
import android.widget.ImageView;
import android.widget.Magnifier;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.Chat.ChatDetailActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SaleCarActivity extends AppCompatActivity {

    Boolean isLiked = false;

    CardView sale_car_big_pic;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_car);

        InitView();
    }

    private void InitView(){
        sale_car_big_pic = findViewById(R.id.sale_car_big_pic);
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
                                Toast.makeText(getApplicationContext(),"车辆信息已删除",Toast.LENGTH_LONG).show();
                                finish();
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

        isLiked = false;
        if(isLiked){
            sale_car_heart_1.setImageResource(R.drawable.liked);
            sale_car_heart_2.setImageResource(R.drawable.liked);
        }
        sale_car_like_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLiked = !isLiked;
                if(isLiked){
                    sale_car_heart_1.setImageResource(R.drawable.liked);
                }else{
                    sale_car_heart_1.setImageResource(R.drawable.sale_button_like);
                }
                // send isLiked
            }
        });
        sale_car_like_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLiked = !isLiked;
                if(isLiked){
                    sale_car_heart_2.setImageResource(R.drawable.liked);
                }else{
                    sale_car_heart_2.setImageResource(R.drawable.sale_button_like);
                }
                // send isLiked
            }
        });

        sale_car_message_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start chat
                Intent intent = new Intent(SaleCarActivity.this, ChatDetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) SaleCarActivity.this,sale_car_big_pic,"sale_chat_detail_car_card");
                startActivity(intent,optionsCompat.toBundle());
            }
        });
        sale_car_message_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start chat
                Intent intent = new Intent(SaleCarActivity.this, ChatDetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) SaleCarActivity.this,sale_car_big_pic,"sale_chat_detail_car_card");
                startActivity(intent,optionsCompat.toBundle());
            }
        });
    }

    private void displayDelete() {
        if(UserInfo.UserName.equals("1")){
            sale_car_bottom_linear_1.setVisibility(View.GONE);
            sale_car_bottom_linear_2.setVisibility(View.VISIBLE);
        }else{
            sale_car_bottom_linear_1.setVisibility(View.VISIBLE);
            sale_car_bottom_linear_2.setVisibility(View.GONE);
        }
    }
}