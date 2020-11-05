package com.example.sahemproject.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahemproject.Donations.AddDonationActivity;
import com.example.sahemproject.Donations.InsertDonations;
import com.example.sahemproject.MainActivity;
import com.example.sahemproject.Profile.Settings.SettingActivity;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {
ImageView userImageIV;
  EditText usernameET , phoneNumberET , passwordET , confirmPasswordET,userEmailET ;
  String userName,userPassword,userEmail,userPhoneNumber,confirmPassword;
  ProgressDialog pBar;
  TextView msgErrorTV;
  String selectedImage;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
  int OPEN_GALLERY_REQUEST_CODE = 2;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    getSupportActionBar().hide();
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME, MODE_PRIVATE);
    pBar = new ProgressDialog(SignUpActivity.this);
    usernameET = findViewById(R.id.signUp_username_et);
    phoneNumberET = findViewById(R.id.phone_number_et);
    userEmailET = findViewById(R.id.userEmail_et);
    userImageIV = findViewById(R.id.userImage_iv);
    passwordET = findViewById(R.id.signUp_password_et);
    msgErrorTV = findViewById(R.id.msgError_tv);
    confirmPasswordET = findViewById(R.id.signUp_confirm_password_et);
   // getSupportActionBar().setTitle("registration");



  }

  public void registration(View view) {






    userName = usernameET.getText().toString();
    userPassword = passwordET.getText().toString();
    userEmail = userEmailET.getText().toString();
    userPhoneNumber = phoneNumberET.getText().toString();
    confirmPassword = confirmPasswordET.getText().toString();

    if (userName.matches("")) {
      Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
      return;
    } else if (userEmail.matches("")) {
      Toast.makeText(this, "You did not enter a userEmail", Toast.LENGTH_SHORT).show();
      return;
    } else if (userPhoneNumber.matches("")) {
      Toast.makeText(this, "You did not enter a userPhoneNumber", Toast.LENGTH_SHORT).show();
      return;
    } else if (userPassword.matches("")) {
      Toast.makeText(this, "You did not enter a userPassword", Toast.LENGTH_SHORT).show();
      return;
    } else if (confirmPassword.matches("")) {
      Toast.makeText(this, "You did not enter a confirmPassword", Toast.LENGTH_SHORT).show();
      return;
    }

     if (userPassword.matches(confirmPassword) ){
       registrationTask task = new registrationTask();
       task.execute();
    }else {
       Toast.makeText(this, "password is not equal  confirmPassword", Toast.LENGTH_SHORT).show();

    }
  }



  public void goToSignIn(View view) {
    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
    startActivity(intent);
  }

  private class registrationTask extends AsyncTask<Void, Void, SignUp>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
      msgErrorTV.setVisibility(View.INVISIBLE);
    }

    @Override
    protected SignUp doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;
//http://test2021.aba.vg
        //http://test2021.aba.vg/Sahem/sahem_insertUserProfile.php
        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_insertUserProfile.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        String postParameters =
                "User_Name="+userName
                        +"&User_Email="+userEmail
                        +"&User_PhoneNumber="+userPhoneNumber
                        +"&User_Password="+ userPassword;

        urlConnection.setFixedLengthStreamingMode(
                postParameters.getBytes().length);
        PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
        out.print(postParameters);
        out.close();

        urlConnection.connect();


        InputStream stream = urlConnection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String str;
        while (true) {
          str = bufferedReader.readLine();
          if (str == null)
            break;
          result += str;
        }

        Log.e("test res2",result);
        Gson jsonParser = new Gson();

        SignUp message = jsonParser.fromJson(
                result,
                SignUp.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(SignUp message) {
      super.onPostExecute(message);




      if(message != null)
      {

        Toast.makeText(SignUpActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {
          msgErrorTV.setVisibility(View.INVISIBLE);
//          editor = sharedPreferences.edit();
//          editor.clear();
//          editor.putString(UsersPrefs.User_ID,message.getResult_msg().get(0).getUser_ID());
//          editor.putString(UsersPrefs.User_Name,message.getResult_msg().get(0).getUser_Name());
//          editor.putString(UsersPrefs.User_Email ,message.getResult_msg().get(0).getUser_Email() );
//          editor.putString(UsersPrefs.User_PhoneNumber ,message.getResult_msg().get(0).getUser_PhoneNumber() );
//          editor.putString(UsersPrefs.User_Password ,message.getResult_msg().get(0).getUser_Password() );
//          editor.putString(UsersPrefs.USER_PHOTO ,selectedImage);
         // editor.apply();
          Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
          startActivity(intent);
          finish();
          pBar.dismiss();

        }else {
          msgErrorTV.setVisibility(View.VISIBLE);
          Toast.makeText(SignUpActivity.this, message.getResult_status(), Toast.LENGTH_SHORT).show();
          pBar.dismiss();
        }
      }else
      {
        msgErrorTV.setVisibility(View.VISIBLE);
        Toast.makeText(SignUpActivity.this,"Error!", Toast.LENGTH_SHORT).show();
        pBar.dismiss();
        //print error
      }


    }
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
        Toast.makeText(SignUpActivity.this, "تحتاج لصلاحية لاختيار صورة", Toast.LENGTH_SHORT).show();
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
//        editor = sharedPreferences.edit();
//        editor.putString(UsersPrefs.USER_PHOTO ,selectedImage );
//        editor.commit();
        userImageIV.setImageBitmap(bmp);
      }
    }

  }
}
