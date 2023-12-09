package com.example.car.HomePage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.car.HomeActivity;
import com.example.car.HomePage.CarRescue.RescueActivity;
import com.example.car.HomePage.CarRescue.RescueManagerActivity;
import com.example.car.HomePage.Exercise.ExerciseActivity;
import com.example.car.HomePage.LicenseCheck.LicenseCheckActivity;
import com.example.car.HomePage.Report.MyReportActivity;
import com.example.car.HomePage.Report.ReportActivity;
import com.example.car.HomePage.Report.ReportCheckActivity;
import com.example.car.MainActivity;
import com.example.car.R;
import com.example.car.loginActivity;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.SplittableRandom;


public class HomeFragment extends Fragment {
    private View view;
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
    Boolean haveCard = false;
    ActivityResultLauncher<String[]> launcher_muti;
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
        return view;
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
                    Intent intent = new Intent(requireContext(), CardActivity.class);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) requireContext(),home_card,"card");
                    startActivity(intent,optionsCompat.toBundle());
                }else {
                    OptionPicker picker = new OptionPicker(requireActivity());
                    List<String> s = new ArrayList<>();
                    List<String> carId = new ArrayList<>();
                    s.add("奥迪 A6");
                    carId.add("1");
                    s.add("奥迪 A8");
                    carId.add("2");
                    picker.setData(s);
                    picker.setTitle("选择要绑定的车辆");
                    picker.setBodyWidth(140);
                    picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                        @Override
                        public void onOptionPicked(int position, Object item) {
                            // todo upload
                            carId.get(position);
                            home_card_not_found.setVisibility(View.GONE);
                            haveCard = true;
                            Toast.makeText(requireContext(),"已成功绑定车辆:"+s.get(position),Toast.LENGTH_LONG).show();
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
}