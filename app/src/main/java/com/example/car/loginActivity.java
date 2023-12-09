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
import android.util.Log;

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

public class loginActivity extends AppCompatActivity {

    EditText login_StuNumber,login_password;
    Button login_button;
    TextView toSign;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(Color.rgb(134,146,247));

        InitView();

    }

    private void InitView() {
        login_StuNumber=findViewById(R.id.login_stuNumber);
        login_password=findViewById(R.id.login_password);
        login_button=findViewById(R.id.login_button);
        toSign=findViewById(R.id.login_toSign);

        toSign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(loginActivity.this,SignActivity.class));
            }
        });
        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String StuNumber = login_StuNumber.getText().toString();
                String password = login_password.getText().toString();
                if(StuNumber.isEmpty()){
                    Toast.makeText(getApplicationContext(),"学号为空",Toast.LENGTH_LONG).show();
                }else{
                    Call<ResponseBody> call = apiService.login(StuNumber,password);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String responseString = response.body().string();
                                    Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                    if(jsonMap.get("code").equals("1")){
                                        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                                        UserInfo.setUserStuNum(StuNumber);
                                        UserInfo.admin = jsonMap.get("admin").equals("1");
                                        Intent intent = new Intent(loginActivity.this,HomeActivity.class);
                                        intent.putExtra("StuNumber",StuNumber);
                                        startActivity(intent);
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
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }
}