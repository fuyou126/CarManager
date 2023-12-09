package com.example.car;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.car.Car.CarFragment;
import com.example.car.HomePage.HomeFragment;
import com.example.car.Info.InfoFragment;
import com.example.car.SalePage.SaleFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HomeActivity extends AppCompatActivity {

    AnimatedBottomBar bottomBar;
    String StuNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        InitView();


    }

    private void InitView() {
        bottomBar = findViewById(R.id.bottom_bar);
        replace_menu(new HomeFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if(tab1.getId()==R.id.home){
                    replace_menu(new HomeFragment());
                }else if(tab1.getId()==R.id.sale){
                    replace_menu(new SaleFragment());
                }else if(tab1.getId()==R.id.car){
                    replace_menu(new CarFragment());
                }else if(tab1.getId()==R.id.info){
                    replace_menu(new InfoFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

        Intent intent = getIntent();
        StuNumber = intent.getStringExtra("StuNumber");

    }

    private void replace_menu(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        //禁用返回键
    }
}