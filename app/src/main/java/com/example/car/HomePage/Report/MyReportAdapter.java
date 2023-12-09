package com.example.car.HomePage.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.ViewHolder>{
    private List<ReportCard> lists;
    private Context context;

    public MyReportAdapter(List<ReportCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_my_report_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.home_my_report_detail_type.setText(lists.get(position).type);
        holder.home_my_report_detail_description.setText(lists.get(position).description);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView home_my_report_detail_type;
        TextView home_my_report_detail_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_my_report_detail_type = itemView.findViewById(R.id.home_my_report_detail_type);
            home_my_report_detail_description = itemView.findViewById(R.id.home_my_report_detail_description);
        }
    }
}
