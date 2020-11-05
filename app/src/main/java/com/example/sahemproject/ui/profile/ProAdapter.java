package com.example.sahemproject.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahemproject.R;

import java.util.ArrayList;

public class ProAdapter extends BaseAdapter {
  Context context ;
  ArrayList<Pro> pros;

  public ProAdapter(Context context, ArrayList<Pro> pros) {
    this.context = context;
    this.pros = pros;
  }

  @Override
  public int getCount() {
    return pros.size();
  }

  @Override
  public Object getItem(int position) {
    return pros.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.profile_item, null);
    Pro pro = pros.get(position);
    ImageView icon;
    TextView name;
    icon = view.findViewById(R.id.pro_image);
    name = view.findViewById(R.id.pro_name);

    name.setText(pro.getName());
    icon.setImageDrawable(context.getDrawable(pro.getImage()));
    return view;
  }
}
