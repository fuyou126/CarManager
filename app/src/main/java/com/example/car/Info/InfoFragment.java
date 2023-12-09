package com.example.car.Info;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.car.R;
import com.example.car.loginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdodenhof.circleimageview.CircleImageView;


public class InfoFragment extends Fragment {
    private View view;
    CardView info_exit;
    CircleImageView info_icon;


    public InfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.rgb(100, 46, 208));
        InitView();
        return view;
    }

    private void InitView() {
        info_icon = view.findViewById(R.id.info_icon);
        info_exit = view.findViewById(R.id.info_exit);
        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(R.layout.info_upload_sheet);
                bottomSheetDialog.setDismissWithAnimation(true);
                LinearLayout info_upload_take_now = bottomSheetDialog.findViewById(R.id.info_upload_take_now);
                LinearLayout info_upload_take_from = bottomSheetDialog.findViewById(R.id.info_upload_take_from);
                bottomSheetDialog.show();

                info_upload_take_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(requireActivity(), TakePhotoActivity.class);
                        startActivity(intent);
                    }
                });
                info_upload_take_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(requireActivity(), TakePictureActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        info_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo.setUserName("");
                Intent intent = new Intent(requireActivity(), loginActivity.class);
                startActivity(intent);
            }
        });
    }

}
