package com.example.car.HomePage.CarRescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.example.car.Car.CarCard;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.Chat.ChatDetailCard;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RescueManagerActivity extends AppCompatActivity  implements RescueManagerAdapter.OnItemClickListener{
    Context context = this;
    CardView home_rescue_manager_back;
    CardView home_rescue_manager_list;
    CardView home_rescue_manager_list_panel;
    CardView home_rescue_manager_list_panel_close;
    RecyclerView home_rescue_manager_list_rv;
    List<RescueCard> list = new ArrayList<>();
    RescueManagerAdapter adapter;

    MapView home_rescue_manager_map;
    MyLocationStyle myLocationStyle; // 定位蓝点
    AMap aMap; // 地图控制器
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;
    AMapLocationListener mLocationListener;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_manager);

        home_rescue_manager_map =  findViewById(R.id.home_rescue_manager_map);
        home_rescue_manager_map.onCreate(savedInstanceState);

        InitView();
    }

    private void InitView() {
        // 用户同意隐私
        AMapLocationClient.updatePrivacyShow(context,true,true);
        AMapLocationClient.updatePrivacyAgree(context,true);
        ServiceSettings.updatePrivacyShow(context,true,true);
        ServiceSettings.updatePrivacyAgree(context,true);

        home_rescue_manager_list_panel = findViewById(R.id.home_rescue_manager_list_panel);
        home_rescue_manager_list = findViewById(R.id.home_rescue_manager_list);
        home_rescue_manager_back = findViewById(R.id.home_rescue_manager_back);
        home_rescue_manager_list_panel_close = findViewById(R.id.home_rescue_manager_list_panel_close);
        home_rescue_manager_list_rv = findViewById(R.id.home_rescue_manager_list_rv);

        home_rescue_manager_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        home_rescue_manager_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofFloat(home_rescue_manager_list_panel, "alpha", 0f, 1f)
                        .setDuration(300)
                        .start();
                ObjectAnimator.ofFloat(home_rescue_manager_list, "alpha", 1f, 0f)
                        .setDuration(300)
                        .start();
                home_rescue_manager_list_panel.setVisibility(View.VISIBLE);
                home_rescue_manager_list.setVisibility(View.GONE);
            }
        });
        home_rescue_manager_list_panel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofFloat(home_rescue_manager_list, "alpha", 0f, 1f)
                        .setDuration(300)
                        .start();
                ObjectAnimator.ofFloat(home_rescue_manager_list_panel, "alpha", 1f, 0f)
                        .setDuration(300)
                        .start();
                home_rescue_manager_list_panel.setVisibility(View.GONE);
                home_rescue_manager_list.setVisibility(View.VISIBLE);
            }
        });

        // 配置适配器
        adapter = new RescueManagerAdapter(list,this);
        adapter.setOnItemClickListener(this);
        home_rescue_manager_list_rv.setAdapter(adapter);
        home_rescue_manager_list_rv.setLayoutManager(new LinearLayoutManager(this));


        mLocationListener = aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Toast.makeText(getApplicationContext(),"定位失败，错误码" + aMapLocation.getErrorCode()+aMapLocation.getErrorInfo(),Toast.LENGTH_LONG).show();
                }
            }
        };
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        aMap = home_rescue_manager_map.getMap();
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.animateCamera(CameraUpdateFactory.zoomTo(17.0F));
        aMap.setMyLocationEnabled(true);
    }

    private void getReportList(){

        Call<ResponseBody> call = apiService.getRescueList();
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(Objects.equals(jsonMap.get("code"), "1")){
                            list.clear();
                            JSONArray jsonArray = JSONArray.parseArray(jsonMap.get("rescues"));
                            if (jsonArray != null) {
                                for (Object obj : jsonArray) {
                                    JSONObject jsonObj = (JSONObject) obj;
                                    String rescueId = jsonObj.getString("rescueId");
                                    String username = jsonObj.getString("username");
                                    String type = jsonObj.getString("type");
                                    String description = jsonObj.getString("description");
                                    String phone = jsonObj.getString("phone");
                                    String position = jsonObj.getString("position");
                                    double longitude = Double.parseDouble(jsonObj.getString("longitude"));
                                    double latitude = Double.parseDouble(jsonObj.getString("latitude"));
                                    String stuNumber = jsonObj.getString("stuNumber");

                                    int type_int;
                                    if (type.equals("拖车")) {
                                        type_int = 1;
                                    } else if (type.equals("换胎")) {
                                        type_int = 2;
                                    } else {
                                        type_int = 3;
                                    }
                                    list.add(new RescueCard(username,type_int,rescueId,latitude,longitude,position,description,phone,stuNumber));
                                }
                                adapter.notifyDataSetChanged();

                                // 绘制点标记
                                List<String> markerId = new ArrayList<>();
                                aMap.clear();
                                myLocationStyle = new MyLocationStyle();
                                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                                myLocationStyle.showMyLocation(true);
                                aMap.setMyLocationStyle(myLocationStyle);
                                aMap.getUiSettings().setMyLocationButtonEnabled(true);
                                aMap.animateCamera(CameraUpdateFactory.zoomTo(17.0F));
                                aMap.setMyLocationEnabled(true);
                                aMap.invalidate();
                                for(int i=0;i<list.size();i++){
                                    RescueCard rescueCard = list.get(i);
                                    LatLng latLng = new LatLng(rescueCard.latitude,rescueCard.longitude);
                                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(rescueCard.username).snippet("需要"+rescueCard.getRescue_type_str()));
                                    markerId.add(marker.getId());
                                }
                                aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        for(int i =0;i<markerId.size();i++){
                                            if(marker.getId().equals(markerId.get(i))){
                                                home_rescue_manager_list_panel.setVisibility(View.GONE);

                                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RescueManagerActivity.this);
                                                bottomSheetDialog.setContentView(R.layout.home_rescue_manager_helper_sheet);
                                                bottomSheetDialog.setDismissWithAnimation(true);
                                                bottomSheetDialog.show();
                                                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialogInterface) {
                                                        ObjectAnimator.ofFloat(home_rescue_manager_list, "alpha", 0f, 1f)
                                                                .setDuration(300)
                                                                .start();
                                                        home_rescue_manager_list.setVisibility(View.VISIBLE);
                                                    }
                                                });

                                                TextView home_rescue_manager_detail_username = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_username);
                                                TextView home_rescue_manager_detail_type = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_type);
                                                TextView home_rescue_manager_detail_description = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_description);
                                                TextView home_rescue_manager_detail_phone = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_phone);
                                                TextView home_rescue_manager_detail_position = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_position);
                                                CardView home_rescue_manager_detail_cancel = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_cancel);
                                                CardView home_rescue_manager_detail_confirm = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_confirm);

                                                if(i < list.size()){
                                                    RescueCard rescueCard = list.get(i);
                                                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(rescueCard.latitude,rescueCard.longitude),17.0f));
                                                    home_rescue_manager_detail_username.setText("用户:"+rescueCard.username);
                                                    home_rescue_manager_detail_type.setText("救援类型:"+rescueCard.getRescue_type_str());
                                                    home_rescue_manager_detail_description.setText("备注:"+rescueCard.description);
                                                    home_rescue_manager_detail_phone.setText("联系电话:"+rescueCard.phone);
                                                    home_rescue_manager_detail_position.setText("位置:"+rescueCard.address);
                                                    home_rescue_manager_detail_cancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            bottomSheetDialog.dismiss();
                                                        }
                                                    });
                                                    home_rescue_manager_detail_confirm.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            bottomSheetDialog.dismiss();

                                                            // handle

                                                            Toast.makeText(getApplicationContext(),rescueCard.username+"的救援请求已处理",Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                }
                                            }
                                        }
                                        return true;
                                    }
                                });
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行home_rescue_map.onDestroy()，销毁地图
        home_rescue_manager_map.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行home_rescue_map.onResume ()，重新绘制加载地图
        home_rescue_manager_map.onResume();
        getReportList();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行home_rescue_map.onPause ()，暂停地图的绘制
        home_rescue_manager_map.onPause();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行home_rescue_map.onSaveInstanceState (outState)，保存地图当前的状态
        home_rescue_manager_map.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int position) {
        home_rescue_manager_list_panel.setVisibility(View.GONE);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RescueManagerActivity.this);
        bottomSheetDialog.setContentView(R.layout.home_rescue_manager_helper_sheet);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ObjectAnimator.ofFloat(home_rescue_manager_list, "alpha", 0f, 1f)
                        .setDuration(300)
                        .start();
                home_rescue_manager_list.setVisibility(View.VISIBLE);
            }
        });

        TextView home_rescue_manager_detail_username = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_username);
        TextView home_rescue_manager_detail_type = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_type);
        TextView home_rescue_manager_detail_description = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_description);
        TextView home_rescue_manager_detail_phone = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_phone);
        TextView home_rescue_manager_detail_position = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_position);
        CardView home_rescue_manager_detail_cancel = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_cancel);
        CardView home_rescue_manager_detail_confirm = bottomSheetDialog.findViewById(R.id.home_rescue_manager_detail_confirm);

        if(position < list.size()){
            RescueCard rescueCard = list.get(position);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(rescueCard.latitude,rescueCard.longitude),17.0f));
            home_rescue_manager_detail_username.setText("用户:"+rescueCard.username);
            home_rescue_manager_detail_type.setText("救援类型:"+rescueCard.getRescue_type_str());
            home_rescue_manager_detail_description.setText("备注:"+rescueCard.description);
            home_rescue_manager_detail_phone.setText("联系电话:"+rescueCard.phone);
            home_rescue_manager_detail_position.setText("位置:"+rescueCard.address);
            home_rescue_manager_detail_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
            home_rescue_manager_detail_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // handle
                    Call<ResponseBody> call = apiService.deleteRescue(rescueCard.rescueId);
                    call.enqueue(new Callback<ResponseBody>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String responseString = response.body().string();
                                    Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                    if(jsonMap.get("code").equals("1")){
                                        getReportList();
                                        bottomSheetDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),rescueCard.username+"的救援请求已处理",Toast.LENGTH_LONG).show();
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
            });

        }


    }
}