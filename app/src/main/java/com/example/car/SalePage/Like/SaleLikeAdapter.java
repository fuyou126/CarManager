package com.example.car.SalePage.Like;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.SaleCarActivity;
import com.example.car.SalePage.SaleCard;
import com.example.car.SalePage.SaleCardAdapter;

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

public class SaleLikeAdapter extends RecyclerView.Adapter<SaleLikeAdapter.ViewHolder> {
    private List<SaleCard> lists;
    private Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    public SaleLikeAdapter(List<SaleCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_like_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.sale_like_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaleCarActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.sale_like_card_picture,"sale_car_big_pic");
                intent.putExtra("carName",lists.get(position).carName);
                intent.putExtra("description",lists.get(position).description);
                intent.putExtra("price",lists.get(position).price);
                intent.putExtra("sellId",lists.get(position).sellId);
                intent.putExtra("stuNumber",lists.get(position).stuNumber);
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });
        //
        holder.sale_like_card_name.setText(lists.get(position).getCarName_viewer());
        holder.sale_like_card_description.setText(lists.get(position).getCarDescription_viewer());
        holder.sale_like_card_price.setText("￥" + lists.get(position).price);
        Glide.with(context)
                .load("http://182.92.87.107:8080/CarServerFile/sellCar/"+ lists.get(position).sellId)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(holder.sale_like_card_picture);
        holder.sale_like_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete liked
                SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog
                        .setTitleText("确认删除该出售信息吗?")
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
                                // delete car
                                Call<ResponseBody> call = apiService.deleteLike(UserInfo.UserStuNum,lists.get(position).sellId);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                String responseString = response.body().string();
                                                Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                                if(jsonMap.get("code").equals("1")){
                                                    lists.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context,"车辆收藏已删除",Toast.LENGTH_LONG).show();
                                                }
                                            } catch (IOException e) {
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        // 处理失败的情况
                                        Toast.makeText(context,"网络错误",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView sale_like_card;
        public ImageView sale_like_card_picture;
        public TextView sale_like_card_name;
        public TextView sale_like_card_description;
        public TextView sale_like_card_price;
        public CardView sale_like_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sale_like_card = itemView.findViewById(R.id.sale_like_card);
            sale_like_card_picture = itemView.findViewById(R.id.sale_like_card_picture);
            sale_like_card_name = itemView.findViewById(R.id.sale_like_card_name);
            sale_like_card_description = itemView.findViewById(R.id.sale_like_card_description);
            sale_like_card_price = itemView.findViewById(R.id.sale_like_card_price);
            sale_like_delete = itemView.findViewById(R.id.sale_like_delete);
        }
    }

}
