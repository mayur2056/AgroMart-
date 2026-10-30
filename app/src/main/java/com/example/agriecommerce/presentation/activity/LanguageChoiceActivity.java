package com.example.agriecommerce.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agriecommerce.R;

public class LanguageChoiceActivity extends AppCompatActivity {

    private Button btnEnglish, btnMarathi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);

        btnEnglish = findViewById(R.id.btnEnglish);
        btnMarathi = findViewById(R.id.btnMarathi);

        // Set click listeners to change language
        btnEnglish.setOnClickListener(view -> {
            changeLanguage("en");
            navigateToMainActivity();
        });

        btnMarathi.setOnClickListener(view -> {
            changeLanguage("mr");
            navigateToMainActivity();
        });
    }

    private void changeLanguage(String languageCode) {
        // Change language programmatically using the utility method
        LanguageUtil.setLocale(this, languageCode);
    }

    private void navigateToMainActivity() {
        // After choosing language, navigate to the Main Activity
        Intent intent = new Intent(LanguageChoiceActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
