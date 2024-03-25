package com.example.car.HomePage.Exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.car.Car.CarCard;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.HomePage.Report.ReportDetailActivity;
import com.example.car.Info.UserInfo;
import com.example.car.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseActivity extends AppCompatActivity {

    CardView home_exercise_back;
    CardView home_exercise_ok;
    TextView home_exercise_ok_text;
    TextView home_exercise_content;
    RadioGroup home_exercise_group;
    RadioButton home_exercise_a;
    RadioButton home_exercise_b;
    RadioButton home_exercise_c;
    RadioButton home_exercise_d;
    TextView home_exercise_answer;
    TextView home_exercise_analysis;
    ImageView home_exercise_emo;
    ImageView home_exercise_pic;
    int emo = 0; // -1 0 1
    int needExercise = 3;
    String questionId;

    String content;
    String a;
    String b;
    String c;
    String d;
    int answerId;
    String analysis;
    boolean haveAnswer = false;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        InitView();
    }

    private void InitView() {
        home_exercise_back = findViewById(R.id.home_exercise_back);
        home_exercise_ok = findViewById(R.id.home_exercise_ok);
        home_exercise_ok_text = findViewById(R.id.home_exercise_ok_text);
        home_exercise_content = findViewById(R.id.home_exercise_content);
        home_exercise_group = findViewById(R.id.home_exercise_group);
        home_exercise_a = findViewById(R.id.home_exercise_a);
        home_exercise_b = findViewById(R.id.home_exercise_b);
        home_exercise_c = findViewById(R.id.home_exercise_c);
        home_exercise_d = findViewById(R.id.home_exercise_d);
        home_exercise_answer = findViewById(R.id.home_exercise_answer);
        home_exercise_analysis = findViewById(R.id.home_exercise_analysis);
        home_exercise_emo = findViewById(R.id.home_exercise_emo);
        home_exercise_pic = findViewById(R.id.home_exercise_pic);

        home_exercise_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_exercise_emo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emo == -1){
                    Toast.makeText(getApplicationContext(),"再做对"+ needExercise +"道题将提升信誉等级至一般",Toast.LENGTH_LONG).show();
                } else if (emo == 0) {
                    Toast.makeText(getApplicationContext(),"再做对"+ needExercise +"道题将提升信誉等级至优秀",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"当前信誉等级优秀！",Toast.LENGTH_LONG).show();
                }
            }
        });
        home_exercise_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveAnswer){
                    haveAnswer = false;
                    getNewQuestion();
                }else {
                    haveAnswer = true;
                    commitQuestion();
                }
            }
        });

        getFirstQuestion();
        getLearned();
    }

    private void getLearned() {
        Call<ResponseBody> call = apiService.getLearned(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            if (jsonMap.get("haveFound").equals("1")) {
                                needExercise = Integer.parseInt(Objects.requireNonNull(jsonMap.get("need")));
                                emo = Integer.parseInt(Objects.requireNonNull(jsonMap.get("status")));
                                if(emo == -1){
                                    home_exercise_emo.setImageResource(R.drawable.cry);
                                } else if (emo == 0) {
                                    home_exercise_emo.setImageResource(R.drawable.meh);
                                }else {
                                    home_exercise_emo.setImageResource(R.drawable.smile);
                                }
                            } else {
                                home_exercise_emo.setVisibility(View.GONE);
                            }
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

    private void getFirstQuestion() {
        Call<ResponseBody> call = apiService.getFirstQuestion();
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            home_exercise_a.setText("A、"+jsonMap.get("A"));
                            home_exercise_b.setText("B、"+jsonMap.get("B"));
                            home_exercise_c.setText("C、"+jsonMap.get("C"));
                            home_exercise_d.setText("D、"+jsonMap.get("D"));
                            home_exercise_content.setText(jsonMap.get("question"));
                            home_exercise_analysis.setText(jsonMap.get("description"));
                            questionId = jsonMap.get("questionId");
                            String answer_str = jsonMap.get("answer");
                            if (answer_str.equals("A")) {
                                answerId = home_exercise_a.getId();
                            } else if (answer_str.equals("B")) {
                                answerId = home_exercise_b.getId();
                            } else if (answer_str.equals("C")) {
                                answerId = home_exercise_c.getId();
                            } else {
                                answerId = home_exercise_d.getId();
                            }
                            Glide.with(ExerciseActivity.this)
                                    .load("http://182.92.87.107:8080/CarServerFile/question/"+questionId)
                                    .into(home_exercise_pic);
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

    private void getNewQuestion() {
        // 获取题目
        Call<ResponseBody> call = apiService.getQuestion(questionId);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            home_exercise_a.setText("A、"+jsonMap.get("A"));
                            home_exercise_b.setText("B、"+jsonMap.get("B"));
                            home_exercise_c.setText("C、"+jsonMap.get("C"));
                            home_exercise_d.setText("D、"+jsonMap.get("D"));
                            home_exercise_content.setText(jsonMap.get("question"));
                            home_exercise_analysis.setText(jsonMap.get("description"));
                            questionId = jsonMap.get("questionId");
                            String answer_str = jsonMap.get("answer");
                            if (answer_str.equals("A")) {
                                answerId = home_exercise_a.getId();
                            } else if (answer_str.equals("B")) {
                                answerId = home_exercise_b.getId();
                            } else if (answer_str.equals("C")) {
                                answerId = home_exercise_c.getId();
                            } else {
                                answerId = home_exercise_d.getId();
                            }
                            Glide.with(ExerciseActivity.this)
                                    .load("http://182.92.87.107:8080/CarServerFile/question/"+questionId)
                                    .into(home_exercise_pic);
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

        home_exercise_ok_text.setText("提交");
        home_exercise_answer.setVisibility(View.GONE);
        home_exercise_analysis.setVisibility(View.GONE);
    }
    private void commitQuestion(){
        int selected = home_exercise_group.getCheckedRadioButtonId();
        if(selected == answerId){
            home_exercise_answer.setText("回答正确");
            home_exercise_answer.setTextColor(Color.parseColor("#00B52D"));

            Call<ResponseBody> call = apiService.setLearned(UserInfo.UserStuNum);
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseString = response.body().string();
                            Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                            if(jsonMap.get("code").equals("1")){
                                getLearned();
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
        }else {
            home_exercise_answer.setTextColor(ContextCompat.getColor(this,R.color.red));
            if(answerId == home_exercise_a.getId()){
                home_exercise_answer.setText("回答错误，正确答案：A");
            } else if (answerId == home_exercise_b.getId()) {
                home_exercise_answer.setText("回答错误，正确答案：B");
            } else if (answerId == home_exercise_b.getId()) {
                home_exercise_answer.setText("回答错误，正确答案：C");
            }else {
                home_exercise_answer.setText("回答错误，正确答案：D");
            }
        }
        home_exercise_ok_text.setText("下一题");
        home_exercise_answer.setVisibility(View.VISIBLE);
        home_exercise_analysis.setVisibility(View.VISIBLE);
    }


}