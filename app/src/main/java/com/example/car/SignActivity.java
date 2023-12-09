package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignActivity extends AppCompatActivity {
    EditText sign_StuNumber,sign_phone,sign_password,sign_confirmPassword,sign_name;
    Button sign_button;
    TextView sign_toLogin;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        InitView();

    }

    private void InitView() {
        getWindow().setStatusBarColor(Color.rgb(134,146,247));
        sign_StuNumber=findViewById(R.id.sign_StuNumber);
        sign_phone=findViewById(R.id.sign_phone);
        sign_password=findViewById(R.id.sign_password);
        sign_confirmPassword=findViewById(R.id.sign_confirmPassword);
        sign_name = findViewById(R.id.sign_name);
        sign_button=findViewById(R.id.sign_button);
        sign_toLogin=findViewById(R.id.sign_toLogin);


        sign_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(sign_StuNumber.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学号不能为空",Toast.LENGTH_LONG).show();
                } else if (sign_phone.getText().toString().length()!=11) {
                    Toast.makeText(getApplicationContext(),"手机号格式不规范",Toast.LENGTH_LONG).show();
                } else if (sign_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"姓名不能为空",Toast.LENGTH_LONG).show();
                } else if (sign_password.getText().toString().equals(sign_confirmPassword.getText().toString())) {
                    Call<ResponseBody> call = apiService.sign(sign_StuNumber.getText().toString(),sign_name.getText().toString(),sign_phone.getText().toString(),sign_password.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String responseString = response.body().string();
                                    Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                    if(jsonMap.get("code").equals("1")){
                                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignActivity.this,loginActivity.class));
                                    }else if(jsonMap.get("code").equals("0")){
                                        Toast.makeText(getApplicationContext(),jsonMap.get("description"),Toast.LENGTH_LONG).show();
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
            }
        });
        sign_toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignActivity.this,loginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignActivity.this,loginActivity.class));
    }
}