package com.example.sahemproject.Profile.MYList.MyRequests;

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
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonationDetailsActivity;
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonationsListActivity;
import com.example.sahemproject.Profile.MYList.MyDonations.Update;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyDetailsRequestActivity extends AppCompatActivity {
  TextView detailsDonorNameTV  , detailsSoursNameTV  , detailsDescriptionTV,requestPublishingTimeTV ;
  EditText requestNameEt , requestDescriptionET  ;
  AlertDialog dialog;
  ProgressDialog pBar;
  String Req_ID,Req_Name,Req_Description,Req_PublishingTime,User_Name ,
          sourceName,requestDescription;
  Button detailsSoursRequestB,saveChangesB,cancelIB ;

  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_details_request);

    pBar = new ProgressDialog(MyDetailsRequestActivity.this);


    detailsDonorNameTV = findViewById(R.id.details_DonorName_tv);
    detailsDescriptionTV = findViewById(R.id.details_Description_tv);


    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");

    Intent intent = getIntent();
     Req_ID = intent.getStringExtra("Req_ID");
     Req_Name = intent.getStringExtra("Req_Name");
     Req_Description = intent.getStringExtra("Req_Description");
     Req_PublishingTime = intent.getStringExtra("Req_PublishingTime");
     User_Name = intent.getStringExtra("User_Name");
    getSupportActionBar().setTitle(Req_Name);

    detailsDescriptionTV.setText(Req_Description);
    detailsDonorNameTV.setText(User_Name);




  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.edit, menu);


    return true;
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    int id = item.getItemId();



    if(id == R.id.edit_it)
    {
      View v = LayoutInflater.from(MyDetailsRequestActivity.this).inflate(R.layout.edit_request_dialog, null);


      requestNameEt = v.findViewById(R.id.request_name_et);
      requestDescriptionET = v.findViewById(R.id.request_description_et);

      saveChangesB = v.findViewById(R.id.saveChangesIB);
      cancelIB = v.findViewById(R.id.cancelIB);

      cancelIB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          dialog.dismiss();
        }
      });

      requestNameEt.setText(Req_Name);
      requestDescriptionET.setText(Req_Description);


      saveChangesB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
          saveBuilder.setTitle("Save Changes");
          saveBuilder.setMessage("Are You Sure You Want to Save Changes ?");
          saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


              sourceName = requestNameEt.getText().toString();
              requestDescription = requestDescriptionET.getText().toString();


              EditRequestTask task = new EditRequestTask();
              task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


              dialog.dismiss();

            }
          });
          saveBuilder.setNegativeButton("No", null);
          saveBuilder.create().show();
        }

      });


      AlertDialog.Builder builder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
      builder.setTitle("Edit");
      builder.setView(v);
      dialog = builder.create();
      dialog.show();
    }
    else if(id == R.id.delete_it)
    {
      AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
      saveBuilder.setTitle("Save Changes");
      saveBuilder.setMessage("Are You Sure You Want to Remove Your Request ?");
      saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          RemoveRequestsTask task = new RemoveRequestsTask();
          task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



          dialog.dismiss();

        }
      });
      saveBuilder.setNegativeButton("No", null);
      saveBuilder.create().show();
    }
    return super.onOptionsItemSelected(item);
  }



  public void editRequest(View view) {
    View v = LayoutInflater.from(MyDetailsRequestActivity.this).inflate(R.layout.edit_request_dialog, null);



    requestNameEt = v.findViewById(R.id.request_name_et);
    requestDescriptionET = v.findViewById(R.id.request_description_et);

    saveChangesB = v.findViewById(R.id.saveChangesIB);
    cancelIB = v.findViewById(R.id.cancelIB);

    cancelIB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        dialog.dismiss();
      }
    });

    requestNameEt.setText(Req_Name);
    requestDescriptionET.setText(Req_Description);


    saveChangesB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
        saveBuilder.setTitle("Save Changes");
        saveBuilder.setMessage("Are You Sure You Want to Save Changes ?");
        saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {


            sourceName = requestNameEt.getText().toString();
            requestDescription = requestDescriptionET.getText().toString();


            EditRequestTask task = new EditRequestTask();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



            dialog.dismiss();

          }
        });
        saveBuilder.setNegativeButton("No", null);
        saveBuilder.create().show();
      }

    });


    AlertDialog.Builder builder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
    builder.setTitle("Edit");
    builder.setView(v);
    dialog = builder.create();
    dialog.show();


  }

  public void removeRequest(View view) {
    AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MyDetailsRequestActivity.this);
    saveBuilder.setTitle("Save Changes");
    saveBuilder.setMessage("Are You Sure You Want to Remove Your Request ?");
    saveBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        RemoveRequestsTask task = new RemoveRequestsTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        dialog.dismiss();

      }
    });
    saveBuilder.setNegativeButton("No", null);
    saveBuilder.create().show();
  }

  private class EditRequestTask extends AsyncTask<Void, Void, EditRequest>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected EditRequest doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateRequests.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        String postParameters =
                "User_ID="+ user_id
                        +"&Req_ID="+Req_ID
                        +"&Req_Name="+sourceName
                        +"&Req_Description="+requestDescription
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

        EditRequest message = jsonParser.fromJson(
                result,
                EditRequest.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(EditRequest message) {
      super.onPostExecute(message);



      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(MyDetailsRequestActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

          requestDescriptionET.setText("");
          requestNameEt.setText("");


          Intent intent = new Intent(MyDetailsRequestActivity.this, MyRequestListActivity.class);
          startActivity(intent);

          finish();
        }

      }else
      {
        Toast.makeText(MyDetailsRequestActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }

  private class RemoveRequestsTask extends AsyncTask<Void, Void, Update>
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

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_deleteRequests.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&Req_ID="+Req_ID
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
        Toast.makeText(MyDetailsRequestActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {


          Intent intent = new Intent(MyDetailsRequestActivity.this, MyRequestListActivity.class);
          startActivity(intent);
          finish();


        }

      }else
      {
        Toast.makeText(MyDetailsRequestActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }

}
