package com.example.car.SalePage.Chat;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.car.MainActivity;
import com.example.car.R;
import com.example.car.SalePage.SaleCarActivity;
import com.example.car.SalePage.SaleCard;
import com.example.car.loginActivity;

import java.util.List;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ChatDetailAdapter_ViewHolder> {

    private List<ChatDetailCard> lists;
    private Context context;

    public ChatDetailAdapter(List<ChatDetailCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatDetailAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_chat_detail_item, parent, false);
        return new ChatDetailAdapter_ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatDetailAdapter_ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(lists.get(position).isSend){
            holder.sale_chat_detail_my_msg.setVisibility(View.VISIBLE);
            holder.sale_chat_detail_my_content.setText(lists.get(position).content);
            holder.sale_chat_detail_my_time.setText(lists.get(position).getTimeString());
            holder.sale_chat_detail_your_msg.setVisibility(View.INVISIBLE);
        }else{
            holder.sale_chat_detail_your_msg.setVisibility(View.VISIBLE);
            holder.sale_chat_detail_your_content.setText(lists.get(position).content);
            holder.sale_chat_detail_your_time.setText(lists.get(position).getTimeString());
            holder.sale_chat_detail_my_msg.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public static class ChatDetailAdapter_ViewHolder extends RecyclerView.ViewHolder {
        // 绑定控件
        public LinearLayout sale_chat_detail_your_msg;
        public TextView sale_chat_detail_your_content;
        public TextView sale_chat_detail_your_time;
        public LinearLayout sale_chat_detail_my_msg;
        public TextView sale_chat_detail_my_content;
        public TextView sale_chat_detail_my_time;
        public ChatDetailAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            sale_chat_detail_your_msg = itemView.findViewById(R.id.sale_chat_detail_your_msg);
            sale_chat_detail_your_content = itemView.findViewById(R.id.sale_chat_detail_your_content);
            sale_chat_detail_your_time = itemView.findViewById(R.id.sale_chat_detail_your_time);
            sale_chat_detail_my_msg = itemView.findViewById(R.id.sale_chat_detail_my_msg);
            sale_chat_detail_my_content = itemView.findViewById(R.id.sale_chat_detail_my_content);
            sale_chat_detail_my_time = itemView.findViewById(R.id.sale_chat_detail_my_time);

        }
    }

}
