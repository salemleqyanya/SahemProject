package com.example.sahemproject.Donations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.R;

public class DonationDetailsActivity extends AppCompatActivity {
  TextView donationDonorNameTV, donationDescriptionTV ;
  String Don_PDF ;
  MenuItem download;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donation_details);

    sharedPreferences = getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME,MODE_PRIVATE);

    Intent intent = getIntent();
    String Don_ID = intent.getStringExtra("Don_ID");
    String Don_Name = intent.getStringExtra("Don_Name");
    String Don_Description = intent.getStringExtra("Don_Description");
    String User_Name = intent.getStringExtra("User_Name");
     Don_PDF = intent.getStringExtra("Don_PDF");
    String Don_PublishingTime = intent.getStringExtra("Don_PublishingTime");

    getSupportActionBar().setTitle(Don_Name);

    donationDonorNameTV = findViewById(R.id.donation_DonorName_tv);
    donationDescriptionTV = findViewById(R.id.donation_Description_tv);


    donationDescriptionTV.setText(Don_Description);
    donationDonorNameTV.setText("Add By "+User_Name);

  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.download, menu);
    download =  menu.findItem(R.id.Download_it);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.Download_it) {
      if (!Don_PDF.startsWith("http://") && !Don_PDF.startsWith("https://"))
        Don_PDF = "http://" + Don_PDF;


      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Don_PDF));
      startActivity(browserIntent);
      finish();

    }
    return super.onOptionsItemSelected(item);
  }


}
