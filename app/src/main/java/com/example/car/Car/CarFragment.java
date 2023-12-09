package com.example.car.Car;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.car.R;
import com.example.car.SalePage.Like.SaleLikeActivity;
import com.example.car.SalePage.Sell.SaleSellCarActivity;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CarFragment extends Fragment {
    private View view;
    RecyclerView car_rv;
    List<CarCard> list = new ArrayList<>();
    ImageButton car_button_add;

    public CarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#f5f5f5"));
        InitView();
        return view;
    }

    private void InitView() {
        car_button_add = view.findViewById(R.id.car_button_add);
        car_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionPicker picker = new OptionPicker(requireActivity());
                picker.setData(Arrays.asList("汽车","摩托车","自行车"));
                picker.setTitle("选择要添加的车辆类型");
                picker.setBodyWidth(140);
                picker.setOnOptionPickedListener(new OnOptionPickedListener() {
                    @Override
                    public void onOptionPicked(int position, Object item) {
                        String addType = "car";
                        Intent intent = new Intent(requireActivity(), CarAddActivity.class);
                        switch (position){
                            case 0:
                                addType = "car";
                                break;
                            case 1:
                                addType = "motor";
                                break;
                            case 2:
                                addType = "bike";
                                break;
                        }
                        intent.putExtra("addType",addType);
                        startActivity(intent);
                    }
                });
                picker.show();
            }
        });

        car_rv = view.findViewById(R.id.car_rv);
        list.add(new CarCard("Car","奥迪 A8","陕A8S8ZS",true));
        list.add(new CarCard("Car","奥迪 A6","陕UCZCZO0",false));
        list.add(new CarCard("Motor","奥迪 A8","陕A8S8ZS",false));
        list.add(new CarCard("Bike","奥迪 A8","陕A8S8ZS",false));
        CarCardAdapter adapter = new CarCardAdapter(list,requireActivity());
        car_rv.setAdapter(adapter);
        car_rv.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }
}