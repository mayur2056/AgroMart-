package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;


public class FarmerDetailsFragment extends Fragment {

    private ImageView profileImage;
    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
    private Users user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);

        // Initialize UI elements
//        profileImage = view.findViewById(R.id.profileImage);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);

        // Get user data from arguments
        if (getArguments() != null) {
            user = (Users) getArguments().getSerializable("USER_DATA");
            if (user != null) {
                displayUserDetails(user);
            }
        }

        return view;
    }

    // Display user details in UI elements
    private void displayUserDetails(Users user) {
        editTextName.setText(user.getName());
        editTextEmail.setText(user.getEmail());
        editTextPassword.setText(user.getPassword());  // Consider encrypting or hiding this
        editTextPhone.setText(user.getPhoneNumber());

        // Load profile image using Picasso (if URL exists)
//        if (user.getUserImage() != null && !user.getUserImage().isEmpty()) {
//            ;
//        }
    }
}
