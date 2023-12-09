package com.example.car.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.airbnb.lottie.LottieAnimationView;
import com.example.car.R;
import com.github.gzuliyujiang.wheelpicker.impl.CarPlateProvider;
import com.github.gzuliyujiang.wheelpicker.widget.CarPlateWheelLayout;

public class MoveCarActivity extends AppCompatActivity {
    CardView home_move_back;
    EditText home_move_license;
    CarPlateWheelLayout home_move_number_selector;
    CardView home_move_search;
    LottieAnimationView home_move_animation_searching;
    TextView home_move_search_tips;
    TextView home_move_name;
    TextView home_move_car_name;
    TextView home_move_phone;
    CardView home_move_button_email;
    CardView home_move_button_call;
    RelativeLayout home_move_info;

    private final int REQUEST_PERMISSIONS_EMAIL = 1;
    private final int REQUEST_PERMISSIONS_CALL = 2;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_car);
        InitView();
    }

    private void InitView() {
        home_move_back = findViewById(R.id.home_move_back);
        home_move_license = findViewById(R.id.home_move_license);
        home_move_number_selector = findViewById(R.id.home_move_number_selector);
        home_move_search = findViewById(R.id.home_move_search);
        home_move_search_tips = findViewById(R.id.home_move_search_tips);
        home_move_name = findViewById(R.id.home_move_name);
        home_move_car_name = findViewById(R.id.home_move_car_name);
        home_move_phone = findViewById(R.id.home_move_phone);
        home_move_button_email = findViewById(R.id.home_move_button_email);
        home_move_button_call = findViewById(R.id.home_move_button_call);
        home_move_animation_searching = findViewById(R.id.home_move_animation_searching);
        home_move_info = findViewById(R.id.home_move_info);

        home_move_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home_move_license.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(6)});

        home_move_number_selector.setData(new NewCarPlateProvider());
        home_move_number_selector.setDefaultValue("陕","A","");

        home_move_search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // searching
                home_move_info.setVisibility(View.GONE);
                home_move_search_tips.setText("正在寻找中...");
                home_move_animation_searching.setVisibility(View.VISIBLE);
                // start search

                // search finish
                if(true) {
                    home_move_animation_searching.setVisibility(View.GONE);
                    home_move_search_tips.setText("已经查找到车主信息，如需挪车请联系车主");
                    home_move_info.setVisibility(View.VISIBLE);
                    // update info
                    home_move_name.setText("车主:张*");
                    home_move_car_name.setText("车辆:奥迪 A6");
                    home_move_phone.setText("电话:150****2282");
                    phone_number = "15091752282";
                }else {
                    //search error
                    home_move_animation_searching.setVisibility(View.GONE);
                    home_move_search_tips.setText("未查到车主信息，请检查车牌号是否正确");
                    home_move_info.setVisibility(View.GONE);
                }
            }
        });

        home_move_button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MoveCarActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MoveCarActivity.this,new String[]{Manifest.permission.SEND_SMS},REQUEST_PERMISSIONS_EMAIL);
                }else{
                    sms();
                }
            }
        });

        home_move_button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MoveCarActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MoveCarActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PERMISSIONS_CALL);
                }else{
                    call();
                }
            }
        });




    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "未获取到电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSIONS_EMAIL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sms();
                } else {
                    Toast.makeText(this, "未获取到短信权限", Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    private void call(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+86"+phone_number)); //设置要拨打的号码
        startActivity(intent);
    }
    private void sms(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO); //调用发送短信息
        intent.setData(Uri.parse("smsto:+86"+phone_number)); //设置要发送的号码
        intent.putExtra("sms_body", "不好意思，您的车挡住了我的去路，能否请您把车挪一下");
        startActivity(intent);
    }
}