package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.agriecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminProfileFragment extends Fragment {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
    private Button btnEditProfile;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;

    public AdminProfileFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_profile, container, false);

        // Initialize Firebase
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
////        // Get userId from arguments
////        Bundle bundle = getArguments();
////        if (bundle != null) {
////            String userId = bundle.getString("userId");
////            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
////        }
//
//        // Get userId from arguments
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String userId = bundle.getString("userId");
//            if (userId != null && !userId.isEmpty()) {
//                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//                loadUserData(); // Load data for the selected customer
//            } else {
//                Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
//            }
//        }

//
//        if (user != null) {
//            userId = user.getUid(); // Get user UID
//        }

//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);


        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // Get userId from arguments (if passed)
        Bundle bundle = getArguments();
        if (bundle != null) {
            // If userId is passed from Admin (viewing a farmer/customer profile)
            userId = bundle.getString("userId");
        } else {
            // If no userId is passed, use the currently logged-in user's ID (farmer/customer viewing their own profile)
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getUid();
            } else {
                Toast.makeText(getContext(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
                return view;
            }
        }

        // Initialize databaseReference with the userId
        if (userId != null && !userId.isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            loadUserData(); // Load data for the user
        } else {
            Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
        }

        // Initialize UI elements
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Load User Data
        loadUserData();

        // Update User Data
        btnEditProfile.setOnClickListener(v -> updateUserProfile());

        return view;
    }

    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String phone = snapshot.child("phoneNumber").getValue(String.class);

                    // Set data in fields
                    editTextName.setText(name);
                    editTextEmail.setText(email);
                    editTextPassword.setText(password);
                    editTextPhone.setText(phone);
                } else {
                    Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update data in Firebase
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        databaseReference.child("password").setValue(password);
        databaseReference.child("phoneNumber").setValue(phone)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Update Failed!", Toast.LENGTH_SHORT).show());
    }
}
