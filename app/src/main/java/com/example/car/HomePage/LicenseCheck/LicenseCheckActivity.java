package com.example.car.HomePage.LicenseCheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.car.HomePage.Report.ReportCard;
import com.example.car.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class LicenseCheckActivity extends AppCompatActivity {

    CardView home_license_check_back;
    SmartRefreshLayout home_license_check_fresh;
    RecyclerView home_license_check_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_check);
        
        InitView();
    }

    private void InitView() {
        home_license_check_back = findViewById(R.id.home_license_check_back);
        home_license_check_fresh = findViewById(R.id.home_license_check_fresh);
        home_license_check_rv = findViewById(R.id.home_license_check_rv);

        home_license_check_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<LicenseCard> s = new ArrayList<>();
        s.add(new LicenseCard("张樊","奥迪","A6","陕ACZ2U1A"));
        s.add(new LicenseCard("张樊","奥迪","A6","陕ACZ2U1"));
        LicenseAdapter adapter = new LicenseAdapter(s,this);
        home_license_check_rv.setAdapter(adapter);
        home_license_check_rv.setLayoutManager(new LinearLayoutManager(this));

    }
}