package com.example.sahemproject.Profile.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PhoneNumberSettingsActivity extends AppCompatActivity {
  ProgressDialog pBar;
  EditText userPhoneNumberET,phonePasswordET  ;
  TextView currentPhoneTV;
  String userPhoneNumber,password;
  SharedPreferences.Editor editor;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  String user_password = "user_password";
  String user_phone = "user_phone";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_phone_number_settings);
    getSupportActionBar().setTitle("Settings");
    pBar = new ProgressDialog(PhoneNumberSettingsActivity.this);
    userPhoneNumberET = findViewById(R.id.userPhoneNumber_et);
    phonePasswordET = findViewById(R.id.phonePassword_et);
    currentPhoneTV = findViewById(R.id.currentPhone_tv);
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_phone = sharedPreferences.getString(UsersPrefs.User_PhoneNumber,"No Data Set");
    user_password = sharedPreferences.getString(UsersPrefs.User_Password,"No Data Set");
    currentPhoneTV.setText(user_phone);
  }

  public void saveChanges(View view) {
    userPhoneNumber = userPhoneNumberET.getText().toString();
    password = phonePasswordET.getText().toString();
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");

    if (userPhoneNumber.matches("")) {
      Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
      return;
    } else if (password.matches("")) {
      Toast.makeText(this, "You did not enter a userPassword", Toast.LENGTH_SHORT).show();
      return;
    }else {

      if (password.equals(user_password)){


        EditUserPhoneNumberTask task = new EditUserPhoneNumberTask();
        task.execute();

      }else {
        Toast.makeText(PhoneNumberSettingsActivity.this,"Your Password Incorect", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private class EditUserPhoneNumberTask extends AsyncTask<Void, Void, Update>
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

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateUserProfile_PhoneNumber.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&User_PhoneNumber="+userPhoneNumber
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
        Toast.makeText(PhoneNumberSettingsActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          editor = sharedPreferences.edit();

          //  editor.remove(UsersPrefs.User_Name);
          editor.putString(UsersPrefs.User_PhoneNumber ,userPhoneNumber );
          editor.apply();

          userPhoneNumberET.setText("");
          phonePasswordET.setText("");
          Intent intent = new Intent(PhoneNumberSettingsActivity.this, SettingActivity.class);
          startActivity(intent);



        }

      }else
      {
        Toast.makeText(PhoneNumberSettingsActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }
}