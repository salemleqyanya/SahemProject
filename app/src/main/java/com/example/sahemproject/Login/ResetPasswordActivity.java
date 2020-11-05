package com.example.sahemproject.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.nio.charset.StandardCharsets;

public class ResetPasswordActivity extends AppCompatActivity {
EditText re_userNameET,re_userEmailET,re_userPhoneET;
  EditText newPasswordET, confirmPasswordET;
  ProgressDialog pBar;
  Button saveChangesB, cancelB;
  TextView msgErrorTV;
  AlertDialog dialog;
  String newPassword, confirmPassword;
  String userName,userPassword,userEmail,userPhoneNumber, user_id;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);
    getSupportActionBar().hide();
    pBar = new ProgressDialog(ResetPasswordActivity.this);
    re_userNameET = findViewById(R.id.re_userName_et);
    re_userEmailET = findViewById(R.id.re_userEmail_et);
    re_userPhoneET = findViewById(R.id.re_userPhone_et);
    msgErrorTV = findViewById(R.id.msgError_tv);
  }

  public void reset(View view) {
    ResetPassword task = new ResetPassword();
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

  }
  private class ResetPassword extends AsyncTask<Void, Void, ResetPasswordData>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
      userName = re_userNameET.getText().toString();
      userEmail = re_userEmailET.getText().toString();
      userPhoneNumber = re_userPhoneET.getText().toString();
    }

    @Override
    protected ResetPasswordData doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_Reset_Password.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_Name=" + userName
                        + "&User_Email=" + userEmail
                        + "&User_PhoneNumber=" + userPhoneNumber
                        ;

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

        ResetPasswordData message = jsonParser.fromJson(
                result,
                ResetPasswordData.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(ResetPasswordData message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(ResetPasswordActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {
//          Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
//          startActivity(intent);
//          pBar.dismiss();
//          finish();

//staet
              user_id = message.result_msg.get(0).User_ID;
System.out.println("jvbjubsdiohb+++++++++++++++++++viuashviuh"+user_id);
          final View view = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.reset_password, null);


          newPasswordET = view.findViewById(R.id.new_password_et);
          confirmPasswordET = view.findViewById(R.id.confirm_password_et);
          saveChangesB = view.findViewById(R.id.saveChangesB);
          cancelB = view.findViewById(R.id.cancelB);

          cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
          });

          saveChangesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              newPassword = newPasswordET.getText().toString();
              confirmPassword = confirmPasswordET.getText().toString();



            if (newPassword.matches("")) {
                Toast.makeText(ResetPasswordActivity.this, "You did not enter a NewPassword", Toast.LENGTH_SHORT).show();
                return;
              } else if (confirmPassword.matches("")) {
                Toast.makeText(ResetPasswordActivity.this, "You did not enter a confirmPassword", Toast.LENGTH_SHORT).show();
                return;
              } else {


                if (newPassword.matches(confirmPassword)) {

              EditUserPasswordTask task = new EditUserPasswordTask();
                  task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                } else {
                  Toast.makeText(ResetPasswordActivity.this, "password is not equal  confirmPassword", Toast.LENGTH_SHORT).show();

                }

              }


              //
            }
          });
          AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);

          builder.setTitle("      Reset Password");


          builder.setView(view);

          dialog = builder.create();
          dialog.show();






          //end

          msgErrorTV.setVisibility(View.INVISIBLE);



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
  private class EditUserPasswordTask extends AsyncTask<Void, Void, Update> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected Update doInBackground(Void... voids) {

      try {
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
                "User_ID=" + user_id
                        + "&User_Password=" + newPassword;
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

        Log.e("test res42", result);
        Gson jsonParser = new Gson();

        Update message = jsonParser.fromJson(
                result,
                Update.class
        );


        return message;


      } catch (Exception e) {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(Update message) {
      super.onPostExecute(message);


      if (message != null) {
        pBar.dismiss();
        Toast.makeText(ResetPasswordActivity.this, message.getResult_status(), Toast.LENGTH_SHORT).show();

        if (message.getResult_code() == 1) {

         Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
          startActivity(intent);
          pBar.dismiss();
          finish();

          newPasswordET.setText("");
          confirmPasswordET.setText("");


          dialog.dismiss();

        } else {
          Toast.makeText(ResetPasswordActivity.this, "Your Password Incorect", Toast.LENGTH_SHORT).show();
        }

      } else {
        Toast.makeText(ResetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        pBar.dismiss();
        //print error
      }


    }
  }
}