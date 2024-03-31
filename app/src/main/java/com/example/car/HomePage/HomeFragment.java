package com.example.car.HomePage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.HomePage.CarRescue.RescueActivity;
import com.example.car.HomePage.CarRescue.RescueManagerActivity;
import com.example.car.HomePage.Exercise.ExerciseActivity;
import com.example.car.HomePage.LicenseCheck.LicenseCheckActivity;
import com.example.car.HomePage.Report.MyReportActivity;
import com.example.car.HomePage.Report.ReportActivity;
import com.example.car.HomePage.Report.ReportCheckActivity;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {
    private View view;
    TextView home_morning;
    CardView home_button_move;
    CardView home_button_rescue;
    CardView home_button_rescue_manager;
    CardView home_card;
    CardView home_button_report;
    CardView home_button_report_handle;
    CardView home_button_record;
    CardView home_button_find;
    CardView home_button_license_check;
    CardView home_button_study;
    RelativeLayout home_card_not_found;
    LinearLayout home_manage_first;
    TextView home_manage_text;

    CircleImageView home_icon;

    TextView home_card_carName;
    TextView home_card_time;
    ImageView home_emo;
    String currentCarId;
    List<String> carsName = new ArrayList<>();
    List<String> carsId = new ArrayList<>();

    Boolean haveCard = false;
    ActivityResultLauncher<String[]> launcher_muti;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.rgb(133,168,191));
        InitView();
        InitCard();
        return view;
    }

    private void InitCard() {
        Call<ResponseBody> call = apiService.getCardInfo(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            if (jsonMap.get("haveCard").equals("1")) {
                                home_card_not_found.setVisibility(View.GONE);
                                home_card_carName.setText(jsonMap.get("brand")+" "+jsonMap.get("model"));
                                home_card_time.setText(jsonMap.get("endDate"));
                                if (jsonMap.get("status").equals("1")) {
                                    home_emo.setImageResource(R.drawable.smile);
                                }else if (jsonMap.get("status").equals("0")) {
                                    home_emo.setImageResource(R.drawable.meh);
                                } else {
                                    home_emo.setImageResource(R.drawable.cry);
                                }
                                currentCarId = jsonMap.get("carId");
                                haveCard = true;
                            } else {
                                home_card_not_found.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("cars"));
                                if (jsonArray != null) {
                                    for (Object obj : jsonArray) {
                                        JSONObject jsonObj = (JSONObject) obj;
                                        String carName = jsonObj.getString("brand") + jsonObj.getString("model");
                                        String carId = jsonObj.getString("carId");
                                        carsName.add(carName);
                                        carsId.add(carId);
                                    }
                                }
                                haveCard = false;
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void InitView() {
        launcher_muti = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null
                            && result.get(Manifest.permission.ACCESS_COARSE_LOCATION) != null) {
                        if (Objects.requireNonNull(result.get(Manifest.permission.ACCESS_FINE_LOCATION)).equals(true)
                                && Objects.requireNonNull(result.get(Manifest.permission.ACCESS_COARSE_LOCATION)).equals(true)) {
                            //权限全部获取到之后的动作
                            Intent intent = new Intent(requireActivity(), RescueActivity.class);
                            startActivity(intent);
                        } else {
                            //有权限没有获取到的动作
                            Toast.makeText(requireContext(), "未获取到定位权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        home_morning = view.findViewById(R.id.home_morning);
        home_button_move = view.findViewById(R.id.home_button_move);
        home_button_rescue = view.findViewById(R.id.home_button_rescue);
        home_button_rescue_manager = view.findViewById(R.id.home_button_rescue_manager);
        home_card = view.findViewById(R.id.home_card);
        home_button_report = view.findViewById(R.id.home_button_report);
        home_button_report_handle = view.findViewById(R.id.home_button_report_handle);
        home_button_record = view.findViewById(R.id.home_button_record);
        home_button_find = view.findViewById(R.id.home_button_find);
        home_button_license_check = view.findViewById(R.id.home_button_license_check);
        home_button_study = view.findViewById(R.id.home_button_study);
        home_card_not_found = view.findViewById(R.id.home_card_not_found);
        home_card_carName = view.findViewById(R.id.home_card_carName);
        home_card_time = view.findViewById(R.id.home_card_time);
        home_emo = view.findViewById(R.id.home_emo);
        home_icon = view.findViewById(R.id.home_icon);
        home_manage_first = view.findViewById(R.id.home_manage_first);
        home_manage_text = view.findViewById(R.id.home_manage_text);

        // get haveCard
        if(haveCard){
            home_card_not_found.setVisibility(View.GONE);
        }else {
            home_card_not_found.setVisibility(View.VISIBLE);
        }

        home_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveCard){
                    if (currentCarId != null) {
                        Intent intent = new Intent(requireContext(), CardActivity.class);
                        intent.putExtra("carId",currentCarId);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) requireContext(),home_card,"card");
                        startActivity(intent,optionsCompat.toBundle());
                    }
                }else {
                    OptionPicker picker = new OptionPicker(requireActivity());
                    picker.setData(carsName);
                    picker.setTitle("选择要绑定的车辆");
                    picker.setBodyWidth(140);
                    picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                        @Override
                        public void onOptionPicked(int position, Object item) {
                            Call<ResponseBody> call = apiService.addRequest(carsId.get(position));
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String responseString = response.body().string();
                                            Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                            if(jsonMap.get("code").equals("1")){
                                                Toast.makeText(requireContext(),"已提交审核:"+ carsName.get(position),Toast.LENGTH_LONG).show();
                                            }
                                        } catch (IOException e) {
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 处理失败的情况
                                    Toast.makeText(requireContext(),"网络错误",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    picker.show();
                }
            }
        });

        home_button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });

        home_button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), MyReportActivity.class);
                startActivity(intent);
            }
        });
        home_button_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), FindActivity.class);
                startActivity(intent);
            }
        });
        home_button_license_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), LicenseCheckActivity.class);
                startActivity(intent);
            }
        });
        home_button_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ExerciseActivity.class);
                startActivity(intent);
            }
        });

        home_button_report_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ReportCheckActivity.class);
                startActivity(intent);
            }
        });

        home_button_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), MoveCarActivity.class);
                startActivity(intent);
            }
        });
        home_button_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    // 请求权限
                    launcher_muti.launch(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION});
                }else{
                    Intent intent = new Intent(requireActivity(), RescueActivity.class);
                    startActivity(intent);
                }
            }
        });
        home_button_rescue_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    // 请求权限
                    launcher_muti.launch(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION});
                }else{
                    Intent intent = new Intent(requireActivity(), RescueManagerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        // 管理员内容展示
        if (UserInfo.Admin) {
            home_manage_first.setVisibility(View.VISIBLE);
            home_manage_text.setVisibility(View.VISIBLE);
        } else {
            home_manage_first.setVisibility(View.GONE);
            home_manage_text.setVisibility(View.GONE);
        }
        // 早上好
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        if (hour >= 6 && hour < 12){
            home_morning.setText("早上好，"+ UserInfo.UserName);
        } else if (hour >= 12 && hour < 18){
            home_morning.setText("下午好，"+ UserInfo.UserName);
        } else {
            home_morning.setText("晚上好，"+ UserInfo.UserName);
        }
        // 加载头像
        Glide.with(requireActivity())
                .load("http://182.92.87.107:8080/CarServerFile/userIcon/"+UserInfo.UserStuNum)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(home_icon);
    }
}