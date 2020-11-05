package com.example.sahemproject.ui.profile;

import android.graphics.drawable.Drawable;

public class Pro {
  String Name;
  int image;
  Class<?> destinationActivity;

  public Class<?> getDestinationActivity() {
    return destinationActivity;
  }

  public void setDestinationActivity(Class<?> destinationActivity) {
    this.destinationActivity = destinationActivity;
  }

  public Pro(String name, int image, Class<?> destinationActivity) {
    Name = name;
    this.image = image;
    this.destinationActivity = destinationActivity;
  }

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
    this.image = image;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }


}
