package com.example.car.HomePage.LicenseCheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.HomePage.Report.MyReportAdapter;
import com.example.car.HomePage.Report.ReportCard;
import com.example.car.R;

import java.util.List;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder>{
    private List<LicenseCard> lists;
    private Context context;

    public LicenseAdapter(List<LicenseCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_license_check_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.home_license_check_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LicenseDetailActivity.class);
                intent.putExtra("username",lists.get(position).username);
                context.startActivity(intent);
            }
        });
        holder.home_license_check_username.setText(lists.get(position).username);
        holder.home_license_check_info.setText(lists.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView home_license_check_card;
        TextView home_license_check_username;
        TextView home_license_check_info;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_license_check_card = itemView.findViewById(R.id.home_license_check_card);
            home_license_check_username = itemView.findViewById(R.id.home_license_check_username);
            home_license_check_info = itemView.findViewById(R.id.home_license_check_info);
        }
    }
}
