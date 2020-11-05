package com.example.sahemproject.Requests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.MainActivity;
import com.example.sahemproject.R;
import com.example.sahemproject.ui.requests.RequestsFragment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.PendingIntent.getActivity;

public class AddRequestsActivity extends AppCompatActivity {

  EditText requestNameEt , requestDescriptionET  ;
  ProgressDialog pBar;
  Requests requestToBeAdded;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  String SourceName,Description;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_requset);
    getSupportActionBar().setTitle("Add Request");
    requestNameEt = findViewById(R.id.request_name_et);
    requestDescriptionET = findViewById(R.id.request_description_et);

    pBar = new ProgressDialog(AddRequestsActivity.this);
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);



  }

  public void share_Request(View view) {
    SourceName = requestNameEt.getText().toString();
    Description = requestDescriptionET.getText().toString();



    if (SourceName.matches("")) {
      Toast.makeText(this, "You did not enter a SourceName", Toast.LENGTH_SHORT).show();
      return;
    } else if (Description.matches("")) {
      Toast.makeText(this, "You did not enter a Description", Toast.LENGTH_SHORT).show();
      return;
    }else {

      AlertDialog.Builder addBuilder = new AlertDialog.Builder(AddRequestsActivity.this);
      addBuilder.setTitle("Confirm Add");
      addBuilder.setMessage("Are You Sure You Want to Add Request ?");
      addBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

          requestToBeAdded = new Requests();

          requestToBeAdded.setReq_Name(requestNameEt.getText().toString());
          requestToBeAdded.setReq_Description(requestDescriptionET.getText().toString());
          user_id = sharedPreferences.getString(UsersPrefs.User_ID, "No Data Set");


          AddRequestTask task = new AddRequestTask();
          task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        }


      });
      addBuilder.setNegativeButton("No", null);
      addBuilder.create().show();
    }
  }

  private class AddRequestTask extends AsyncTask<Void, Void, InsertRequest>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected InsertRequest doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_insertRequests.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        String postParameters =
                "Req_Name="+requestToBeAdded.getReq_Name()
                        +"&Req_Description="+requestToBeAdded.getReq_Description()
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

        //Log.e("test res2",result);
        Gson jsonParser = new Gson();

        InsertRequest message = jsonParser.fromJson(
                result,
                InsertRequest.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(InsertRequest message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(AddRequestsActivity.this,message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_status().equals("success"))
        {

          Intent intent = new Intent(AddRequestsActivity.this, MainActivity.class);
          startActivity(intent);

          finish();
        }

      }else
      {
        Toast.makeText(AddRequestsActivity.this,"Error!", Toast.LENGTH_SHORT).show();

        //print error
      }


    }
  }
}
