package com.example.sahemproject.Requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahemproject.Donations.Donations;
import com.example.sahemproject.R;

import java.util.ArrayList;

public class RequestsAdapter extends BaseAdapter {
  Context context;

  ArrayList<Requests> requestsList ;

  public RequestsAdapter(Context context, ArrayList<Requests> requestsList) {
    this.context = context;
    this.requestsList = requestsList;
  }

  @Override
  public int getCount() {
    return requestsList.size();
  }

  @Override
  public Object getItem(int position) {
    return requestsList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.requests_item, null);
    Requests requests = requestsList.get(position);

    TextView bookTitle,addPy;

    bookTitle = view.findViewById(R.id.book_title_tv);
    addPy = view.findViewById(R.id.addPy_tv);

    bookTitle.setText(requests.getReq_Name());
    addPy.setText("Add By "+requests.getUser_Name());


    return view;
  }
}
