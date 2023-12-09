package com.example.car.HomePage.LicenseCheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.R;

public class LicenseDetailActivity extends AppCompatActivity {

    CardView home_license_check_detail_back;
    ImageView home_license_check_detail_pic;
    TextView home_license_check_detail_stuNumber;
    TextView home_license_check_detail_username;
    TextView home_license_check_detail_carNumber;
    TextView home_license_check_detail_phone;
    TextView home_license_check_detail_carName;
    CardView home_license_check_detail_no;
    CardView home_license_check_detail_yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_detail);

        InitView();
    }

    private void InitView() {
        home_license_check_detail_back = findViewById(R.id.home_license_check_detail_back);
        home_license_check_detail_pic = findViewById(R.id.home_license_check_detail_pic);
        home_license_check_detail_stuNumber = findViewById(R.id.home_license_check_detail_stuNumber);
        home_license_check_detail_username = findViewById(R.id.home_license_check_detail_username);
        home_license_check_detail_carNumber = findViewById(R.id.home_license_check_detail_carNumber);
        home_license_check_detail_phone = findViewById(R.id.home_license_check_detail_phone);
        home_license_check_detail_carName = findViewById(R.id.home_license_check_detail_carName);
        home_license_check_detail_no = findViewById(R.id.home_license_check_detail_no);
        home_license_check_detail_yes = findViewById(R.id.home_license_check_detail_yes);

        home_license_check_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // todo setText

        home_license_check_detail_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo no
                finish();
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
            }
        });
        home_license_check_detail_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo yes
                finish();
                Toast.makeText(getApplicationContext(),"已完成审核",Toast.LENGTH_LONG).show();
            }
        });
    }
}