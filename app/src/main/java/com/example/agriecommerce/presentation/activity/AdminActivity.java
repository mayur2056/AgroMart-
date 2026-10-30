package com.example.agriecommerce.presentation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.fragment.AdminCustomerFragment;
import com.example.agriecommerce.presentation.fragment.AdminFarmerFragment;
import com.example.agriecommerce.presentation.fragment.AdminHomeFragment;
import com.example.agriecommerce.presentation.fragment.AdminProfileFragment;
import com.example.agriecommerce.presentation.fragment.AdminUserListFragment;
import com.example.agriecommerce.presentation.fragment.ProfileAdminFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {


    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationView = findViewById(R.id.bottomAdminMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new AdminHomeFragment(), AdminHomeFragment.class.getSimpleName()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.homeMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new AdminHomeFragment(), AdminHomeFragment.class.getSimpleName()).commit();
                return true;
            case R.id.farmerMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new AdminFarmerFragment(), AdminFarmerFragment.class.getSimpleName()).commit();
                return true;

            case R.id.customerMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new AdminCustomerFragment(), AdminCustomerFragment.class.getSimpleName()).commit();
                return true;

            case R.id.chatMenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_fragment, new AdminUserListFragment(), "AdminUserListFragment")
                        .commit();
                return  true;



            case R.id.profileMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new ProfileAdminFragment(), ProfileAdminFragment.class.getSimpleName()).commit();
                return true;

        }


        return false;
    }
}
