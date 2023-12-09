package com.example.car.HomePage.CarRescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.car.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RescueActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener{
    Context context = this;
    CardView home_rescue_back;
    CardView home_rescue_help;
    TextView home_rescue_position;
    int selected = 1;

    MapView home_rescue_map;
    MyLocationStyle myLocationStyle; // 定位蓝点
    AMap aMap; // 地图控制器
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;
    GeocodeSearch geocoderSearch;// 位置搜索

    AMapLocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);

        home_rescue_map =  findViewById(R.id.home_rescue_map);
        home_rescue_map.onCreate(savedInstanceState);

        try {
            InitView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void InitView() throws Exception {

        // 用户同意隐私
        AMapLocationClient.updatePrivacyShow(context,true,true);
        AMapLocationClient.updatePrivacyAgree(context,true);
        ServiceSettings.updatePrivacyShow(context,true,true);
        ServiceSettings.updatePrivacyAgree(context,true);

        home_rescue_back = findViewById(R.id.home_rescue_back);
        home_rescue_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        home_rescue_help = findViewById(R.id.home_rescue_help);
        home_rescue_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RescueActivity.this);
                bottomSheetDialog.setContentView(R.layout.home_rescue_helper_sheet);
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.show();

                // 已经有请求
                if(false){
                    RelativeLayout home_rescue_waiting = bottomSheetDialog.findViewById(R.id.home_rescue_waiting);
                    home_rescue_waiting.setVisibility(View.VISIBLE);
                }

                home_rescue_position = bottomSheetDialog.findViewById(R.id.home_rescue_position);
                CameraPosition cameraPosition = aMap.getCameraPosition();
                LatLng target = cameraPosition.target;
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(target.latitude,target.longitude), 200,GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);


                CardView home_rescue_card_1 = bottomSheetDialog.findViewById(R.id.home_rescue_card_1);
                CardView home_rescue_card_2 = bottomSheetDialog.findViewById(R.id.home_rescue_card_2);
                CardView home_rescue_card_3 = bottomSheetDialog.findViewById(R.id.home_rescue_card_3);
                ImageView home_rescue_button_1 = bottomSheetDialog.findViewById(R.id.home_rescue_button_1);
                ImageView home_rescue_button_2 = bottomSheetDialog.findViewById(R.id.home_rescue_button_2);
                ImageView home_rescue_button_3 = bottomSheetDialog.findViewById(R.id.home_rescue_button_3);
                TextView home_rescue_text_1 = bottomSheetDialog.findViewById(R.id.home_rescue_text_1);
                TextView home_rescue_text_2 = bottomSheetDialog.findViewById(R.id.home_rescue_text_2);
                TextView home_rescue_text_3 = bottomSheetDialog.findViewById(R.id.home_rescue_text_3);
                EditText home_rescue_description = bottomSheetDialog.findViewById(R.id.home_rescue_description);
                CardView home_rescue_button_cancel = bottomSheetDialog.findViewById(R.id.home_rescue_button_cancel);
                CardView home_rescue_button_confirm = bottomSheetDialog.findViewById(R.id.home_rescue_button_confirm);

                home_rescue_button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                home_rescue_card_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected = 1;
                        home_rescue_button_1.setImageResource(cn.pedant.SweetAlert.R.color.blue_btn_bg_color);
                        home_rescue_text_1.setTextColor(Color.rgb(255,255,255));
                        home_rescue_button_2.setImageResource(R.color.white);
                        home_rescue_text_2.setTextColor(Color.rgb(0,0,0));
                        home_rescue_button_3.setImageResource(R.color.white);
                        home_rescue_text_3.setTextColor(Color.rgb(0,0,0));
                    }
                });
                home_rescue_card_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected = 2;
                        home_rescue_button_1.setImageResource(R.color.white);
                        home_rescue_text_1.setTextColor(Color.rgb(0,0,0));
                        home_rescue_button_2.setImageResource(cn.pedant.SweetAlert.R.color.blue_btn_bg_color);
                        home_rescue_text_2.setTextColor(Color.rgb(255,255,255));
                        home_rescue_button_3.setImageResource(R.color.white);
                        home_rescue_text_3.setTextColor(Color.rgb(0,0,0));
                    }
                });
                home_rescue_card_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected = 3;
                        home_rescue_button_1.setImageResource(R.color.white);
                        home_rescue_text_1.setTextColor(Color.rgb(0,0,0));
                        home_rescue_button_2.setImageResource(R.color.white);
                        home_rescue_text_2.setTextColor(Color.rgb(0,0,0));
                        home_rescue_button_3.setImageResource(cn.pedant.SweetAlert.R.color.blue_btn_bg_color);
                        home_rescue_text_3.setTextColor(Color.rgb(255,255,255));
                    }
                });
                //
                home_rescue_button_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String description = home_rescue_description.getText().toString();
                        // send selected description pos
                        if(true){
                            RelativeLayout home_rescue_waiting = bottomSheetDialog.findViewById(R.id.home_rescue_waiting);
                            home_rescue_waiting.setVisibility(View.VISIBLE);
                            TextView home_rescue_waiting_type = bottomSheetDialog.findViewById(R.id.home_rescue_waiting_type);
                            if(selected == 1){
                                home_rescue_waiting_type.setText("救援类型:拖车");
                            } else if (selected == 2) {
                                home_rescue_waiting_type.setText("救援类型:换胎");
                            }else {
                                home_rescue_waiting_type.setText("救援类型:其他");
                            }
                            TextView home_rescue_waiting_position = bottomSheetDialog.findViewById(R.id.home_rescue_waiting_position);
                            home_rescue_waiting_position.setText("位置:"+home_rescue_position.getText().toString());
                            TextView home_rescue_waiting_description = bottomSheetDialog.findViewById(R.id.home_rescue_waiting_description);
                            home_rescue_waiting_description.setText("备注:"+description);
                        }
                    }
                });

            }
        });


        mLocationListener = aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);

                    Toast.makeText(getApplicationContext(),aMapLocation.getAddress(),Toast.LENGTH_LONG).show();
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
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        aMap = home_rescue_map.getMap();

        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.animateCamera(CameraUpdateFactory.zoomTo(17.0F));
        aMap.setMyLocationEnabled(true);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行home_rescue_map.onDestroy()，销毁地图
        home_rescue_map.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行home_rescue_map.onResume ()，重新绘制加载地图
        home_rescue_map.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行home_rescue_map.onPause ()，暂停地图的绘制
        home_rescue_map.onPause();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行home_rescue_map.onSaveInstanceState (outState)，保存地图当前的状态
        home_rescue_map.onSaveInstanceState(outState);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if(i==1000) {
            RegeocodeAddress res = regeocodeResult.getRegeocodeAddress();
            String position_format = res.getFormatAddress();
            home_rescue_position.setText(position_format);
        }else{
            home_rescue_position.setText(String.valueOf(i));
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
    }


}