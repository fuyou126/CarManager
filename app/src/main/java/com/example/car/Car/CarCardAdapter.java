package com.example.car.Car;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarCardAdapter extends RecyclerView.Adapter<CarCardAdapter.ViewHolder> {

    private List<CarCard> lists;
    private CarFragment context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    public CarCardAdapter(List<CarCard> lists, CarFragment context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.car_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(lists.get(position).carType.equals("car")){
                    Toast.makeText(context.requireActivity(), "该车辆不可删除", Toast.LENGTH_SHORT).show();
                }else{
                    SweetAlertDialog pDialog = new SweetAlertDialog(context.requireActivity(), SweetAlertDialog.WARNING_TYPE);
                    pDialog
                            .setTitleText("确认删除车辆信息吗?")
                            .setContentText("删除后无法恢复")
                            .setContentTextSize(14)
                            .setCancelText("✖")
                            .setConfirmText("✔")
                            .setConfirmButtonBackgroundColor(Color.parseColor("#FF0000"))
                            .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                    delete(lists.get(position).carId,position);
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        holder.car_name.setText(lists.get(position).carName);
        if(lists.get(position).carType.equals("car")){
            holder.car_type_pic.setImageResource(R.drawable.car);
            holder.car_carNumber.setText(lists.get(position).getFormatCarNumber());
            if(lists.get(position).isRegister){
                holder.car_state_pic.setImageResource(R.drawable.registered);
                holder.car_state_text.setText("已登记");
            }else {
                holder.car_state_pic.setImageResource(R.drawable.unregistered);
                holder.car_state_text.setText("未登记");
            }
        } else if (lists.get(position).carType.equals("motor")) {
            holder.car_type_pic.setImageResource(R.drawable.motor);
            holder.car_carNumber.setText(lists.get(position).getFormatCarNumber());
            holder.car_state_pic.setVisibility(View.INVISIBLE);
            holder.car_state_text.setVisibility(View.INVISIBLE);
        }else {
            holder.car_type_pic.setImageResource(R.drawable.bike);
            holder.car_carNumber.setVisibility(View.INVISIBLE);
            holder.car_state_pic.setVisibility(View.INVISIBLE);
            holder.car_state_text.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView car_card;
        ImageView car_type_pic;
        TextView car_name;
        TextView car_carNumber;
        ImageView car_state_pic;
        TextView car_state_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            car_card = itemView.findViewById(R.id.car_card);
            car_type_pic = itemView.findViewById(R.id.car_type_pic);
            car_name = itemView.findViewById(R.id.car_name);
            car_carNumber = itemView.findViewById(R.id.car_carNumber);
            car_state_pic = itemView.findViewById(R.id.car_state_pic);
            car_state_text = itemView.findViewById(R.id.car_state_text);
        }
    }

    private void delete(String carId,int position){
        Call<ResponseBody> call = apiService.deleteCar(carId);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                        if(jsonMap.get("code").equals("1")){
                            context.getCarInfo();
                            Toast.makeText(context.requireActivity(),"删除成功",Toast.LENGTH_LONG).show();
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
}
