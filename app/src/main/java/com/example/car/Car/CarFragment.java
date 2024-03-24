package com.example.car.Car;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.HomePage.LicenseCheck.LicenseCard;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CarFragment extends Fragment {
    private View view;
    RecyclerView car_rv;
    ImageButton car_button_add;

    List<CarCard> list = new ArrayList<>();
    CarCardAdapter adapter = new CarCardAdapter(list,this);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    public CarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#f5f5f5"));
        InitView();
        return view;
    }

    private void InitView() {
        car_button_add = view.findViewById(R.id.car_button_add);
        car_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionPicker picker = new OptionPicker(requireActivity());
                picker.setData(Arrays.asList("汽车","摩托车","自行车"));
                picker.setTitle("选择要添加的车辆类型");
                picker.setBodyWidth(140);
                picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                    @Override
                    public void onOptionPicked(int position, Object item) {
                        String addType = "car";
                        Intent intent = new Intent(requireActivity(), CarAddActivity.class);
                        switch (position){
                            case 0:
                                addType = "car";
                                break;
                            case 1:
                                addType = "motor";
                                break;
                            case 2:
                                addType = "bike";
                                break;
                        }
                        intent.putExtra("addType",addType);
                        startActivity(intent);
                    }
                });
                picker.show();
            }
        });

        car_rv = view.findViewById(R.id.car_rv);
        car_rv.setAdapter(adapter);
        car_rv.setLayoutManager(new LinearLayoutManager(requireActivity()));

        getCarInfo();
    }

    protected void getCarInfo() {
        Call<ResponseBody> call = apiService.getMyCar(UserInfo.UserStuNum);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            list.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("cars"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String carNumber = jsonObj.getString("carNumber");
                                    String type = jsonObj.getString("type");
                                    boolean isSign = jsonObj.getString("isSign").equals("1");
                                    String brand = jsonObj.getString("brand");
                                    String model = jsonObj.getString("model");
                                    String carId = jsonObj.getString("carId");

                                    list.add(new CarCard(brand,model,type,carNumber,isSign,carId));
                                }
                                adapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        getCarInfo();
    }
}