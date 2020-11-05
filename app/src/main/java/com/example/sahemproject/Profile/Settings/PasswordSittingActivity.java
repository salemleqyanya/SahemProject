package com.example.sahemproject.Profile.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sahemproject.Login.SignInActivity;
import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.Profile.MYList.MyDonations.Update;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PasswordSittingActivity extends AppCompatActivity {
  EditText currentPasswordET , newPasswordET , confirmPasswordET ;
  String newPassword,confirmPassword,currentPassword;
  ProgressDialog pBar;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  String user_password = "user_password";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_sitting);
    pBar = new ProgressDialog(PasswordSittingActivity.this);
    getSupportActionBar().setTitle("Settings");
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_password = sharedPreferences.getString(UsersPrefs.User_Password,"No Data Set");


    currentPasswordET = findViewById(R.id.current_password_et);
    newPasswordET = findViewById(R.id.new_password_et);
    confirmPasswordET = findViewById(R.id.confirm_password_et);




  }

  public void saveChanges(View view) {
    currentPassword = currentPasswordET.getText().toString();
    newPassword = newPasswordET.getText().toString();
    confirmPassword = confirmPasswordET.getText().toString();


    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");

    if (currentPassword.matches("")) {
      Toast.makeText(this, "You did not enter a currentPassword", Toast.LENGTH_SHORT).show();
      return;
    } else if (newPassword.matches("")) {
      Toast.makeText(this, "You did not enter a NewPassword", Toast.LENGTH_SHORT).show();
      return;
    }else if (confirmPassword.matches("")) {
      Toast.makeText(this, "You did not enter a confirmPassword", Toast.LENGTH_SHORT).show();
      return;
    } else {


      if (newPassword.matches(confirmPassword) ){
        byte[] decodePassword;
        decodePassword= Base64.decode(user_password, Base64.DEFAULT);
        user_password = new String(decodePassword, StandardCharsets.UTF_8);

        if (currentPassword.equals(user_password)){

          EditUserPasswordTask task = new EditUserPasswordTask();
          task.execute();

        }else {
          Toast.makeText(PasswordSittingActivity.this,"Your Password Incorect", Toast.LENGTH_SHORT).show();
        }

      }else {
        Toast.makeText(this, "password is not equal  confirmPassword", Toast.LENGTH_SHORT).show();

      }

    }


  }
  private class EditUserPasswordTask extends AsyncTask<Void, Void, Update>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected Update doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateUserProfile_Password.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&User_Password="+ newPassword

                ;
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

        Log.e("test res42",result);
        Gson jsonParser = new Gson();

        Update message = jsonParser.fromJson(
                result,
                Update.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(Update message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(PasswordSittingActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {


  currentPasswordET.setText("");
  newPasswordET.setText("");
  confirmPasswordET.setText("");
          editor = sharedPreferences.edit();
          editor.clear();

          editor.putString(UsersPrefs.User_ID, "user_id");
          editor.putString(UsersPrefs.User_Password, "user_password");
          editor.commit();
          Intent intent = new Intent(PasswordSittingActivity.this, SignInActivity.class);
          startActivity(intent);



        }else {
          Toast.makeText(PasswordSittingActivity.this,"Your Password Incorect", Toast.LENGTH_SHORT).show();
        }

      }else
      {
        Toast.makeText(PasswordSittingActivity.this,"Error!", Toast.LENGTH_SHORT).show();
        pBar.dismiss();
        //print error
      }


    }
  }
}
