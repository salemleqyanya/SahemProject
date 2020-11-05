package com.example.sahemproject.Profile.MYList.MyRequests;

import com.example.sahemproject.Requests.Requests;

import java.util.ArrayList;

public class MyRequests_Data {

  String result_status;
  int result_code ;
  ArrayList<MyRequests> result_msg ;

  public String getResult_status() {
    return result_status;
  }

  public void setResult_status(String result_status) {
    this.result_status = result_status;
  }

  public int getResult_code() {
    return result_code;
  }

  public void setResult_code(int result_code) {
    this.result_code = result_code;
  }

  public ArrayList<MyRequests> getResult_msg() {
    return result_msg;
  }

  public void setResult_msg(ArrayList<MyRequests> result_msg) {
    this.result_msg = result_msg;
  }
}
