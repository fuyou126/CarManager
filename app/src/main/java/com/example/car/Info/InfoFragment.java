package com.example.car.Info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;
import com.example.car.loginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class InfoFragment extends Fragment {
    private View view;
    CardView info_exit;
    CircleImageView info_icon;

    TextView info_username;
    TextView info_stuNumber;
    TextView info_phone;
    TextView info_carName;
    TextView info_status;
    TextView info_learned;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    public InfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.rgb(100, 46, 208));
        InitView();
        InitData();
        return view;
    }

    private void InitData() {
        info_username.setText(UserInfo.UserName);
        info_stuNumber.setText(UserInfo.UserStuNum);

        Call<ResponseBody> call = apiService.getInfo(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            info_learned.setText(jsonMap.get("learned"));
                            info_phone.setText(jsonMap.get("phone"));
                            if (jsonMap.get("haveCard").equals("1")) {
                                info_carName.setText(jsonMap.get("brand") + " " + jsonMap.get("model"));
                                if (jsonMap.get("status").equals("1")) {
                                    info_status.setText("优秀");
                                } else if (jsonMap.get("status").equals("0")) {
                                    info_status.setText("一般");
                                } else {
                                    info_status.setText("较差");
                                }
                            } else {

                            }
                        }
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理失败的情况
                Toast.makeText(requireActivity(),"网络错误",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void InitView() {
        info_username = view.findViewById(R.id.info_username);
        info_stuNumber = view.findViewById(R.id.info_stuNumber);
        info_phone = view.findViewById(R.id.info_phone);
        info_carName = view.findViewById(R.id.info_carName);
        info_status = view.findViewById(R.id.info_status);
        info_learned = view.findViewById(R.id.info_learned);

        info_icon = view.findViewById(R.id.info_icon);
        info_exit = view.findViewById(R.id.info_exit);
        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(R.layout.info_upload_sheet);
                bottomSheetDialog.setDismissWithAnimation(true);
                LinearLayout info_upload_take_now = bottomSheetDialog.findViewById(R.id.info_upload_take_now);
                LinearLayout info_upload_take_from = bottomSheetDialog.findViewById(R.id.info_upload_take_from);
                bottomSheetDialog.show();

                info_upload_take_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(requireActivity(), TakePhotoActivity.class);
                        startActivity(intent);
                    }
                });
                info_upload_take_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(requireActivity(), TakePictureActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        info_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo.setUserName("");
                UserInfo.setUserStuNum("");
                Intent intent = new Intent(requireActivity(), loginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(requireActivity())
                .load("http://182.92.87.107:8080/CarServerFile/userIcon/"+UserInfo.UserStuNum)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(info_icon);
    }
}
