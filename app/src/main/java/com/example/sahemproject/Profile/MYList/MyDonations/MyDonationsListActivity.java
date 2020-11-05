package com.example.sahemproject.Profile.MYList.MyDonations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyDonationsListActivity extends AppCompatActivity {
  ProgressDialog pBar;
  SharedPreferences sharedPreferences;
  String sharedPrefsName = "user_prefs";
  String user_id = "user_id";
  ListView donationsListLV;
  MyDonationsListAdapter adapter;

  ArrayList<MyDonations> myDonations;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_donations_list);
    getSupportActionBar().setTitle("My Donations");
    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);
    user_id = sharedPreferences.getString(UsersPrefs.User_ID,"No Data Set");
    pBar = new ProgressDialog(MyDonationsListActivity.this);
    donationsListLV = findViewById(R.id.donationsList_lv);

    myDonations = new ArrayList<>();

    adapter = new MyDonationsListAdapter(MyDonationsListActivity.this,myDonations);
   // arrayAdapter = new ArrayAdapter<MyDonations>(MyDonationsListActivity.this,R.layout.donations_item,myDonations);
    donationsListLV.setAdapter(adapter);
    DataRetrieveTask task = new DataRetrieveTask();
    task.execute();
    donationsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MyDonations donationsData = myDonations.get(position);

        Intent intent = new Intent(MyDonationsListActivity.this, MyDonationDetailsActivity.class);
        intent.putExtra("Don_ID",donationsData.getDon_ID());
        intent.putExtra("Don_Name",donationsData.getDon_Name());
        intent.putExtra("Don_Description",donationsData.getDon_Description());
        intent.putExtra("Don_PDF",donationsData.getDon_PDF());
        intent.putExtra("Don_PublishingTime",donationsData.getDon_PublishingTime());
        intent.putExtra("User_Name",donationsData.getUser_Name());
        startActivity(intent);
      }
    });
  }


  private class DataRetrieveTask extends AsyncTask<Void, Void, MyDonations_Data> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      pBar.setMessage("Getting Latest Donations, Please Wait ...");
      pBar.show();


    }

    @Override
    protected MyDonations_Data doInBackground(Void... voids) {



      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_selectDonation_MyList.php");
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

        MyDonations_Data message = jsonParser.fromJson(
                result,
                MyDonations_Data.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(MyDonations_Data message) {
      super.onPostExecute(message);



      if (message != null) {
        pBar.dismiss();
        if (message.getResult_code() == 1){
          myDonations.clear();
          myDonations.addAll(message.getResult_msg());
          adapter.notifyDataSetChanged();

        }


      }
    }


  }
}