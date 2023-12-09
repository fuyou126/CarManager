package com.example.car.HomePage.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.car.R;

import java.util.ArrayList;
import java.util.List;

public class MyReportActivity extends AppCompatActivity {

    CardView home_my_report_back;
    RecyclerView home_my_report_rv;
    ImageView home_my_report_emo;
    int emo = 1; // -1 0 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);

        InitView();
    }

    private void InitView() {
        home_my_report_back = findViewById(R.id.home_my_report_back);
        home_my_report_rv = findViewById(R.id.home_my_report_rv);
        home_my_report_emo = findViewById(R.id.home_my_report_emo);

        home_my_report_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_my_report_emo.setOnClickListener(new View.OnClickListener() {
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
        if(emo == -1){
            home_my_report_emo.setImageResource(R.drawable.cry);
        } else if (emo == 0) {
            home_my_report_emo.setImageResource(R.drawable.meh);
        }else {
            home_my_report_emo.setImageResource(R.drawable.smile);
        }

        List<ReportCard> s = new ArrayList<>();
        s.add(new ReportCard("1","张樊","危险驾驶","危险驾驶危险驾驶危险驾驶"));
        s.add(new ReportCard("2","张樊","危险驾驶","危险驾驶"));
        MyReportAdapter adapter = new MyReportAdapter(s,this);
        home_my_report_rv.setAdapter(adapter);
        home_my_report_rv.setLayoutManager(new LinearLayoutManager(this));

    }
}