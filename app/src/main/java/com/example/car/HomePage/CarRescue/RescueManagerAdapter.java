package com.example.car.HomePage.CarRescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RescueManagerAdapter extends RecyclerView.Adapter<RescueManagerAdapter.RescueManagerAdapter_ViewHolder>{
    private List<RescueCard> lists;
    private Context context;
    private OnItemClickListener listener;

    public RescueManagerAdapter(List<RescueCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public RescueManagerAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rescue_manager_list_item, parent, false);
        return new RescueManagerAdapter.RescueManagerAdapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RescueManagerAdapter.RescueManagerAdapter_ViewHolder holder, int position) {
        // set image
        holder.home_rescue_manager_list_item_description.setText(lists.get(position).getRescue_description_str());
        holder.home_rescue_manager_list_item_pos.setText(lists.get(position).address);
        holder.home_rescue_manager_list_item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(holder.getBindingAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    public static class RescueManagerAdapter_ViewHolder extends RecyclerView.ViewHolder {
        // 绑定控件
        public CircleImageView home_rescue_manager_list_item_pic;
        public TextView home_rescue_manager_list_item_description;
        public TextView home_rescue_manager_list_item_pos;
        public CardView home_rescue_manager_list_item_card;
        public RescueManagerAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_rescue_manager_list_item_pic = itemView.findViewById(R.id.home_rescue_manager_list_item_pic);
            home_rescue_manager_list_item_description = itemView.findViewById(R.id.home_rescue_manager_list_item_description);
            home_rescue_manager_list_item_pos = itemView.findViewById(R.id.home_rescue_manager_list_item_pos);
            home_rescue_manager_list_item_card = itemView.findViewById(R.id.home_rescue_manager_list_item_card);
        };
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
