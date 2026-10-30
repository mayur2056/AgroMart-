//package com.example.agriecommerce.presentation.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.presentation.fragment.FarmerHomeFragment;
//import com.example.agriecommerce.presentation.fragment.FragmentFarmerProfile;
//import com.example.agriecommerce.presentation.fragment.ProductUploadFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class FarmerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//
//    private BottomNavigationView bottomNavigationView;
//    private DatabaseReference databaseReference;
//    private FirebaseAuth auth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_farmer);
//
//        // Initialize Firebase Auth and Database
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            // Get the current user's ID
//            String userId = currentUser.getUid();
//            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
//            // Retrieve the approval status
//            checkApprovalStatus();
//        } else {
//            // Handle case where user is not logged in
//            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
//            finish(); // Close the activity
//        }
//
//        // Initialize bottom navigation
//        bottomNavigationView = findViewById(R.id.bottomFarmerMenu);
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//    }
//
//    // Check the approval status
//    private void checkApprovalStatus() {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String approvalStatus = snapshot.child("approval").getValue(String.class);
//
//                    // Check if approval is false
//                    if ("false".equals(approvalStatus)) {
//                        // Redirect to UploadFarmerDocumentActivity
//                        Intent intent = new Intent(FarmerActivity.this, UploadFarmerDocumentActivity.class);
//                        startActivity(intent);
//                        finish(); // Finish the current activity to prevent going back
//                    } else {
//                        // Approval is true, proceed with normal flow
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.nav_fragment, new FarmerHomeFragment(), FarmerHomeFragment.class.getSimpleName())
//                                .commit();
//                    }
//                } else {
//                    Toast.makeText(FarmerActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(FarmerActivity.this, "Failed to fetch approval status.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.shopMenu:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_fragment, new ProductUploadFragment(), ProductUploadFragment.class.getSimpleName())
//                        .commit();
//                return true;
//
//            case R.id.homeMenu:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_fragment, new FarmerHomeFragment(), FarmerHomeFragment.class.getSimpleName())
//                        .commit();
//                return true;
//
//            case R.id.profileMenu:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_fragment, new FragmentFarmerProfile(), FragmentFarmerProfile.class.getSimpleName())
//                        .commit();
//                return true;
//        }
//        return false;
//    }
//}
package com.example.agriecommerce.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.fragment.FarmerHomeFragment;
import com.example.agriecommerce.presentation.fragment.FragmentFarmerProfile;
import com.example.agriecommerce.presentation.fragment.ProductUploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FarmerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Constants for Firebase keys
    private static final String USERS_NODE = "Users";
    private static final String APPROVAL_KEY = "approval";

    private BottomNavigationView bottomNavigationView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Get the current user's ID
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE).child(userId);

            // Retrieve the approval status
            checkApprovalStatus();
        } else {
            // Handle case where user is not logged in
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottomFarmerMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    // Check the approval status
    private void checkApprovalStatus() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String approvalStatus = snapshot.child(APPROVAL_KEY).getValue(String.class);

                    // Handle null or missing approval status
                    if (approvalStatus == null) {
                        approvalStatus = "false"; // Default to false
                    }
                    // Update the user's data in Firebase to include the approval field
                    databaseReference.child(APPROVAL_KEY).setValue(approvalStatus);

                    // Check if approval is false
                    if ("false".equalsIgnoreCase(approvalStatus)) {
                        // Redirect to UploadFarmerDocumentActivity
                        Intent intent = new Intent(FarmerActivity.this, UploadFarmerDocumentActivity.class);
                        startActivity(intent);
                        finish(); // Finish the current activity to prevent going back
                    } else {
                        // Approval is true, proceed with normal flow
                        loadFragment(new FarmerHomeFragment(), FarmerHomeFragment.class.getSimpleName());
                    }
                } else {
                    Toast.makeText(FarmerActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FarmerActivity.this, "Failed to fetch approval status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Redirect to the login activity
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Load a fragment
    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);

        if (existingFragment == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, tag)
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopMenu:
                loadFragment(new ProductUploadFragment(), ProductUploadFragment.class.getSimpleName());
                return true;

            case R.id.homeMenu:
                loadFragment(new FarmerHomeFragment(), FarmerHomeFragment.class.getSimpleName());
                return true;

            case R.id.profileMenu:
                loadFragment(new FragmentFarmerProfile(), FragmentFarmerProfile.class.getSimpleName());
                return true;
        }
        return false;
    }
}