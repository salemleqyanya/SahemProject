package com.example.sahemproject.Requests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sahemproject.R;

public class DetailsRequestActivity extends AppCompatActivity {
  TextView detailsDonorNameTV    , detailsDescriptionTV ;
  Button detailsSoursRequestB ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details_request);

    detailsDonorNameTV = findViewById(R.id.details_DonorName_tv);

    detailsDescriptionTV = findViewById(R.id.details_Description_tv);
//    detailsSoursRequestB = findViewById(R.id.details_soursRequest_b);



    Intent intent = getIntent();
    String Req_ID = intent.getStringExtra("Req_ID");
    String Req_Name = intent.getStringExtra("Req_Name");
    String Req_Description = intent.getStringExtra("Req_Description");
    String Req_PublishingTime = intent.getStringExtra("Req_PublishingTime");
    String User_Name = intent.getStringExtra("User_Name");
    getSupportActionBar().setTitle(Req_Name);
    //getSupportActionBar().setBackgroundDrawable();

    detailsDescriptionTV.setText(Req_Description);

    detailsDonorNameTV.setText(User_Name);




  }

}
