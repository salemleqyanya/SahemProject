package com.example.sahemproject.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.sahemproject.Login.SignInActivity;
import com.example.sahemproject.Login.UsersPrefs;
import com.example.sahemproject.Profile.InformationActivity;
import com.example.sahemproject.Profile.MYList.MyDonations.MyDonationsListActivity;
import com.example.sahemproject.Profile.MYList.MyDonations.Update;
import com.example.sahemproject.Profile.MYList.MyRequests.MyRequestListActivity;
import com.example.sahemproject.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sahemproject.R.id.doneProfile;
import static com.example.sahemproject.R.id.editProfile;

public class ProfileFragment extends Fragment {

  Context context;
  EditText editNameET, editEmailET, editPhoneNumberET;
  EditText currentPasswordET, newPasswordET, confirmPasswordET;
  Button saveChangesB, cancelB;
  ImageButton pickImageB;
  View root;
  ImageView userImageIV;
  AlertDialog dialog;
  TextView changePasswordTv;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String user_id = "user_id";
  String user_name = "user_name";
  String user_email = "user_email";
  String user_phoneNumber = "user_phoneNumber";
  String user_password = "user_password";
  String name,email,phoneNo;
  ProgressDialog pBar;
  String newPassword, confirmPassword, currentPassword;
  MenuItem edit,done;

  String selectedImage;
  int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
  int OPEN_GALLERY_REQUEST_CODE = 2;

  ListView profileListLV;
  ProAdapter adapter;
  ArrayList<Pro> testObectList = new ArrayList<>();
  public ProfileFragment(Context context) {
    this.context = context;
  }

  public ProfileFragment() {

  }

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
          setHasOptionsMenu(true);
    root = inflater.inflate(R.layout.fragment_profile, container, false);

    sharedPreferences = root.getContext().getSharedPreferences(UsersPrefs.SHARED_PREFS_NAME, MODE_PRIVATE);
    userImageIV = root.findViewById(R.id.userImage_iv);
    pBar = new ProgressDialog(root.getContext());




    changePasswordTv = root.findViewById(R.id.changePassword_tv);
    editEmailET = root.findViewById(R.id.editEmail_et);
    editNameET = root.findViewById(R.id.editName_et);
    pickImageB = root.findViewById(R.id.pickImageB);
    editPhoneNumberET = root.findViewById(R.id.editPhoneNo_et);
    editNameET.setEnabled(false);
    editEmailET.setEnabled(false);
    editPhoneNumberET.setEnabled(false);
    changePasswordTv.setEnabled(false);
    pickImageB.setEnabled(false);
    changePasswordTv.setVisibility(View.INVISIBLE);


    selectedImage = sharedPreferences.getString(UsersPrefs.USER_PHOTO, "No Data Set");
    user_id = sharedPreferences.getString(UsersPrefs.User_ID, "No Data Set");
    user_name = sharedPreferences.getString(UsersPrefs.User_Name, "No Data Set");
    user_email = sharedPreferences.getString(UsersPrefs.User_Email, "No Data Set");
    user_phoneNumber = sharedPreferences.getString(UsersPrefs.User_PhoneNumber, "No Data Set");
    user_password = sharedPreferences.getString(UsersPrefs.User_Password, "No Data Set");
    editNameET.setText(user_name);
    editEmailET.setText(user_email);
    editPhoneNumberET.setText(user_phoneNumber);
    Bitmap bmp = BitmapFactory.decodeFile(selectedImage);
    userImageIV.setImageBitmap(bmp);
     profileListLV = root.findViewById(R.id.profileList_lv);

