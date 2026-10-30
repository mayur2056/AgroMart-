package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;

public class CustomerDetailsFragment extends Fragment {

    private EditText editTextName, editTextEmail, editTextPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_details, container, false);

        // Initialize views
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);

        // Get the user data from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            Users user = (Users) bundle.getSerializable("USER_DATA");

            if (user != null) {
                // Populate the fields with the user data
                editTextName.setText(user.getName());
                editTextEmail.setText(user.getEmail());
                // You can add more fields here as needed
            }
        }

        return view;
    }
}
