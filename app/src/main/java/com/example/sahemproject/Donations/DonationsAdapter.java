package com.example.sahemproject.Donations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sahemproject.R;

import java.util.ArrayList;

public class DonationsAdapter extends BaseAdapter {
  Context context ;
  ArrayList<Donations> donationsList ;

  public DonationsAdapter(Context context, ArrayList<Donations> donationsList) {
    this.context = context;
    this.donationsList = donationsList;
  }

  @Override
  public int getCount() {
    return donationsList.size();
  }

  @Override
  public Object getItem(int position) {
    return donationsList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.donations_item, null);
    Donations donations = donationsList.get(position);

    TextView bookTitle , addPy ;
    bookTitle = view.findViewById(R.id.book_title_tv);
    addPy = view.findViewById(R.id.addPy_tv);
    bookTitle.setText(donations.getDon_Name());
    addPy.setText("Add By "+donations.getUser_Name());
    return view;
  }
}
