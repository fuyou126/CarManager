package com.example.car.Info;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.R;
import com.example.car.SalePage.Sell.SaleSellCarActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class TakePictureActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_PIC = 1;
    private final int PICTURE_CODE = 4;
    Uri afterCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InitView();
        }else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void InitView() {
        if(ContextCompat.checkSelfPermission(TakePictureActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 是否允许多选
            albumIntent.setType("image/*"); // 类型为图像
            startActivityForResult(albumIntent, PICTURE_CODE);
        }else {
            ActivityCompat.requestPermissions(TakePictureActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK){
            if (requestCode == PICTURE_CODE && intent.getData() != null) {
                Uri uri = intent.getData(); // 获得已选择照片的路径对象
                startCrop(uri);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri uri = UCrop.getOutput(intent);
                // todo upload
                finish();
            }
        }
    }

    private void startCrop(Uri uri){
        File file = new File(getCacheDir(), "myCroppedImage_Info.jpg");
        if (file.exists()) {
            // avoid cache
            file.delete();
        }
        afterCrop = Uri.fromFile(file);
        UCrop.of(uri, afterCrop)
                .withAspectRatio(1, 1)
                .start(TakePictureActivity.this,UCrop.REQUEST_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_PIC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 是否允许多选
                albumIntent.setType("image/*"); // 类型为图像
                startActivityForResult(albumIntent, PICTURE_CODE);
            } else {
                // 权限被拒绝，处理相应逻辑（例如显示一个提示或禁用相关功能）
                Toast.makeText(getApplicationContext(),"未获得存储使用权限",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
            newConfig.setTo(newConfig);
        }
    }
}