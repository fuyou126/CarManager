package com.example.car.SalePage.Sell;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.car.R;
import com.example.car.SalePage.SaleCarActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SaleSellCarActivity extends AppCompatActivity {

    CardView sale_sell_car_back;
    EditText sale_sell_car_price;
    EditText sale_sell_car_brand;
    ImageView sale_sell_car_type;
    EditText sale_sell_car_model;
    EditText sale_sell_car_description;
    // pic
    ImageView sale_sell_car_pic;
    CardView sale_sell_car_upload;
    CardView sale_sell_car_ok;

    private final int CAMERA_CODE = 3;
    private final int PICTURE_CODE = 4;
//    private final int UCROP_CODE = 5;
    private final int REQUEST_PERMISSIONS = 0;
    private final int REQUEST_PERMISSIONS_PIC = 1;
    Uri afterCrop;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_sell_car);

        InitView();
    }

    private void InitView() {
        sale_sell_car_back = findViewById(R.id.sale_sell_car_back);
        sale_sell_car_price = findViewById(R.id.sale_sell_car_price);
        sale_sell_car_brand = findViewById(R.id.sale_sell_car_brand);
        sale_sell_car_type = findViewById(R.id.sale_sell_car_type);
        sale_sell_car_model = findViewById(R.id.sale_sell_car_model);
        sale_sell_car_description = findViewById(R.id.sale_sell_car_description);
        sale_sell_car_upload = findViewById(R.id.sale_sell_car_upload);
        sale_sell_car_ok = findViewById(R.id.sale_sell_car_ok);
        sale_sell_car_pic = findViewById(R.id.sale_sell_car_pic);

        Intent thisIntent = getIntent();
        String sellType = thisIntent.getStringExtra("sellType");
        if(sellType.equals("car")){
            sale_sell_car_type.setImageResource(R.drawable.car);
        } else if (sellType.equals("motor")) {
            sale_sell_car_type.setImageResource(R.drawable.motor);
        }else {
            sale_sell_car_type.setImageResource(R.drawable.bike);
        }

        sale_sell_car_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sale_sell_car_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = sale_sell_car_price.getText().toString();
                String brand = sale_sell_car_brand.getText().toString();
                String model = sale_sell_car_model.getText().toString();
                String description = sale_sell_car_description.getText().toString();

                SweetAlertDialog pDialog = new SweetAlertDialog(SaleSellCarActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog
                        .setTitleText("确认发布该出售信息吗?")
                        .setContentText(brand+"("+model+")")
                        .setContentTextSize(14)
                        .setCancelText("✖")
                        .setConfirmText("✔")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#FF0000"))
                        .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                                if(price.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"价格信息未填写",Toast.LENGTH_LONG).show();
                                }else if(brand.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"车辆品牌未填写",Toast.LENGTH_LONG).show();
                                }else if(model.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"车辆型号未填写",Toast.LENGTH_LONG).show();
                                }else{
                                    // 发布
                                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        })
                        .show();
            }
        });

        sale_sell_car_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SaleSellCarActivity.this);
                bottomSheetDialog.setContentView(R.layout.sale_sell_car_upload_sheet);
                bottomSheetDialog.setDismissWithAnimation(true);
                LinearLayout sale_sell_car_upload_take_now = bottomSheetDialog.findViewById(R.id.sale_sell_car_upload_take_now);
                LinearLayout sale_sell_car_upload_take_from = bottomSheetDialog.findViewById(R.id.sale_sell_car_upload_take_from);
                bottomSheetDialog.show();
                sale_sell_car_upload_take_now.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if(ContextCompat.checkSelfPermission(SaleSellCarActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(SaleSellCarActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakePictureIntent();
                        }else {
                            ActivityCompat.requestPermissions(SaleSellCarActivity.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS);
                        }
                    }
                });
                sale_sell_car_upload_take_from.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InlinedApi")
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if(ContextCompat.checkSelfPermission(SaleSellCarActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 是否允许多选
                            albumIntent.setType("image/*"); // 类型为图像
                            startActivityForResult(albumIntent, PICTURE_CODE);
                        }else {
                            ActivityCompat.requestPermissions(SaleSellCarActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS_PIC);
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
//                Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
//                sale_sell_car_pic.setImageBitmap(imageBitmap);
//                deletePhotoFile();
            } else if (requestCode == PICTURE_CODE && intent.getData() != null) {
                Uri uri = intent.getData(); // 获得已选择照片的路径对象
                startCrop(uri);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri uri = UCrop.getOutput(intent);
                Glide.with(SaleSellCarActivity.this)
                        .load(uri)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .into(sale_sell_car_pic);
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
        File file = new File(getCacheDir(), "myCroppedImage.jpg");
        if (file.exists()) {
            // avoid cache
            file.delete();
        }
        afterCrop = Uri.fromFile(file);
        UCrop.of(uri, afterCrop)
                .withAspectRatio(16, 9)
                .start(SaleSellCarActivity.this,UCrop.REQUEST_CROP);
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

//    第二步同时重写onConfigurationChanged(Configuration newConfig)方法
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