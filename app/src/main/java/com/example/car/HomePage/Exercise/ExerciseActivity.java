package com.example.car.HomePage.Exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.R;

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
    int emo = 0; // -1 0 1
    int needExercise = 3;

    String content;
    String a;
    String b;
    String c;
    String d;
    int answerId;
    String analysis;
    Boolean haveAnswer = false;

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
                    getNewQuestion();
                    haveAnswer = false;
                }else {
                    commitQuestion();
                    haveAnswer = true;
                }
            }
        });

        if(emo == -1){
            home_exercise_emo.setImageResource(R.drawable.cry);
        } else if (emo == 0) {
            home_exercise_emo.setImageResource(R.drawable.meh);
        }else {
            home_exercise_emo.setImageResource(R.drawable.smile);
        }

        getNewQuestion();
    }

    private void getNewQuestion() {
        // 获取题目
        answerId = home_exercise_a.getId();

        home_exercise_ok_text.setText("提交");
        home_exercise_answer.setVisibility(View.GONE);
        home_exercise_analysis.setVisibility(View.GONE);
    }
    private void commitQuestion(){
        int selected = home_exercise_group.getCheckedRadioButtonId();
        if(selected == answerId){
            home_exercise_answer.setText("回答正确");
            home_exercise_answer.setTextColor(Color.parseColor("#00B52D"));

            if(emo == -1){
                needExercise--;
                // 提交做题数
                if(needExercise == 0){
                    needExercise = 500;
                    // 信誉等级升级 提交
                    emo = 0;
                    home_exercise_emo.setImageResource(R.drawable.meh);
                }
            }else if (emo == 0){
                needExercise--;
                // 提交做题数
                if(needExercise == 0){
                    needExercise = 500;
                    // 信誉等级升级 提交
                    emo = 1;
                    home_exercise_emo.setImageResource(R.drawable.smile);
                }
            }
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