    adapter = new ProAdapter(getActivity(),testObectList);
    profileListLV.setAdapter(adapter);
    testObectList.add(new Pro("My Donations",R.drawable.ic_knowledge,MyDonationsListActivity.class));
    testObectList.add(new Pro("My Requests",R.drawable.ic_question,MyRequestListActivity.class));
    testObectList.add(new Pro("Information",R.drawable.ic_info,InformationActivity.class));
    testObectList.add(new Pro("LogOut",R.drawable.ic_logout,SignInActivity.class));
    profileListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(),testObectList.get(position).destinationActivity));
        Pro pro = testObectList.get(position);
          if (pro.getName().matches("LogOut")){
                    editor = sharedPreferences.edit();
        editor.clear();

        editor.putString(UsersPrefs.User_ID, "user_id");
        editor.putString(UsersPrefs.User_Password, "user_password");
        editor.commit();

        Intent intent = new Intent(root.getContext(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
          }




      }
    });
    pickImageB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

          if(root.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED)
          {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
          }else
          {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
          }

        }else
        {
          Intent intent = new Intent(Intent.ACTION_PICK);
          intent.setType("image/*");
          startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
        }

      }
    });


    changePasswordTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final View view = LayoutInflater.from(root.getContext()).inflate(R.layout.change_password, null);

        currentPasswordET = view.findViewById(R.id.current_password_et);
        newPasswordET = view.findViewById(R.id.new_password_et);
        confirmPasswordET = view.findViewById(R.id.confirm_password_et);
        saveChangesB = view.findViewById(R.id.saveChangesB);
        cancelB = view.findViewById(R.id.cancelB);

        cancelB.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });

        saveChangesB.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


            currentPassword = currentPasswordET.getText().toString();
            newPassword = newPasswordET.getText().toString();
            confirmPassword = confirmPasswordET.getText().toString();

            user_id = sharedPreferences.getString(UsersPrefs.User_ID, "No Data Set");

            if (currentPassword.matches("")) {
              Toast.makeText(root.getContext(), "You did not enter a currentPassword", Toast.LENGTH_SHORT).show();
              return;
            } else if (newPassword.matches("")) {
              Toast.makeText(root.getContext(), "You did not enter a NewPassword", Toast.LENGTH_SHORT).show();
              return;
            } else if (confirmPassword.matches("")) {
              Toast.makeText(root.getContext(), "You did not enter a confirmPassword", Toast.LENGTH_SHORT).show();
              return;
            } else {


              if (newPassword.matches(confirmPassword)) {
                byte[] decodePassword;
                decodePassword = Base64.decode(user_password, Base64.DEFAULT);
                user_password = new String(decodePassword, StandardCharsets.UTF_8);

                if (currentPassword.equals(user_password)) {

                  EditUserPasswordTask task = new EditUserPasswordTask();
                  task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                  Toast.makeText(root.getContext(), "Your Password Incorect", Toast.LENGTH_SHORT).show();
                }

              } else {
                Toast.makeText(root.getContext(), "password is not equal  confirmPassword", Toast.LENGTH_SHORT).show();

              }

            }


            //
          }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());

        builder.setTitle("      Reset Password");


        builder.setView(view);

        dialog = builder.create();
        dialog.show();
      }
    });




    return root;
  }

  @Override
  public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.edit_profile, menu);

    edit =  menu.findItem(R.id.editProfile);
    done = menu.findItem(R.id.doneProfile);

  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item) {

    int id = item.getItemId();

    if (id == editProfile) {

      done.setVisible(true);
      edit.setVisible(false);

      editNameET.setEnabled(true);
      editEmailET.setEnabled(true);
      editPhoneNumberET.setEnabled(true);
      changePasswordTv.setEnabled(true);
      pickImageB.setEnabled(true);
      changePasswordTv.setVisibility(View.VISIBLE);
    }if (id == doneProfile){



      EditUserTask task = new EditUserTask();
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//      ImageTask task1 = new ImageTask();
//      task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      editEmailET.setEnabled(false);
      editNameET.setEnabled(false);
      editPhoneNumberET.setEnabled(false);
      pickImageB.setEnabled(false);
    }
    return false;
  }
      private class EditUserPasswordTask extends AsyncTask<Void, Void, Update> {

        @Override
        protected void onPreExecute() {
          super.onPreExecute();
          pBar.setMessage("Buffering ...");
          pBar.show();
        }

        @Override
        protected Update doInBackground(Void... voids) {

          try {
            String result = "";

            HttpURLConnection urlConnection;
            BufferedReader reader = null;

            URL url = null;

            url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateUserProfile_Password.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");


            String postParameters =
                    "User_ID=" + user_id
                            + "&User_Password=" + newPassword;
            urlConnection.setFixedLengthStreamingMode(
                    postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();

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

            Log.e("test res42", result);
            Gson jsonParser = new Gson();

            Update message = jsonParser.fromJson(
                    result,
                    Update.class
            );


            return message;


          } catch (Exception e) {
            e.printStackTrace();

          }

          return null;

        }

        @Override
        protected void onPostExecute(Update message) {
          super.onPostExecute(message);


          if (message != null) {
            pBar.dismiss();
            Toast.makeText(root.getContext(), message.getResult_status(), Toast.LENGTH_SHORT).show();

            if (message.getResult_code() == 1) {


              currentPasswordET.setText("");
              newPasswordET.setText("");
              confirmPasswordET.setText("");
              editor = sharedPreferences.edit();
              editor.clear();

              editor.putString(UsersPrefs.User_ID, "user_id");
              editor.putString(UsersPrefs.User_Password, "user_password");
              editor.commit();

              dialog.dismiss();

            } else {
              Toast.makeText(root.getContext(), "Your Password Incorect", Toast.LENGTH_SHORT).show();
            }

          } else {
            Toast.makeText(root.getContext(), "Error!", Toast.LENGTH_SHORT).show();
            pBar.dismiss();
            //print error
          }


        }
      }
  private class EditUserTask extends AsyncTask<Void, Void, Update>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      name = editNameET.getText().toString();
      email = editEmailET.getText().toString();
      phoneNo = editPhoneNumberET.getText().toString();
      System.out.println("dnkf;lm++++++++++++++++++++++++++++"+name);
      System.out.println("dnkf;lm++++++++++++++++++++++++++++"+user_id);
      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected Update doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_updateUserProfile.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&User_Name="+name
                        + "&User_Email="+email
                        + "&User_PhoneNumber="+phoneNo
                    + "&Image_URL="+selectedImage;

        urlConnection.setFixedLengthStreamingMode(
                postParameters.getBytes().length);
        PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
        out.print(postParameters);
        out.close();

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

        Log.e("test res422",result);
        Gson jsonParser = new Gson();

        Update message = jsonParser.fromJson(
                result,
                Update.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(Update message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(root.getContext(),message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

//          editor = sharedPreferences.edit();
//          editor.putString(UsersPrefs.User_Name ,userName );
//          editor.apply();

          editor = sharedPreferences.edit();


          editor.putString(UsersPrefs.User_Name, name);
          editor.putString(UsersPrefs.User_Email, email);
          editor.putString(UsersPrefs.User_PhoneNumber, phoneNo);
          editor.commit();

          editNameET.setEnabled(false);
          editEmailET.setEnabled(false);
          editPhoneNumberET.setEnabled(false);
          changePasswordTv.setEnabled(false);
          changePasswordTv.setVisibility(View.INVISIBLE);
          done.setVisible(false);
          edit.setVisible(true);
//          Intent intent = new Intent(root.getContext(), MainActivity.class);
//          startActivity(intent);



        }

      }else
      {
        Toast.makeText(root.getContext(),"Error!", Toast.LENGTH_SHORT).show();
          pBar.dismiss();
        //print error
      }


    }
  }
  private class ImageTask extends AsyncTask<Void, Void, Update>
  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();


      pBar.setMessage("Buffering ...");
      pBar.show();
    }

    @Override
    protected Update doInBackground(Void... voids) {

      try{
        String result = "";

        HttpURLConnection urlConnection;
        BufferedReader reader = null;

        URL url = null;

        url = new URL("https://ahmadhababa.000webhostapp.com/Sahem/sahem_Image.php");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        String postParameters =
                "User_ID="+ user_id
                        + "&Image_URL="+selectedImage;

        urlConnection.setFixedLengthStreamingMode(
                postParameters.getBytes().length);
        PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
        out.print(postParameters);
        out.close();

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

        Log.e("test res422",result);
        Gson jsonParser = new Gson();

        Update message = jsonParser.fromJson(
                result,
                Update.class
        );



        return message;


      } catch (Exception e)
      {
        e.printStackTrace();

      }

      return null;

    }

    @Override
    protected void onPostExecute(Update message) {
      super.onPostExecute(message);




      if(message != null)
      {
        pBar.dismiss();
        Toast.makeText(root.getContext(),message.getResult_status(), Toast.LENGTH_SHORT).show();

        if(message.getResult_code() == 1)
        {

//          editor = sharedPreferences.edit();
//          editor.putString(UsersPrefs.User_Name ,userName );
//          editor.apply();

          editor = sharedPreferences.edit();

          editor.putString(UsersPrefs.USER_PHOTO, selectedImage);
          editor.commit();

          editNameET.setEnabled(false);
          editEmailET.setEnabled(false);
          editPhoneNumberET.setEnabled(false);
          changePasswordTv.setEnabled(false);
          changePasswordTv.setVisibility(View.INVISIBLE);
          done.setVisible(false);
          edit.setVisible(true);
//          Intent intent = new Intent(root.getContext(), MainActivity.class);
//          startActivity(intent);



        }

      }else
      {
        Toast.makeText(root.getContext(),"Error66!", Toast.LENGTH_SHORT).show();
        pBar.dismiss();
        //print error
      }


    }
  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if(requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE)
    {
      if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
      {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
      }else
      {
        Toast.makeText(root.getContext(), "تحتاج لصلاحية لاختيار صورة", Toast.LENGTH_SHORT).show();
      }
    }

  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == OPEN_GALLERY_REQUEST_CODE)
    {
      if(resultCode == Activity.RESULT_OK)
      {
        Uri imageUri = data.getData();
        String[] filePathCol = {MediaStore.Images.Media.DATA};
        Cursor cursor = root.getContext().getContentResolver().query(imageUri, filePathCol, null,null,null);
        cursor.moveToNext();
        int col_index = cursor.getColumnIndex(filePathCol[0]);
        selectedImage = cursor.getString(col_index);
        cursor.close();

        Bitmap bmp = BitmapFactory.decodeFile(selectedImage);
        userImageIV.setImageBitmap(bmp);
        editor = sharedPreferences.edit();
        editor.putString(UsersPrefs.USER_PHOTO ,selectedImage );
        editor.commit();

//        System.out.println("+++++++++++++++======___--+++"+selectedImage);
//        System.out.println("+++++++++++++++======___--+++"+bmp);
      }
    }

  }
}
