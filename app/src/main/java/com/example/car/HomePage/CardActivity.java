package com.example.car.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.R;

import org.w3c.dom.Text;

public class CardActivity extends AppCompatActivity {

    CardView home_card_close;
    ImageView home_card_emo;
    TextView home_card_stuNumber;
    TextView home_card_username;
    TextView home_card_carName;
    TextView home_card_carNumber;
    TextView home_card_state;
    TextView home_card_date;
    TextView home_card_endDate;
    int emo = 1; // -1 0 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        
        InitView();
    }

    private void InitView() {
        home_card_close = findViewById(R.id.home_card_close);
        home_card_emo = findViewById(R.id.home_card_emo);
        home_card_stuNumber = findViewById(R.id.home_card_stuNumber);
        home_card_username = findViewById(R.id.home_card_username);
        home_card_carName = findViewById(R.id.home_card_carName);
        home_card_carNumber = findViewById(R.id.home_card_carNumber);
        home_card_state = findViewById(R.id.home_card_state);
        home_card_date = findViewById(R.id.home_card_date);
        home_card_endDate = findViewById(R.id.home_card_endDate);

        home_card_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_card_emo.setOnClickListener(new View.OnClickListener() {
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
            home_card_emo.setImageResource(R.drawable.cry);
        } else if (emo == 0) {
            home_card_emo.setImageResource(R.drawable.meh);
        }else {
            home_card_emo.setImageResource(R.drawable.smile);
        }
        // todo setData


    }
}