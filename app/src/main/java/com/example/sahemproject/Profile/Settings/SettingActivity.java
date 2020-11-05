package com.example.sahemproject.Profile.Settings;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.R;

public class SettingActivity extends AppCompatActivity {
  int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
  int OPEN_GALLERY_REQUEST_CODE = 2;
  ImageView userPhotoIv;
  String selectedImage;
  SharedPreferences.Editor editor;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";

  String user_photo = "user_photo";
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
 userPhotoIv = findViewById(R.id.userPhoto_iv);
    getSupportActionBar().setTitle("Settings");
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_photo = sharedPreferences.getString(UsersPrefs.USER_PHOTO,"user_photo");

    if (user_photo.equals("user_photo")){
      userPhotoIv.setImageDrawable(getDrawable(R.drawable.sss));
      Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(R.drawable.sss));
      userPhotoIv.setImageBitmap(bmp);
    }else {
      //userPhotoIv.setImageDrawable(getDrawable(R.drawable.user));
      Bitmap bmp = BitmapFactory.decodeFile(user_photo);
      userPhotoIv.setImageBitmap(bmp);

    }
  }

  public void nameSitting(View view) {
    Intent intent = new Intent(SettingActivity.this, NameSittingActivity.class);
    startActivity(intent);
  }

  public void phoneNumber(View view) {
    Intent intent = new Intent(SettingActivity.this, PhoneNumberSettingsActivity.class);
    startActivity(intent);
  }

  public void passwordSitting(View view) {
    Intent intent = new Intent(SettingActivity.this, PasswordSittingActivity.class);
    startActivity(intent);
  }

  public void emailSitting(View view) {
    Intent intent = new Intent(SettingActivity.this, EmailSettingActivity.class);
    startActivity(intent);
  }


  public void pickImageB(View view) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {

      if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED)
      {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE_REQUEST_CODE);
      }else
      {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
      }

    }else
    {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType("image/*");
      startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
    }
  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if(requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE)
    {
      if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
      {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
      }else
      {
        Toast.makeText(SettingActivity.this, "تحتاج لصلاحية لاختيار صورة", Toast.LENGTH_SHORT).show();
      }
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == OPEN_GALLERY_REQUEST_CODE)
    {
      if(resultCode == Activity.RESULT_OK)
      {
        Uri imageUri = data.getData();
        String[] filePathCol = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, filePathCol, null,null,null);
        cursor.moveToNext();
        int col_index = cursor.getColumnIndex(filePathCol[0]);
        selectedImage = cursor.getString(col_index);
        cursor.close();

        Bitmap bmp = BitmapFactory.decodeFile(selectedImage);
        editor = sharedPreferences.edit();
        editor.putString(UsersPrefs.USER_PHOTO ,selectedImage );
        editor.commit();
        userPhotoIv.setImageBitmap(bmp);
      }
    }

  }
}
