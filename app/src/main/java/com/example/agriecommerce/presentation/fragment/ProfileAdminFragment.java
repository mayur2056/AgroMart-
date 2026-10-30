package com.example.agriecommerce.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.activity.ChatActivity;
import com.example.agriecommerce.presentation.activity.ChatUserActivity;
//import com.example.agriecommerce.presentation.activity.OrderActivity;
import com.example.agriecommerce.presentation.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileAdminFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private TextView profileName, profileEmail;
    private LinearLayout viewOrdersLayout;

    public ProfileAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_admin, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Get current user
        currentUser = mAuth.getCurrentUser();

        // Initialize UI components
        profileName = rootView.findViewById(R.id.profileName_profileFrag);
        profileEmail = rootView.findViewById(R.id.profileEmail_profileFrag);
        viewOrdersLayout = rootView.findViewById(R.id.linearLayout3);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            profileEmail.setText(currentUser.getEmail()); // Set email directly from FirebaseAuth

            // Retrieve user's name from Firebase Realtime Database
            mDatabase.child(userId).child("name").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getValue(String.class);
                    if (userName != null) {
                        profileName.setText(userName);  // Set the retrieved name
                    } else {
                        profileName.setText("Unknown User"); // Default if name is missing
                    }
                } else {
                    Log.e("ProfileFragment", "Failed to fetch name: ", task.getException());
                }
            });
        } else {
            profileName.setText("Guest");
            profileEmail.setText("Not logged in");
        }

        // Set up button click listener to view orders
        viewOrdersLayout.setOnClickListener(v -> {
            if (getActivity() != null && isAdded()) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new OrdersFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Log.e("ProfileFragment", "Fragment is not attached to an activity.");
            }
        });

        // Orders Card Click
        CardView myOrdersCard = rootView.findViewById(R.id.shippedorders_ProfilePage);
        myOrdersCard.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment, new ShippedOrderFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Chat Card Click
        CardView chatCard = rootView.findViewById(R.id.deliveredOrders_ProfilePage);
        chatCard.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment, new DeliveredOrderFragment())
                    .addToBackStack(null)
                    .commit();
        });

        CardView editCard = rootView.findViewById(R.id.editProfile_ProfilePage);
        editCard.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment, new AdminProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        CardView profileCard = rootView.findViewById(R.id.logout_ProfilePage);
        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

}
