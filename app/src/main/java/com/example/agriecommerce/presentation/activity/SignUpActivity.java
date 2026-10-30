package com.example.agriecommerce.presentation.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.utils.Extensions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullName, emailEt, passEt, CpassEt, phoneEt;
    private Spinner userTypeSpinner;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    private ImageView togglePasswordVisibility, toggleConfirmPasswordVisibility; // Toggle buttons for password visibility
    private boolean isPasswordVisible = false; // Track password visibility state
    private boolean isConfirmPasswordVisible = false; // Track confirm password visibility state

    // Regex patterns
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String fullNamePattern = "^[A-Za-z]+(\\s[A-Za-z]+){1,2}$"; // First, Middle, Last Name
    private final String phonePattern = "^[6-9]\\d{9}$"; // 10-digit phone number starting with 6/7/8/9
    private final String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$"; // Minimum 6 characters, at least one letter, one digit, and one special symbol

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI components
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        phoneEt = findViewById(R.id.phoneEt_signUpPage);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility); // Toggle button for password
        toggleConfirmPasswordVisibility = findViewById(R.id.toggleConfirmPasswordVisibility); // Toggle button for confirm password

        // Define the options for the Spinner
        String[] userTypes = {"Farmer", "Customer"};
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        // Create an ArrayAdapter using a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        userTypeSpinner.setAdapter(adapter);

        Button signUpBtn = findViewById(R.id.signUpBtn_signUpPage);
        fullName = findViewById(R.id.nameEt_signUpPage);
        emailEt = findViewById(R.id.emailEt_signUpPage);
        passEt = findViewById(R.id.PassEt_signUpPage);
        CpassEt = findViewById(R.id.cPassEt_signUpPage);
        TextView signInTv = findViewById(R.id.signInTv_signUpPage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users");

        progressDialog = new ProgressDialog(this);

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(passEt, togglePasswordVisibility));
        toggleConfirmPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(CpassEt, toggleConfirmPasswordVisibility));

        // SignIn redirect
        signInTv.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // SignUp button click listener
        signUpBtn.setOnClickListener(v -> checkInput());
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleButton) {
        if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Show password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility); // Set icon to "show"
        } else {
            // Hide password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility_off); // Set icon to "hide"
        }
        editText.setSelection(editText.getText().length()); // Move cursor to the end
    }

    private void checkInput() {
        String fullNameText = fullName.getText().toString().trim();
        String emailText = emailEt.getText().toString().trim();
        String phoneText = phoneEt.getText().toString().trim();
        String passwordText = passEt.getText().toString().trim();
        String confirmPasswordText = CpassEt.getText().toString().trim();

        // Full Name Validation
        if (fullNameText.isEmpty()) {
            Extensions.toast(this, "Name can't be empty!");
            return;
        }
        if (!fullNameText.matches(fullNamePattern)) {
            Extensions.toast(this, "Enter a valid Full Name (First, Middle, Last)");
            return;
        }

        // Email Validation
        if (emailText.isEmpty()) {
            Extensions.toast(this, "Email can't be empty!");
            return;
        }
        if (!emailText.matches(emailPattern)) {
            Extensions.toast(this, "Enter a valid Email");
            return;
        }

        // Phone Number Validation
        if (phoneText.isEmpty()) {
            Extensions.toast(this, "Phone number can't be empty!");
            return;
        }
        if (!phoneText.matches(phonePattern)) {
            Extensions.toast(this, "Enter a valid 10-digit Phone Number starting with 6/7/8/9");
            return;
        }

        // Password Validation
        if (passwordText.isEmpty()) {
            Extensions.toast(this, "Password can't be empty!");
            return;
        }
        if (!passwordText.matches(passwordPattern)) {
            Extensions.toast(this, "Password must be at least 6 characters, including one letter, one digit, and one special symbol");
            return;
        }

        // Confirm Password Validation
        if (confirmPasswordText.isEmpty()) {
            Extensions.toast(this, "Confirm Password can't be empty!");
            return;
        }
        if (!confirmPasswordText.equals(passwordText)) {
            Extensions.toast(this, "Passwords do not match!");
            return;
        }

        // If all validations pass, proceed to sign up
        signUpUser();
    }

    private void signUpUser() {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Account");
        progressDialog.show();

        String emailV = emailEt.getText().toString();
        String passV = passEt.getText().toString();
        String fullname = fullName.getText().toString();
        String phoneNumber = phoneEt.getText().toString();
        String userType = userTypeSpinner.getSelectedItem().toString();
        String userImage = ""; // Initialize userImage as an empty string
        String approval = "false";

        firebaseAuth.createUserWithEmailAndPassword(emailV, passV)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.setMessage("Saving User Data");
                        String userId = firebaseAuth.getUid();
                        Users user = new Users(fullname, phoneNumber, userId, emailV, userType, userImage, passV, approval);
                        storeUserData(user);
                    } else {
                        progressDialog.dismiss();
                        Exception e = task.getException();
                        Extensions.toast(SignUpActivity.this, "Authentication Failed: " + e.getMessage());
                    }
                });
    }

    private void storeUserData(Users user) {
        String userId = firebaseAuth.getUid();
        if (userId != null) {
            userRef.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Extensions.toast(SignUpActivity.this, "Data Saved");
                        progressDialog.dismiss();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Extensions.toast(SignUpActivity.this, "Error: " + e.getMessage());
                        progressDialog.dismiss();
                    });
        }
    }
}