package com.example.car.SalePage;

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
import android.widget.TextView;

import com.example.car.R;

import java.util.List;

public class SaleCardAdapter extends RecyclerView.Adapter<SaleCardAdapter.ViewHolder> {

    private List<SaleCard> lists;
    private Context context;

    public SaleCardAdapter(List<SaleCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_car_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //设置数据
//        holder.sale_card.setText(lists.get(position));
        holder.sale_card_name.setText(lists.get(position).getCarName_viewer());
        holder.sale_card_description.setText(lists.get(position).getCarDescription_viewer());
        holder.sale_card_price.setText("￥" + lists.get(position).price);
        holder.sale_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaleCarActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.sale_card_picture,"sale_car_big_pic");
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 绑定控件
//        public TextView rv_textview;
        // image
        public TextView sale_card_name;
        public TextView sale_card_description;
        public TextView sale_card_price;
        public CardView sale_card;
        public ImageView sale_card_picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sale_card_name = itemView.findViewById(R.id.sale_card_name);
            sale_card_description = itemView.findViewById(R.id.sale_card_description);
            sale_card_price = itemView.findViewById(R.id.sale_card_price);
            sale_card = itemView.findViewById(R.id.sale_card);
            sale_card_picture = itemView.findViewById(R.id.sale_card_picture);

        }
    }

}
