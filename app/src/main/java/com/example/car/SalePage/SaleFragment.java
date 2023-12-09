package com.example.car.SalePage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car.R;
import com.example.car.SalePage.Chat.ChatListActivity;
import com.example.car.SalePage.Like.SaleLikeActivity;
import com.example.car.SalePage.Sell.SaleSellCarActivity;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaleFragment extends Fragment {
    private View view;
    private SmartRefreshLayout smartRefreshLayout;
    RecyclerView saleCard_rv;

    CardView search_card;
    CardView sale_button_message;
    EditText sale_search_content;
    ImageView sale_button_message_haveNews;

    CardView sale_button_like;
    CardView sale_button_sell;

    public SaleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sale, container, false);

        requireActivity().getWindow().setStatusBarColor(Color.rgb(76,57,204));
        InitView();
        return view;
    }

    private void InitView(){
        search_card = view.findViewById(R.id.search_card);

        sale_button_like = view.findViewById(R.id.sale_button_like);
        sale_button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SaleLikeActivity.class);
                startActivity(intent);
            }
        });

        sale_button_message_haveNews = view.findViewById(R.id.sale_button_message_haveNews);
        sale_button_message_haveNews.setVisibility(View.INVISIBLE);

        sale_button_message = view.findViewById(R.id.sale_button_message);
        sale_button_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ChatListActivity.class);
                startActivity(intent);
            }
        });

        sale_search_content = view.findViewById(R.id.sale_search_content);
        sale_search_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 输入法中点击搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    Intent intent = new Intent(requireActivity(), SaleSearchActivity.class);
                    intent.putExtra("searchFor",sale_search_content.getText().toString());
                    sale_search_content.setText("");
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) requireActivity(),search_card,"search_card");
                    startActivity(intent,optionsCompat.toBundle());
                    return true;
                }
                return false;
            }
        });
//        sale_search_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireActivity(), SaleSearchActivity.class);
//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) requireActivity(),search_card,"search_card");
//                startActivity(intent,optionsCompat.toBundle());
//            }
//        });

        saleCard_rv = view.findViewById(R.id.sale_rv);
        List<SaleCard> s = new ArrayList<>();
        s.add(new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
        s.add(new SaleCard("特斯拉","这是一辆好车，能跑很远啊实打实大大大飒飒的","999","22"));
        s.add(new SaleCard("奥迪A8","这","1000000","6"));
        s.add(new SaleCard("奥迪A8","这","1000000","6"));
        // 获取适配器实例
        SaleCardAdapter adapter = new SaleCardAdapter(s,requireActivity());
        //配置适配器
        saleCard_rv.setAdapter(adapter);
        //配置布局管理器
        saleCard_rv.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //设置刷新
        //设置头部刷新的样式
        smartRefreshLayout = view.findViewById(R.id.sale_card_fresh);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(requireActivity()));
        //设置页脚刷新的样式
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(requireActivity()));
        //设置头部刷新时间监听
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                //添加一条新数据，再最开头的位置
                s.add(0,new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
                adapter.notifyDataSetChanged();
                sale_button_message_haveNews.setVisibility(View.VISIBLE);
                Toast.makeText(requireActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                smartRefreshLayout.finishLoadMore(1000);
                //添加一条新数据，再最后的位置
                s.add(new SaleCard("奥迪A8","这是一辆好车，能跑很远","399999","24"));
                adapter.notifyDataSetChanged();
                sale_button_message_haveNews.setVisibility(View.INVISIBLE);
                Toast.makeText(requireActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

        sale_button_sell = view.findViewById(R.id.sale_button_sell);
        sale_button_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionPicker picker = new OptionPicker(requireActivity());
                picker.setData(Arrays.asList("汽车","摩托车","自行车"));
                picker.setTitle("选择要出售的车辆类型");
                picker.setBodyWidth(140);
                picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                    @Override
                    public void onOptionPicked(int position, Object item) {
                        String sellType = "car";
                        Intent intent = new Intent(requireActivity(), SaleSellCarActivity.class);
                        switch (position){
                            case 0:
                                sellType = "car";
                                break;
                            case 1:
                                sellType = "motor";
                                break;
                            case 2:
                                sellType = "bike";
                                break;
                        }
//                        Toast.makeText(requireActivity(), sellType, Toast.LENGTH_SHORT).show();
                        intent.putExtra("sellType",sellType);
                        startActivity(intent);
                    }
                });
                picker.show();
            }
        });


    }
}