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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TakePictureActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_PIC = 1;
    private final int PICTURE_CODE = 4;
    Uri afterCrop;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

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
                File file;
                try {
                    file = new File(new URI(uri.toString()));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                Call<ResponseBody> call = apiService.uploadIcon(imagePart, UserInfo.UserStuNum);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseString = response.body().string();
                                Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                if(jsonMap.get("code").equals("1")){
                                    Toast.makeText(getApplicationContext(),"头像上传成功",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (IOException e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 处理失败的情况
                        Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


//                finish();
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
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(50);
        UCrop.of(uri, afterCrop)
                .withAspectRatio(1, 1)
                .withOptions(options)
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