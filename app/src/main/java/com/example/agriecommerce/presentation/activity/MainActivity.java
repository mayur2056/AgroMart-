//package com.example.agriecommerce.presentation.activity;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.view.ViewAnimationUtils;
//import android.widget.ImageView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import com.example.agriecommerce.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    private View expandingView;
//    private View button; // Get Started button
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        expandingView = findViewById(R.id.expandingView);
//        button = findViewById(R.id.button); // Reference to the Get Started button
//        ImageView imageView2 = findViewById(R.id.imageView2);
//
//        // Check if dark mode is enabled
//        int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
//        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
//            imageView2.setImageResource(R.drawable.bottom_black);
//        } else {
//            imageView2.setImageResource(R.drawable.bottom_white);
//        }
//    }
//
//    public void onGetStartedClick(View view) {
//        // Get the coordinates of the button (bottom-right corner)
//        int centerX = (int) (button.getX() + button.getWidth());
//        int centerY = (int) (button.getY() + button.getHeight());
//
//        // Calculate the final radius (distance to cover the whole screen)
//        int finalRadius = Math.max(expandingView.getWidth(), expandingView.getHeight());
//
//        // Set visibility to VISIBLE before animation starts
//        expandingView.setVisibility(View.VISIBLE);
//
//        // Create the circular reveal animation
//        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
//                expandingView, centerX, centerY, 0, finalRadius);
//        circularReveal.setDuration(3000); // 3 seconds duration
//
//        // Start the animation
//        circularReveal.start();
//
//        // Delay transition to the next activity
//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }, 2000);
//    }
//}
package com.example.agriecommerce.presentation.activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agriecommerce.R;

public class MainActivity extends AppCompatActivity {

    private View expandingView;
    private TextView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandingView = findViewById(R.id.expandingView);
        button = findViewById(R.id.button);
        ImageView imageView = findViewById(R.id.imageView2);

// Check if Dark Mode is enabled
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.bottom_black);
        } else {
            imageView.setImageResource(R.drawable.bottom_white);
        }
    }

    public void onGetStartedClick(View view) {
        // Get the button's center coordinates
        int centerX = (int) (button.getX() + button.getWidth() / 2);
        int centerY = (int) (button.getY() + button.getHeight() / 2);

        // Get the maximum radius to cover the screen
        int finalRadius = Math.max(expandingView.getWidth(), expandingView.getHeight());

        // Set expanding view visible before animation starts
        expandingView.setVisibility(View.VISIBLE);

        // Create circular reveal animation
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                expandingView, centerX, centerY, 0, finalRadius);
        circularReveal.setDuration(500); // 3 seconds duration

        // Start animation
        circularReveal.start();

        // Delay transition to next screen
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 500);
    }
}
