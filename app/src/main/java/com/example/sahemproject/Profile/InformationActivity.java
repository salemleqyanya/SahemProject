package com.example.sahemproject.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahemproject.R;

public class InformationActivity extends AppCompatActivity {
TextView informationTV;
ImageView photoAppIV;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_information);
    getSupportActionBar().setTitle("Information");
    getSupportActionBar().hide();
    informationTV = findViewById(R.id.information_tv);
    photoAppIV = findViewById(R.id.photoApp_iv);

    informationTV.setText("تطبيق يساعد الطلاب في البحث عن المصادر التعليمية التي يحتاجونها في مساقاتهم ، بحيث يستطيع الطلاب البحث او ادراج المصادر التي يملكونها و يريدون التبرع بها");

  }


}
