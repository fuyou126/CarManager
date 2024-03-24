package com.example.car.Car;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.HTTPServer.ApiServer;
import com.example.car.HomeActivity;
import com.example.car.HomePage.NewCarPlateProvider;
import com.example.car.Info.UserInfo;
import com.example.car.R;
import com.example.car.SalePage.Sell.SaleSellCarActivity;
import com.example.car.loginActivity;
import com.github.gzuliyujiang.wheelpicker.widget.CarPlateWheelLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarAddActivity extends AppCompatActivity {
    Context context = this;

    String addType = "car";
    ImageView car_add_type;
    CardView car_add_back;
    CarPlateWheelLayout car_add_number_selector;
    EditText car_add_license;
    EditText car_add_brand;
    EditText car_add_model;
    CardView car_add_confirm;
    CardView car_add_upload;
    ImageView car_add_pic;
    private final int CAMERA_CODE = 3;
    private final int PICTURE_CODE = 4;
    private final int REQUEST_PERMISSIONS = 0;
    private final int REQUEST_PERMISSIONS_PIC = 1;
    Uri afterCrop;
    String currentPhotoPath;
    Uri picUri = null;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://182.92.87.107:8081/")
            .build();
    ApiServer apiService = retrofit.create(ApiServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);

        InitView();
    }

    private void InitView() {
        Intent thisIntent = getIntent();
        addType = thisIntent.getStringExtra("addType");

        car_add_back = findViewById(R.id.car_add_back);
        car_add_number_selector = findViewById(R.id.car_add_number_selector);
        car_add_type = findViewById(R.id.car_add_type);
        car_add_license = findViewById(R.id.car_add_license);
        car_add_brand = findViewById(R.id.car_add_brand);
        car_add_model = findViewById(R.id.car_add_model);
        car_add_confirm = findViewById(R.id.car_add_confirm);
        car_add_upload = findViewById(R.id.car_add_upload);
        car_add_pic = findViewById(R.id.car_add_pic);

        if(addType.equals("car")){
            car_add_type.setImageResource(R.drawable.car);
        } else if (addType.equals("motor")) {
            car_add_type.setImageResource(R.drawable.motor);
            TextView car_add_tips = findViewById(R.id.car_add_tips);
            car_add_tips.setVisibility(View.INVISIBLE);
            CardView car_add_upload = findViewById(R.id.car_add_upload);
            car_add_upload.setVisibility(View.INVISIBLE);
        }else {
            car_add_type.setImageResource(R.drawable.bike);
            LinearLayout car_add_linear = findViewById(R.id.car_add_linear);
            car_add_linear.setVisibility(View.INVISIBLE);
            TextView car_add_tips = findViewById(R.id.car_add_tips);
            car_add_tips.setVisibility(View.INVISIBLE);
            CardView car_add_upload = findViewById(R.id.car_add_upload);
            car_add_upload.setVisibility(View.INVISIBLE);
        }


        car_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        car_add_number_selector.setData(new NewCarPlateProvider());
        car_add_number_selector.setDefaultValue("陕","A","");
        car_add_license.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(6)});
        car_add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addType.equals("car")){
                    if(picUri == null) {
                        Toast.makeText(getApplicationContext(), "请上传行驶证图片", Toast.LENGTH_SHORT).show();
                    }else {
                        File file;
                        try {
                            file = new File(new URI(picUri.toString()));
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        String carNumber = car_add_number_selector.getFirstWheelView().getCurrentItem().toString() + car_add_number_selector.getSecondWheelView().getCurrentItem().toString() + car_add_license.getText();

                        Call<ResponseBody> call = apiService.addCar(imagePart, UserInfo.UserStuNum, carNumber, car_add_brand.getText().toString(), car_add_model.getText().toString(), UserInfo.UserName);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        String responseString = response.body().string();
                                        Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                        if(jsonMap.get("code").equals("1")){
                                            Toast.makeText(getApplicationContext(),"车辆上传成功",Toast.LENGTH_LONG).show();
                                            finish();
                                        }else if(jsonMap.get("code").equals("0")){
                                            Toast.makeText(getApplicationContext(),jsonMap.get("description"),Toast.LENGTH_LONG).show();
                                        }
                                    } catch (IOException e) {
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                // 处理失败的情况
                                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }else if (addType.equals("motor")){
                    // todo
                    String carNumber = car_add_number_selector.getFirstWheelView().getCurrentItem().toString() + car_add_number_selector.getSecondWheelView().getCurrentItem().toString() + car_add_license.getText();
                    Call<ResponseBody> call = apiService.addOther(UserInfo.UserStuNum, carNumber, car_add_brand.getText().toString(), car_add_model.getText().toString(), addType, UserInfo.UserName);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String responseString = response.body().string();
                                    Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                    if(jsonMap.get("code").equals("1")){
                                        Toast.makeText(getApplicationContext(),"车辆上传成功",Toast.LENGTH_LONG).show();
                                        finish();
                                    }else if(jsonMap.get("code").equals("0")){
                                        Toast.makeText(getApplicationContext(),jsonMap.get("上传失败"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // 处理失败的情况
                            Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Call<ResponseBody> call = apiService.addOther(UserInfo.UserStuNum, "",car_add_brand.getText().toString(), car_add_model.getText().toString(), addType, UserInfo.UserName);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String responseString = response.body().string();
                                    Map<String, String> jsonMap = JSON.parseObject(responseString, new TypeReference<HashMap<String, String>>() {});
                                    if(jsonMap.get("code").equals("1")){
                                        Toast.makeText(getApplicationContext(),"车辆上传成功",Toast.LENGTH_LONG).show();
                                        finish();
                                    }else if(jsonMap.get("code").equals("0")){
                                        Toast.makeText(getApplicationContext(),jsonMap.get("上传失败"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // 处理失败的情况
                            Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        car_add_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CarAddActivity.this);
                bottomSheetDialog.setContentView(R.layout.car_add_upload_sheet);
                bottomSheetDialog.setDismissWithAnimation(true);
                LinearLayout car_add_upload_take_now = bottomSheetDialog.findViewById(R.id.car_add_upload_take_now);
                LinearLayout car_add_upload_take_from = bottomSheetDialog.findViewById(R.id.car_add_upload_take_from);
                bottomSheetDialog.show();

                car_add_upload_take_now.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if(ContextCompat.checkSelfPermission(CarAddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(CarAddActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakePictureIntent();
                        }else {
                            ActivityCompat.requestPermissions(CarAddActivity.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS);
                        }
                    }
                });
                car_add_upload_take_from.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InlinedApi")
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if(ContextCompat.checkSelfPermission(CarAddActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 是否允许多选
                            albumIntent.setType("image/*"); // 类型为图像
                            startActivityForResult(albumIntent, PICTURE_CODE);
                        }else {
                            ActivityCompat.requestPermissions(CarAddActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS_PIC);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_CODE){
                File file = new File(currentPhotoPath);
                if(file.exists()) {
                    Uri uri = FileProvider.getUriForFile(this, "com.example.car.fileprovider", file);
                    startCrop(uri);
                }
            } else if (requestCode == PICTURE_CODE && intent.getData() != null) {
                Uri uri = intent.getData(); // 获得已选择照片的路径对象
                startCrop(uri);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                picUri = UCrop.getOutput(intent);
                Glide.with(CarAddActivity.this)
                        .load(picUri)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .into(car_add_pic);
                deletePhotoFile();
            }
        }
    }

    private File createImageFile() throws IOException {
        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 创建文件
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        // 保存文件路径以备后用
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 处理错误
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.car.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private void deletePhotoFile() {
        if(currentPhotoPath!=null) {
            File photoFile = new File(currentPhotoPath);
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }
    }

    private void startCrop(Uri uri){
        File file = new File(getCacheDir(), "myCroppedImage_car_add.jpg");
        if (file.exists()) {
            // avoid cache
            file.delete();
        }
        afterCrop = Uri.fromFile(file);
        UCrop.of(uri, afterCrop)
                .withAspectRatio(16, 9)
                .start(CarAddActivity.this,UCrop.REQUEST_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // 权限被拒绝，处理相应逻辑（例如显示一个提示或禁用相关功能）
                Toast.makeText(getApplicationContext(),"未获得相机和存储使用权限",Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_PERMISSIONS_PIC) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在不再需要照片时清理
        if(currentPhotoPath!=null) {
            deletePhotoFile();
        }
    }
}