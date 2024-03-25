package com.example.car.HomePage.Report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.MainActivity;
import com.example.car.R;
import com.example.car.SalePage.SaleCardAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>{
    private List<ReportCard> lists;
    private Context context;

    public ReportAdapter(List<ReportCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_report_check_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.home_rescue_check_type.setText(lists.get(position).type);
        holder.home_rescue_check_description.setText(lists.get(position).getDescriptionSimple());
        Glide.with(context)
                .load("http://182.92.87.107:8080/CarServerFile/reportImage/"+lists.get(position).reportId)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(holder.home_rescue_check_pic);
        holder.home_rescue_check_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReportDetailActivity.class);
                intent.putExtra("reportId",lists.get(position).reportId);
                intent.putExtra("carId",lists.get(position).carId);
                intent.putExtra("stuNumber",lists.get(position).stuNumber);
                intent.putExtra("carNumber",lists.get(position).carNumber);
                intent.putExtra("description",lists.get(position).description);
                intent.putExtra("type",lists.get(position).type);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.home_rescue_check_pic,"pic");
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView home_rescue_check_card;
        CircleImageView home_rescue_check_pic;
        TextView home_rescue_check_type;
        TextView home_rescue_check_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_rescue_check_card = itemView.findViewById(R.id.home_rescue_check_card);
            home_rescue_check_pic = itemView.findViewById(R.id.home_rescue_check_pic);
            home_rescue_check_type = itemView.findViewById(R.id.home_rescue_check_type);
            home_rescue_check_description = itemView.findViewById(R.id.home_rescue_check_description);
        }
    }
}
