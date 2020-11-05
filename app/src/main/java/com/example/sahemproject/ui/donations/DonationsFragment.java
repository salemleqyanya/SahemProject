package com.example.sahemproject.ui.donations;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.sahemproject.Donations.AddDonationActivity;
import com.example.sahemproject.Donations.DonationDetailsActivity;
import com.example.sahemproject.Donations.Donations;
import com.example.sahemproject.Donations.DonationsAdapter;
import com.example.sahemproject.Donations.Donations_Data;
import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.MainActivity;
import com.example.sahemproject.R;
import com.example.sahemproject.ui.profile.ProfileFragment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sahemproject.R.id.add_Donations;
import static com.example.sahemproject.R.id.doneProfile;
import static com.example.sahemproject.R.id.editProfile;

public class DonationsFragment extends Fragment {
  ProgressDialog pBar;
  Context context ;
  ListView donationsListLV;
  DonationsAdapter adapter;
  ArrayList<Donations> donations;
  MenuItem add,sarche;


  public DonationsFragment(Context context){
    this.context = context;
  }

  public DonationsFragment() {
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    setHasOptionsMenu(true);
   final View root = inflater.inflate(R.layout.fragment_donations, container, false);

   pBar = new ProgressDialog(root.getContext());
    donationsListLV = root.findViewById(R.id.donationsList_lv);
    donations = new ArrayList<>();


    adapter = new DonationsAdapter(root.getContext(), donations);
    donationsListLV.setAdapter(adapter);
    DataRetrieveTask task = new DataRetrieveTask();
    task.execute();


    donationsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    Donations donationsData = donations.get(position);

    Intent intent = new Intent(root.getContext(), DonationDetailsActivity.class);
    intent.putExtra("Don_ID",donationsData.getDon_ID());
    intent.putExtra("Don_Name",donationsData.getDon_Name());
    intent.putExtra("Don_Description",donationsData.getDon_Description());
    intent.putExtra("Don_PDF",donationsData.getDon_PDF());
    intent.putExtra("Don_PublishingTime",donationsData.getDon_PublishingTime());
    intent.putExtra("User_Name",donationsData.getUser_Name());
    startActivity(intent);

  }
});





    return root;
  }
  @Override
  public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add, menu);

    add =  menu.findItem(R.id.add_Donations);


  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item) {
    System.out.println("dnkf;lm++++++++++++++++++++++++++++");
    int id = item.getItemId();

    if (id == add_Donations) {
      Intent intent = new Intent(getActivity(), AddDonationActivity.class);
      startActivity(intent);
    }





    return false;
  }



  private class DataRetrieveTask extends AsyncTask<Void, Void, Donations_Data> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
pBar.setMessage("Getting Latest Donations, Please Wait ...");
   pBar.show();


    }

    @Override
    protected Donations_Data doInBackground(Void... voids) {



      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_selectDonation.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");

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
        Log.e("test res",result);

        Gson jsonParser = new Gson();

        Donations_Data message = jsonParser.fromJson(result, Donations_Data.class);



        return message;

      } catch (Exception e)
      {
        e.printStackTrace();
      }


      return null;
    }

    @Override
    protected void onPostExecute(Donations_Data message) {
      super.onPostExecute(message);



      if (message != null) {
        pBar.dismiss();
        if (message.getResult_code() == 1){


          donations.clear();
          donations.addAll(message.getResult_msg());
          adapter.notifyDataSetChanged();

        }


      }
    }


  }
}
