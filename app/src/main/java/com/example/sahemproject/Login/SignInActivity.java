package com.example.sahemproject.Login;

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

import com.example.sahemproject.MainActivity;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {


  EditText usernameET , passwordET ;
  TextView msgErrorTV;
  ProgressDialog pBar;

  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String userName,userPassword ;

  String user_id = "user_id";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);


    //getSupportActionBar().setTitle("Login");
  getSupportActionBar().hide();


    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME, MODE_PRIVATE);
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"user_id");
    pBar = new ProgressDialog(SignInActivity.this);

    usernameET = findViewById(R.id.username_et);
    passwordET = findViewById(R.id.password_et);
    msgErrorTV = findViewById(R.id.msgError_tv);
    System.out.println("-------------------------------222----xd"+user_id);

    if (user_id.equals("user_id") ){


    }else {
System.out.println("-----------------------------------xd"+user_id);
      Intent intent = new Intent(SignInActivity.this, MainActivity.class);
      startActivity(intent);
      finish();

    }




  }

  public void createAccount(View view) {
    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
    startActivity(intent);
  }

  public void Login(View view) {

     userName = usernameET.getText().toString();
     userPassword = passwordET.getText().toString();

    if (userName.matches("")) {
      Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
      return;
    } else if (userPassword.matches("")) {
      Toast.makeText(this, "You did not enter a userPassword", Toast.LENGTH_SHORT).show();
      return;
    }else {
      SignInTask task = new SignInTask();
      task.execute();
    }
  }

  public void resetPassword(View view) {
    Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
    startActivity(intent);
  }


  private class SignInTask extends AsyncTask<Void, Void, Login>
  {

    @Override
    protected void onPreExecute() {
    super.onPreExecute();
    pBar.setMessage("Buffering ...");
    pBar.show();
  }

    @Override
    protected Login doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_selectUserProfile.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_Name=" + userName
                        + "&User_Password=" + userPassword;
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

        Login message = jsonParser.fromJson(
                result,
                Login.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(Login message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
       // Toast.makeText(SignInActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          editor = sharedPreferences.edit();
          editor.clear();
          editor.putString(UsersPrefs.User_ID,message.getResult_msg().get(0).getUser_ID());
          editor.putString(UsersPrefs.User_Name,message.getResult_msg().get(0).getUser_Name());
          editor.putString(UsersPrefs.User_Email ,message.getResult_msg().get(0).getUser_Email() );
          editor.putString(UsersPrefs.User_PhoneNumber ,message.getResult_msg().get(0).getUser_PhoneNumber() );
          editor.putString(UsersPrefs.User_Password ,message.getResult_msg().get(0).getUser_Password() );
          editor.apply();



          msgErrorTV.setVisibility(View.INVISIBLE);

          Intent intent = new Intent(SignInActivity.this, MainActivity.class);
          pBar.dismiss();
          startActivity(intent);
          finish();


        }else
        {
          msgErrorTV.setVisibility(View.VISIBLE);
        }

      }else
      {
        msgErrorTV.setVisibility(View.VISIBLE);
      //Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
        pBar.dismiss();
        //print error
      }


    }
  }
}
