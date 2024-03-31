package com.example.car.SalePage.Chat;

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
import com.example.car.SalePage.SaleCard;
import com.example.car.SalePage.SaleCardAdapter;
import com.example.car.loginActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<MessageCard> lists;
    private Context context;

    public MessageAdapter(List<MessageCard> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_chat_card_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.sale_chat_list_username.setText(lists.get(position).getName_viewer());
        holder.sale_chat_list_last_message.setText(lists.get(position).getLastMessage_viewer());
//         消息气泡逻辑
        int unseen = lists.get(position).getUnseenMessages_viewer();
        if(unseen == -1){
            holder.sale_chat_list_unseen_num.setVisibility(View.VISIBLE);
            holder.sale_chat_list_unseen_num.setText("");
        }else if(unseen == 0){
            holder.sale_chat_list_unseen_num.setVisibility(View.INVISIBLE);
        }else{
            holder.sale_chat_list_unseen_num.setVisibility(View.VISIBLE);
            holder.sale_chat_list_unseen_num.setText(Integer.toString(unseen));
        }
        holder.sale_chat_list_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start chat
                Intent intent = new Intent(context, ChatDetailActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.sale_chat_list_icon,"sale_chat_detail_icon");
                intent.putExtra("yourStuNumber",lists.get(position).stuNumber);
                intent.putExtra("sellId",lists.get(position).sellId);
                context.startActivity(intent,optionsCompat.toBundle());
            }
        });
        Glide.with(context)
                .load("http://182.92.87.107:8080/CarServerFile/userIcon/"+lists.get(position).stuNumber)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(holder.sale_chat_list_icon);
        Glide.with(context)
                .load("http://182.92.87.107:8080/CarServerFile/sellCar/"+lists.get(position).sellId)
                .error(R.drawable.nwulogo)
                .placeholder(R.drawable.nwulogo)
                .into(holder.sale_chat_list_car_image);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sale_chat_list_username;
        public  TextView sale_chat_list_last_message;
        public TextView sale_chat_list_unseen_num;
        public CardView sale_chat_list_card;
        public CircleImageView sale_chat_list_icon;
        // icon,image
        public ImageView sale_chat_list_car_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sale_chat_list_username = itemView.findViewById(R.id.sale_chat_list_username);
            sale_chat_list_last_message = itemView.findViewById(R.id.sale_chat_list_last_message);
            sale_chat_list_unseen_num = itemView.findViewById(R.id.sale_chat_list_unseen_num);
            sale_chat_list_card = itemView.findViewById(R.id.sale_chat_list_card);
            sale_chat_list_icon = itemView.findViewById(R.id.sale_chat_list_icon);
            sale_chat_list_car_image = itemView.findViewById(R.id.sale_chat_list_car_image);
        }
    }

}
