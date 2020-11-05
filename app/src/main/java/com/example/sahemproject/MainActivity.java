package com.example.sahemproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.sahemproject.Donations.AddDonationActivity;
import com.example.sahemproject.Requests.AddRequestsActivity;
import com.example.sahemproject.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.example.sahemproject.R.id.doneProfile;
import static com.example.sahemproject.R.id.editEmail_et;
import static com.example.sahemproject.R.id.editName_et;
import static com.example.sahemproject.R.id.editProfile;
import static com.example.sahemproject.R.id.navigation_Donations;

public class MainActivity extends AppCompatActivity {
  AppBarConfiguration appBarConfiguration;
  NavController navController;
  private ActionBar toolbar;
  BottomNavigationView navView;
  EditText editNameET;
  String itemName = "";
  int name = 0;
  MenuItem menuItem,addItem ,edit,done;
  @SuppressLint("ResourceType")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);



    toolbar = getSupportActionBar();

    toolbar.setTitle("Donations");

    editNameET = findViewById(R.id.editName_et);

    navView = findViewById(R.id.nav_view);



      // Passing each menu ID as a set of Ids because each
      // menu should be considered as top level destinations.
    appBarConfiguration = new AppBarConfiguration.Builder(
            navigation_Donations, R.id.navigation_Requests, R.id.navigation_Profile)
            .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navView, navController);

    navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    NavigationUI.onNavDestinationSelected(item,navController);

    name = navView.getSelectedItemId();
    if (navView.getSelectedItemId()  == R.id.navigation_Profile){
    //  toolbar.setTitle("Table With Profile");
      toolbar.setTitle("");
//      menuItem.setVisible(false);
//      addItem.setVisible(false);
//      edit.setVisible(true);

    }else if(navView.getSelectedItemId()  == navigation_Donations) {
      toolbar.setTitle("Donations");
//      edit.setVisible(false);
//      menuItem.setVisible(true);
//      addItem.setVisible(true);
    }else {
      toolbar.setTitle("Requests");
    }
    return false;
  }
});



    itemName ="Donations";







    }




//  @SuppressLint("ResourceType")
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    MenuInflater inflater = getMenuInflater();
//    inflater.inflate(R.menu.add, menu);
//   // edit =  menu.findItem(editProfile);
//  //  done = menu.findItem(R.id.doneProfile);
//     menuItem = menu.findItem(R.id.search_view);
//  addItem = menu.findItem(R.id.add_Donations);
//
//    SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//      @Override
//      public boolean onQueryTextSubmit(String query) {
//        return false;
//      }
//
//      @Override
//      public boolean onQueryTextChange(String newText) {
//
////        if (navView.getSelectedItemId()  == 2131230936){
////          // item.setVisible(true);
////          Intent intent = new Intent(MainActivity.this, AddDonationActivity.class);
////          startActivity(intent);
////        }else if (navView.getSelectedItemId()  == 2131230938){
////          // item.setVisible(true);
////          Intent intent = new Intent(MainActivity.this, AddRequestsActivity.class);
////          startActivity(intent);
////        }
//
//    //  adapter.getFilter().filter(newText );
//        return false;
//      }
//    });
//    return true;
//  }
//  @SuppressLint("ResourceType")
//  @Override
//  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//    int id = item.getItemId();
//
//    if (id == R.id.add_Donations) {
//
//      if (navView.getSelectedItemId() == navigation_Donations) {
//        // item.setVisible(true);
//        Intent intent = new Intent(MainActivity.this, AddDonationActivity.class);
//        startActivity(intent);
//
//      } else if (navView.getSelectedItemId() == R.id.navigation_Requests) {
//        // item.setVisible(true);
//        Intent intent = new Intent(MainActivity.this, AddRequestsActivity.class);
//        startActivity(intent);
//
//
//      }
//    }
//
//
//
//      return super.onOptionsItemSelected(item);
//    }

}
