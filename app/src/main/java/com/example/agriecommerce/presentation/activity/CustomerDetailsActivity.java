package com.example.agriecommerce.presentation.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;

import java.io.Serializable;

public class CustomerDetailsActivity extends AppCompatActivity {

    private TextView userName, userEmail,userPassword,userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_profile);

        // Initialize TextViews or other views to display user data
        userName = findViewById(R.id.editTextName);
        userEmail = findViewById(R.id.editTextEmail);
        userPassword=findViewById(R.id.editTextPassword);
        userPhone=findViewById(R.id.editTextPhone);

        // Get the user data from the intent
        Users user = (Users) getIntent().getSerializableExtra("USER_DATA");

        if (user != null) {
            // Set the user data to the views
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            userPassword.setText(user.getPassword());
            userPassword.setText(user.getPhoneNumber());
        }
    }
}
