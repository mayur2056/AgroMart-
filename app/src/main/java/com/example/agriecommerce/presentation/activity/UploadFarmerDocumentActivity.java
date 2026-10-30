package com.example.agriecommerce.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.agriecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Map;

public class UploadFarmerDocumentActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    private ImageView profileImage;
    private Button btnChooseImage, btnEditProfile;
    private TextView tvStatus;
    private Uri selectedImageUri;
    private String uploadedImageUrl;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Cloudinary cloudinary;

    private final String CLOUD_NAME = "dz36r5plo";
    private final String API_KEY = "625591387314229";
    private final String API_SECRET = "AxGPEF3FsM2Fp2NJ_WN7OTAuyIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_farmer_document);

        // Initialize views
        profileImage = findViewById(R.id.profileImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvStatus = findViewById(R.id.tvStatus);

        // Initialize Firebase and Cloudinary
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        } else {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));

        // Load user data
        loadUserData();

        // Choose image button click listener
        btnChooseImage.setOnClickListener(v -> selectImage());

        // Upload image button click listener
        btnEditProfile.setOnClickListener(v -> updateUserProfile());

        // Long press listener for profile image
        profileImage.setOnLongClickListener(v -> {
            if (uploadedImageUrl != null) {
                Intent intent = new Intent(this, com.example.agriecommerce.presentation.activity.FullScreenImageActivity.class);
                intent.putExtra("image_url", uploadedImageUrl);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Open image picker
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(this)
                        .load(selectedImageUri)
                        .placeholder(R.drawable.ic_image_final)
                        .error(R.drawable.ic_image_final)
                        .into(profileImage);
                profileImage.setVisibility(View.VISIBLE);
            }
        }
    }

    // Load user data from Firebase
    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("userImage").getValue(String.class);

                    // Load profile image if available
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        if (profileImageUrl.startsWith("http://")) {
                            profileImageUrl = profileImageUrl.replace("http://", "https://");
                        }

                        Glide.with(UploadFarmerDocumentActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_image_final)
                                .error(R.drawable.ic_image_final)
                                .into(profileImage);
                        profileImage.setVisibility(View.VISIBLE);
                        uploadedImageUrl = profileImageUrl; // Save the image URL for full-screen view
                    } else {
                        Log.d("ProfileImageUrl", "No image URL found in Firebase.");
                    }
                } else {
                    Toast.makeText(UploadFarmerDocumentActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UploadFarmerDocumentActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update user profile
    private void updateUserProfile() {
        if (selectedImageUri != null) {
            uploadImageToCloudinary();
        } else {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
        }
    }

    // Upload image to Cloudinary
    private void uploadImageToCloudinary() {
        setStatus("Uploading image...");

        new Thread(() -> {
            try {
                File file = new File(getRealPathFromURI(selectedImageUri));
                if (!file.exists()) {
                    runOnUiThread(() -> {
                        setStatus("File not found!");
                        Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                uploadedImageUrl = (String) uploadResult.get("url");

                runOnUiThread(() -> {
                    setStatus("Upload successful!\n Pending for Approval");
                    Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    saveUserData(uploadedImageUrl);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    setStatus("Upload failed: " + e.getMessage());
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // Save user data to Firebase
    private void saveUserData(String imageUrl) {
        databaseReference.child("userImage").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
    }

    // Get real path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

    // Update status message
    private void setStatus(String message) {
        tvStatus.setText(message);
    }
}