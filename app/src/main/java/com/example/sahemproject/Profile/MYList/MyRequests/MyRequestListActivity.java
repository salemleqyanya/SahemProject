package com.example.sahemproject.Profile.MYList.MyRequests;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonations;
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonationsListAdapter;
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonations_Data;
import com.example.sahemproject.R;
import com.example.sahemproject.Requests.DetailsRequestActivity;
import com.example.sahemproject.Requests.Requests;
import com.example.sahemproject.Requests.RequestsAdapter;
import com.example.sahemproject.ui.requests.RequestsFragment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyRequestListActivity extends AppCompatActivity {
  ProgressDialog pBar;

  ListView requestListLV;
  MYRequestsListAdapter adapter;
  ArrayList<MyRequests> myRequests;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_request_list);
    getSupportActionBar().setTitle("My Requests ");
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");
    pBar = new ProgressDialog(MyRequestListActivity.this);
    requestListLV = findViewById(R.id.request_lv);
    myRequests = new ArrayList<>();


    adapter = new MYRequestsListAdapter(MyRequestListActivity.this,myRequests);
    requestListLV.setAdapter(adapter);

    DataRetrieveTask task = new DataRetrieveTask();
    task.execute();

    requestListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyRequests requestsData = myRequests.get(position);

        Intent intent = new Intent(MyRequestListActivity.this, MyDetailsRequestActivity.class);
        intent.putExtra("Req_ID",requestsData.getReq_ID());
        intent.putExtra("Req_Name",requestsData.getReq_Name());
        intent.putExtra("Req_Description",requestsData.getReq_Description());
        intent.putExtra("Req_PublishingTime",requestsData.getReq_PublishingTime());
        intent.putExtra("User_Name",requestsData.getUser_Name());
        startActivity(intent);
      }
    });
  }
  private class DataRetrieveTask extends AsyncTask<Void, Void, MyRequests_Data> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Getting Latest Your Requests, Please Wait ...");
      pBar.show();


    }

    @Override
    protected MyRequests_Data doInBackground(Void... voids) {



      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_selectRequests_MyList.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        String postParameters =
                "User_ID="+user_id;
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

        Log.e("test res55",result);
        Gson jsonParser = new Gson();

        MyRequests_Data message = jsonParser.fromJson(
                result,
                MyRequests_Data.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(MyRequests_Data message) {
      super.onPostExecute(message);



      if (message != null) {
        pBar.dismiss();
        if (message.getResult_code() == 1){
          myRequests.clear();
          myRequests.addAll(message.getResult_msg());
          adapter.notifyDataSetChanged();

        }


      }
    }


  }
}