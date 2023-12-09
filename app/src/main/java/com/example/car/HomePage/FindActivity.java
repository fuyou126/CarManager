package com.example.car.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.car.R;
import com.github.gzuliyujiang.wheelpicker.widget.CarPlateWheelLayout;

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
                // searching
                home_find_info.setVisibility(View.GONE);
                home_find_animation_searching.setVisibility(View.VISIBLE);
                // start search

                // search finish
                if(true) {
                    home_find_animation_searching.setVisibility(View.GONE);
                    home_find_info.setVisibility(View.VISIBLE);
                    // update info
                    home_find_name.setText("车主:"+"张樊");
                    home_find_car_name.setText("车辆:"+"奥迪 A6");
                    home_find_phone.setText("电话:"+"15091752282");
                    home_find_stuNumber.setText("学号:"+"2020118108");
                    home_find_state.setText("信誉:"+"优秀");
                }else {
                    //search error
                    home_find_animation_searching.setVisibility(View.GONE);
                    home_find_name.setText("未查到车主信息，请检查车牌号是否正确");
                    home_find_info.setVisibility(View.GONE);
                }
            }
        });




    }
}