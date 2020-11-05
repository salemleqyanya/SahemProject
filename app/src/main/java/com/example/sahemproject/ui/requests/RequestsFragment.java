package com.example.sahemproject.ui.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.sahemproject.R;
import com.example.sahemproject.Requests.AddRequestsActivity;
import com.example.sahemproject.Requests.DetailsRequestActivity;
import com.example.sahemproject.Requests.Requests;
import com.example.sahemproject.Requests.RequestsAdapter;
import com.example.sahemproject.Requests.Requests_Data;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.sahemproject.R.id.add_Donations;

public class RequestsFragment extends Fragment {
  ProgressDialog pBar;
  Context context ;
  ListView requestListLV;

  RequestsAdapter adapter;
  ArrayList<Requests> requests;
  ArrayList<String> requests2;
  MenuItem add,sarche;

  public RequestsFragment(Context context) {
    this.context = context;
  }
  public RequestsFragment() {

  }

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    setHasOptionsMenu(true);
    final View root = inflater.inflate(R.layout.fragment_requests_list, container, false);
    pBar = new ProgressDialog(root.getContext());
    requestListLV = root.findViewById(R.id.request_lv);
    requests = new ArrayList<>();



    adapter = new RequestsAdapter(root.getContext(),requests);
    requestListLV.setAdapter(adapter);
DataRetrieveTask task = new DataRetrieveTask();
    task.execute();


    requestListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Requests requestsData = requests.get(position);

    // System.out.println("ttttttoooooooottttttt"+requestsData.getReq_ID());
        Intent intent = new Intent(root.getContext(), DetailsRequestActivity.class);
        intent.putExtra("Req_ID",requestsData.getReq_ID());
        intent.putExtra("Req_Name",requestsData.getReq_Name());
        intent.putExtra("Req_Description",requestsData.getReq_Description());
        intent.putExtra("Req_PublishingTime",requestsData.getReq_PublishingTime());
        intent.putExtra("User_Name",requestsData.getUser_Name());
        startActivity(intent);

      }
    });

    return root;
  }
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add, menu);

    add =  menu.findItem(R.id.add_Donations);


  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item) {
   // System.out.println("dnkf;lm++++++++++++++++++++++++++++");
    int id = item.getItemId();

    if (id == add_Donations) {
      Intent intent = new Intent(getActivity(), AddRequestsActivity.class);
      startActivity(intent);
    }








    return true;



  }

  private class DataRetrieveTask extends AsyncTask<Void, Void, Requests_Data> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
pBar.setMessage("Getting Latest Requests, Please Wait ...");
   pBar.show();


    }

    @Override
    protected Requests_Data doInBackground(Void... voids) {


      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_selectRequests.php");
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

        Requests_Data message = jsonParser.fromJson(result, Requests_Data.class);



        return message;

      } catch (Exception e)
      {
        e.printStackTrace();
      }


      return null;
    }

    @Override
    protected void onPostExecute(Requests_Data message) {
      super.onPostExecute(message);



      if (message != null) {
        pBar.dismiss();
        if (message.getResult_code() == 1){
          requests.clear();
          requests.addAll(message.getResult_msg());
          adapter.notifyDataSetChanged();
        }


      }
    }


  }

}
