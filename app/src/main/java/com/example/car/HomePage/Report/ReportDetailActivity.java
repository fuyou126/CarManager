package com.example.car.HomePage.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.car.R;

public class ReportDetailActivity extends AppCompatActivity {

    String ReportId;

    CardView home_report_check_detail_back;
    CardView home_report_check_detail_yes;
    CardView home_report_check_detail_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check_detail);

        InitView();
    }

    private void InitView() {
        Intent thisIntent = getIntent();
        ReportId = thisIntent.getStringExtra("ReportId");

        home_report_check_detail_back = findViewById(R.id.home_report_check_detail_back);
        home_report_check_detail_yes = findViewById(R.id.home_report_check_detail_yes);
        home_report_check_detail_no = findViewById(R.id.home_report_check_detail_no);

        home_report_check_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_report_check_detail_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        home_report_check_detail_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}