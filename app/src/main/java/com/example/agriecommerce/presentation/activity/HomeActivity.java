package com.example.agriecommerce.presentation.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.fragment.BagFragment;
import com.example.agriecommerce.presentation.fragment.CustomerProfileFragment;
import com.example.agriecommerce.presentation.fragment.FavFragment;
import com.example.agriecommerce.presentation.fragment.HomeFragment;
import com.example.agriecommerce.presentation.fragment.ProfileFragment;
import com.example.agriecommerce.presentation.fragment.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.PaymentResultListener;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PaymentResultListener {

    private BottomNavigationView bottomNavigationView;
    private BagFragment bagFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.homeMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
                return true;
            case R.id.shopMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new ShopFragment(), ShopFragment.class.getSimpleName()).commit();
                return true;

        case R.id.bagMenu:
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new BagFragment(), BagFragment.class.getSimpleName()).commit();
        return true;
            case R.id.favMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new FavFragment(), FavFragment.class.getSimpleName()).commit();
                return true;
            case R.id.profileMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new ProfileFragment(), ProfileFragment.class.getSimpleName()).commit();
                return true;
    }


        return false;
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (bagFragment != null) {
//            bagFragment.handlePaymentSuccess(razorpayPaymentID);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        if (bagFragment != null) {
//            bagFragment.handlePaymentError(code, response);
        }
    }

}
