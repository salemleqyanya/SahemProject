package com.example.sahemproject.Profile.MYList.MyDonations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sahemproject.R;

import java.util.ArrayList;

public class MyDonationsListAdapter extends BaseAdapter {
  Context context ;
  ArrayList<MyDonations> myDonationsList;

  public MyDonationsListAdapter( Context context, ArrayList<MyDonations> myDonationsList) {

    this.context = context;
    this.myDonationsList = myDonationsList;
  }


  @Override
  public int getCount() {
    return myDonationsList.size();
  }

  @Override
  public Object getItem(int position) {
    return myDonationsList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.donations_item, null);
    MyDonations donations = myDonationsList.get(position);

    TextView bookTitle , addPy ;
    bookTitle = view.findViewById(R.id.book_title_tv);
    addPy = view.findViewById(R.id.addPy_tv);
    bookTitle.setText(donations.getDon_Name());
    addPy.setText("Add By "+donations.getUser_Name());
    return view;
  }
}
