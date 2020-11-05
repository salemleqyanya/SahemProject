package com.example.sahemproject.Donations;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahemproject.Login.SignInActivity;
import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.MainActivity;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddDonationActivity extends AppCompatActivity {
  EditText donationSourceNameET  , donationDescriptionET , donationUrlET;
  Donations donationToBeAdded;
  ProgressDialog pBar;
  SharedPreferences sharedPreferences;
  String user_id = "user_id";
  String SourceName,Description, donationUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_donation);
    getSupportActionBar().setTitle("Add Donations");
    donationSourceNameET = findViewById(R.id.donation_sourceName_et);
    donationDescriptionET = findViewById(R.id.donation_description_et);
    donationUrlET = findViewById(R.id.donation_url_et);

    pBar = new ProgressDialog(AddDonationActivity.this);

    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);


    donationSourceNameET.setText("");
    donationDescriptionET.setText("");
    donationUrlET.setText("");

  }



  public void donation_share(View view) {

    SourceName = donationSourceNameET.getText().toString();
    Description = donationDescriptionET.getText().toString();
    donationUrl = donationUrlET.getText().toString();


    if (SourceName.matches("")) {
      Toast.makeText(this, "You did not enter a SourceName", Toast.LENGTH_SHORT).show();
      return;
    } else if (Description.matches("")) {
      Toast.makeText(this, "You did not enter a Description", Toast.LENGTH_SHORT).show();
      return;
    }else if (donationUrl.matches("")) {
      Toast.makeText(this, "You did not enter a donationUrl", Toast.LENGTH_SHORT).show();
      return;
    }else {


      AlertDialog.Builder addBuilder = new AlertDialog.Builder(AddDonationActivity.this);
      addBuilder.setTitle("Confirm Add");
      addBuilder.setMessage("Are You Sure You Want to Add Donations ?");
      addBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

          donationToBeAdded = new Donations();

          donationToBeAdded.setDon_Name(donationSourceNameET.getText().toString());
          donationToBeAdded.setDon_Description(donationDescriptionET.getText().toString());
          donationToBeAdded.setDon_PDF(donationUrlET.getText().toString());
          user_id = sharedPreferences.getString(UsersPrefs.User_ID, "No Data Set");

          AddDonationsTask task = new AddDonationsTask();
          task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        }


      });
      addBuilder.setNegativeButton("No", null);
      addBuilder.create().show();
    }
  }

  private class AddDonationsTask extends AsyncTask<Void, Void, InsertDonations>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected InsertDonations doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_insertDonation.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        String postParameters =
                "Don_Name="+donationToBeAdded.getDon_Name()
                        +"&Don_Description="+donationToBeAdded.getDon_Description()
                        +"&Don_PDF="+donationToBeAdded.getDon_PDF()
                        +"&User_ID="+ user_id;

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

        InsertDonations message = jsonParser.fromJson(
                result,
                InsertDonations.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(InsertDonations message) {
      super.onPostExecute(message);




      if(message != null)
      {
       pBar.dismiss();
        Toast.makeText(AddDonationActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          Intent intent = new Intent(AddDonationActivity.this, MainActivity.class);
          startActivity(intent);

          finish();
        }

      }else
      {
        Toast.makeText(AddDonationActivity.this,"Error!", Toast.LENGTH_SHORT).show();

         //print error
      }


    }
  }
}
