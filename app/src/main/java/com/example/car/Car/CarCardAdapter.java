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

import com.example.car.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CarCardAdapter extends RecyclerView.Adapter<CarCardAdapter.ViewHolder> {

    private List<CarCard> lists;
    private Context context;

    public CarCardAdapter(List<CarCard> lists, Context context) {
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
                if(lists.get(position).isRegister){
                    Toast.makeText(context, "已登记车辆不可删除", Toast.LENGTH_SHORT).show();
                }else{
                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
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
                                    // TODO delete car
                                    lists.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"删除成功",Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        holder.car_name.setText(lists.get(position).carName);
        if(lists.get(position).carType.equals("Car")){
            holder.car_type_pic.setImageResource(R.drawable.car);
            holder.car_carNumber.setText(lists.get(position).getFormatCarNumber());
            if(lists.get(position).isRegister){
                holder.car_state_pic.setImageResource(R.drawable.registered);
                holder.car_state_text.setText("已登记");
            }else {
                holder.car_state_pic.setImageResource(R.drawable.unregistered);
                holder.car_state_text.setText("未登记");
            }
        } else if (lists.get(position).carType.equals("Motor")) {
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
}
