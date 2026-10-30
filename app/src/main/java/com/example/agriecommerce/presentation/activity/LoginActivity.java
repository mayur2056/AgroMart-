package com.example.agriecommerce.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.LoadingDialog;
import com.example.agriecommerce.utils.Extensions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private String signInEmail;
    private String signInPassword;
    private Button signInBtn;
    private EditText emailEt;
    private EditText passEt;
    private LoadingDialog loadingDialog;
    private TextView emailError;
    private TextView passwordError;
    private ImageView togglePasswordVisibility; // Toggle button for password visibility
    private boolean isPasswordVisible = false; // Track password visibility state
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // Regex patterns
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // Email validation
    private final String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$"; // Password validation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpTv = findViewById(R.id.signUpTv);
        signInBtn = findViewById(R.id.loginBtn);
        emailEt = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.PassEt_signUpPage);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility); // Initialize toggle button

        checkGooglePlayServices();
        textAutoCheck();
        loadingDialog = new LoadingDialog(this);

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility());

        signUpTv.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        signInBtn.setOnClickListener(v -> checkInput());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off); // Set icon to "hide"
        } else {
            // Show password
            passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            togglePasswordVisibility.setImageResource(R.drawable.ic_visibility); // Set icon to "show"
        }
        isPasswordVisible = !isPasswordVisible; // Toggle state
        passEt.setSelection(passEt.getText().length()); // Move cursor to the end
    }

    private void textAutoCheck() {
        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (emailEt.getText().toString().isEmpty()) {
                    emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if (Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()) {
                    emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);
                    emailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()) {
                    emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);
                    emailError.setVisibility(View.GONE);
                }
            }
        });

        passEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (passEt.getText().toString().isEmpty()) {
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if (passEt.getText().toString().matches(passwordPattern)) {
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);
                    passwordError.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passEt.getText().toString().matches(passwordPattern)) {
                    passEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);
                    passwordError.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkInput() {
        String email = emailEt.getText().toString().trim();
        String password = passEt.getText().toString().trim();

        // Email validation
        if (email.isEmpty()) {
            emailError.setVisibility(View.VISIBLE);
            emailError.setText("Email Can't be Empty");
            Toast.makeText(this, "Email field cannot be empty", Toast.LENGTH_SHORT).show(); // Toast for empty email
            return;
        }
        if (!email.matches(emailPattern)) {
            emailError.setVisibility(View.VISIBLE);
            emailError.setText("Enter Valid Email");
            return;
        }

        // Password validation
        if (password.isEmpty()) {
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Password Can't be Empty");
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_SHORT).show(); // Toast for empty password
            return;
        }
        if(password.equals("123456"))
        {


        }else if(!password.matches(passwordPattern)) {
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Password must be at least 6 characters, including one letter, one digit, and one special symbol");
            return;
        }


        // If all validations pass, proceed to sign in
        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
        signInUser();
    }

    private void signInUser() {
        loadingDialog.startLoadingDialog();
        signInEmail = emailEt.getText().toString().trim();
        signInPassword = passEt.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener(signIn -> {
                    loadingDialog.dismissDialog();
                    if (signIn.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        fetchUserTypeAndNavigate(userId);
                    } else {
                        Extensions.toast(LoginActivity.this, "Sign in failed");
                    }
                });
    }

    private void fetchUserTypeAndNavigate(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("userType")) {
                    String userType = snapshot.child("userType").getValue(String.class);

                    if ("farmer".equalsIgnoreCase(userType)) {
                        startActivity(new Intent(LoginActivity.this, FarmerActivity.class));
                    } else if ("admin".equalsIgnoreCase(userType)) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    } else if ("delivery".equalsIgnoreCase(userType)) {
                        startActivity(new Intent(LoginActivity.this, DeliveryHomeActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    finish();
                } else {
                    Extensions.toast(LoginActivity.this, "User type not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Extensions.toast(LoginActivity.this, "Failed to fetch user type: " + error.getMessage());
            }
        });
    }

    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            Log.d("GooglePlayServices", "Google Play Services is available.");
        } else {
            Log.e("GooglePlayServices", "Google Play Services is not available.");
            googleApiAvailability.getErrorDialog(this, status, 0).show();
        }
    }
}