package com.example.sahemproject.Profile.MYList.MyDonations;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyDonationDetailsActivity extends AppCompatActivity {
  TextView donationDonorNameTV  , donationSoursNameTV
          , donationDescriptionTV,donationPublishingTimeTV ;
  EditText donationSourceNameET  , donationDescriptionET , donationUrlET;


  String Don_PDF,Don_ID,Don_Name ,Don_Description,User_Name,Don_PublishingTime,
          sourceName, donationDescription,donationUrl;
  AlertDialog dialog;
  ProgressDialog pBar;
  Button donationSoursRequestB, saveChangesB,cancelIB;

  MenuItem download;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_donation_details);

    pBar = new ProgressDialog(MyDonationDetailsActivity.this);
    Intent intent = getIntent();
     Don_ID = intent.getStringExtra("Don_ID");
     Don_Name = intent.getStringExtra("Don_Name");
     Don_Description = intent.getStringExtra("Don_Description");
     User_Name = intent.getStringExtra("User_Name");
     Don_PDF = intent.getStringExtra("Don_PDF");
     Don_PublishingTime = intent.getStringExtra("Don_PublishingTime");

    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");


    donationDonorNameTV = findViewById(R.id.donation_DonorName_tv);



    donationDescriptionTV = findViewById(R.id.donation_Description_tv);
//    donationSoursRequestB = findViewById(R.id.donation_soursRequest_b);


   // donationSoursNameTV.setText(Don_Name);
    getSupportActionBar().setTitle(Don_Name);
    donationDescriptionTV.setText(Don_Description);

    donationDonorNameTV.setText(User_Name);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.edit, menu);
    inflater.inflate(R.menu.download, menu);
    download =  menu.findItem(R.id.Download_it);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.Download_it) {
      if (!Don_PDF.startsWith("http://") && !Don_PDF.startsWith("https://"))
        Don_PDF = "http://" + Don_PDF;
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Don_PDF));
      startActivity(browserIntent);
    }else if (id == R.id.edit_it) {
      View v = LayoutInflater.from(MyDonationDetailsActivity.this).inflate(R.layout.edit_donation_dialog, null);

      donationSourceNameET = v.findViewById(R.id.donation_sourceName_et);
      donationDescriptionET = v.findViewById(R.id.donation_description_et);
      donationUrlET = v.findViewById(R.id.donation_url_et);
      saveChangesB = v.findViewById(R.id.saveChangesIB);
      cancelIB = v.findViewById(R.id.cancelIB);

      cancelIB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          dialog.dismiss();
        }
      });

      donationSourceNameET.setText(Don_Name);
      donationDescriptionET.setText(Don_Description);
      donationUrlET.setText(Don_PDF);

      saveChangesB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
          saveBuilder.setTitle("Save Changes");
          saveBuilder.setMessage("Are You Sure You Want to Save Changes ?");
          saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


              sourceName = donationSourceNameET.getText().toString();
              donationDescription = donationDescriptionET.getText().toString();
              donationUrl = donationUrlET.getText().toString();

              EditDonationsTask task = new EditDonationsTask();
              task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



              dialog.dismiss();

            }
          });
          saveBuilder.setNegativeButton("No", null);
          saveBuilder.create().show();
        }

      });


      AlertDialog.Builder builder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
      builder.setTitle("Edit MY Donations");
      builder.setView(v);
      dialog = builder.create();
      dialog.show();
    }else if(id == R.id.delete_it) {
      AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
      saveBuilder.setTitle("Save Changes");
      saveBuilder.setMessage("Are You Sure You Want to Remove Your Donation ?");
      saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          RemoveDonationsTask task = new RemoveDonationsTask();
          task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



          dialog.dismiss();

        }
      });
      saveBuilder.setNegativeButton("No", null);
      saveBuilder.create().show();

    }
    return super.onOptionsItemSelected(item);
  }


  public void editDonation(View view) {
    View v = LayoutInflater.from(MyDonationDetailsActivity.this).inflate(R.layout.edit_donation_dialog, null);

    donationSourceNameET = v.findViewById(R.id.donation_sourceName_et);
    donationDescriptionET = v.findViewById(R.id.donation_description_et);
    donationUrlET = v.findViewById(R.id.donation_url_et);
    saveChangesB = v.findViewById(R.id.saveChangesIB);
    cancelIB = v.findViewById(R.id.cancelIB);

    cancelIB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        dialog.dismiss();
      }
    });

    donationSourceNameET.setText(Don_Name);
    donationDescriptionET.setText(Don_Description);
    donationUrlET.setText(Don_PDF);

    saveChangesB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
        saveBuilder.setTitle("Save Changes");
        saveBuilder.setMessage("Are You Sure You Want to Save Changes ?");
        saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {


             sourceName = donationSourceNameET.getText().toString();
             donationDescription = donationDescriptionET.getText().toString();
            donationUrl = donationUrlET.getText().toString();

            EditDonationsTask task = new EditDonationsTask();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



            dialog.dismiss();

          }
        });
        saveBuilder.setNegativeButton("No", null);
        saveBuilder.create().show();
      }

    });


    AlertDialog.Builder builder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
    builder.setTitle("Edit");
    builder.setView(v);
    dialog = builder.create();
    dialog.show();


      }

  public void removeDonation(View view) {
    AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDonationDetailsActivity.this);
    saveBuilder.setTitle("Save Changes");
    saveBuilder.setMessage("Are You Sure You Want to Remove Your Donation ?");
    saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        RemoveDonationsTask task = new RemoveDonationsTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        dialog.dismiss();

      }
    });
    saveBuilder.setNegativeButton("No", null);
    saveBuilder.create().show();
  }


  private class EditDonationsTask extends AsyncTask<Void, Void, Update>
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

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateDonation.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
               + "&Don_ID="+Don_ID
               +"&Don_Name="+sourceName
                        +"&Don_Description="+donationDescription
                        +"&Don_PDF="+donationUrl
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
        Toast.makeText(MyDonationDetailsActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          donationSourceNameET.setText("");
          donationDescriptionET.setText("");
          donationUrlET.setText("");

          Intent intent = new Intent(MyDonationDetailsActivity.this, MyDonationsListActivity.class);
          startActivity(intent);
          finish();

        }

      }else
      {
        Toast.makeText(MyDonationDetailsActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }
  private class RemoveDonationsTask extends AsyncTask<Void, Void, Update>
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

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_deleteDonation.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&Don_ID="+Don_ID
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
        Toast.makeText(MyDonationDetailsActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {



          Intent intent = new Intent(MyDonationDetailsActivity.this, MyDonationsListActivity.class);

          startActivity(intent);
          finish();

        }

      }else
      {
        Toast.makeText(MyDonationDetailsActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }
    }

