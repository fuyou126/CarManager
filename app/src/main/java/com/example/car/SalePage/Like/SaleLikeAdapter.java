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

import com.example.car.R;
import com.example.car.SalePage.SaleCarActivity;
import com.example.car.SalePage.SaleCard;
import com.example.car.SalePage.SaleCardAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SaleLikeAdapter extends RecyclerView.Adapter<SaleLikeAdapter.ViewHolder> {
    private List<SaleCard> lists;
    private Context context;

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
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });
        //
        holder.sale_like_card_name.setText(lists.get(position).getCarName_viewer());
        holder.sale_like_card_description.setText(lists.get(position).getCarDescription_viewer());
        holder.sale_like_card_price.setText("￥" + lists.get(position).price);
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
                                lists.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                Toast.makeText(context,"车辆收藏已删除",Toast.LENGTH_LONG).show();
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
