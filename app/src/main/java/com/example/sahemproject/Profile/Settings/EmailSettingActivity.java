package com.example.sahemproject.Profile.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahemproject.Login.UserData;
import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.MainActivity;
import com.example.sahemproject.Profile.MYList.MyDonations.Update;
import com.example.sahemproject.R;
import com.example.sahemproject.ui.profile.ProfileFragment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailSettingActivity extends AppCompatActivity {
  EditText nameEmailET,passwordET ;
  TextView currentEmailTV;
  ProgressDialog pBar;
  String userEmail,userPassword;


  SharedPreferences.Editor editor;
  SharedPreferences sharedPreferences;
  String user_id = "user_id";
  String user_password = "user_password";
  String user_email = "user_email";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_email_setting);
    getSupportActionBar().setTitle("Settings");
    pBar = new ProgressDialog(EmailSettingActivity.this);
    currentEmailTV = findViewById(R.id.currentEmail_tv);
    nameEmailET = findViewById(R.id.userEmail_et);
    passwordET = findViewById(R.id.emailPassword_et);
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_email = sharedPreferences.getString(UsersPrefs.User_Email,"No Data Set");
    user_password = sharedPreferences.getString(UsersPrefs.User_Password,"No Data Set");
    currentEmailTV.setText(user_email);
  }

  public void saveChanges(View view) {

    userEmail = nameEmailET.getText().toString();
    userPassword = passwordET.getText().toString();

    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");

    if (userEmail.matches("")) {
      Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
      return;
    } else if (userPassword.matches("")) {
      Toast.makeText(this, "You did not enter a userPassword", Toast.LENGTH_SHORT).show();
      return;
    }else {

      if (userPassword.equals(user_password)){


        EditUserEmailTask task = new EditUserEmailTask();
        task.execute();

      }else {
        Toast.makeText(EmailSettingActivity.this,"Your Password Incorect", Toast.LENGTH_SHORT).show();
      }
    }

  }
  private class EditUserEmailTask extends AsyncTask<Void, Void, Update>
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

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateUserProfile_Email.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&User_Email="+userEmail
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
        Toast.makeText(EmailSettingActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          editor = sharedPreferences.edit();

          //  editor.remove(UsersPrefs.User_Name);

          editor.putString(UsersPrefs.User_Email ,userEmail );
          editor.apply();
          currentEmailTV.setText(user_email);
passwordET.setText("");
nameEmailET.setText("");

          Intent intent = new Intent(EmailSettingActivity.this, SettingActivity.class);
          startActivity(intent);

        }

      }else
      {
        Toast.makeText(EmailSettingActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }
}
