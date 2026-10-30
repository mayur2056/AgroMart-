//package com.example.agriecommerce.presentation.fragment;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.presentation.activity.FullScreenImageActivity;
//import com.example.agriecommerce.presentation.activity.LoginActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.io.File;
//import java.util.Map;
//
//public class FragmentFarmerProfile extends Fragment {
//
//    private static final int PICK_IMAGE_REQUEST = 100;
//    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
//    private Button btnEditProfile, btnChooseImage, approveBtn;
//    private ImageView profileImage, logout;
//    private DatabaseReference databaseReference;
//    private FirebaseAuth auth;
//    private Uri selectedImageUri;
//    private Cloudinary cloudinary;
//    private String uploadedImageUrl;
//    private final String CLOUD_NAME = "dz36r5plo";
//    private final String API_KEY = "625591387314229";
//    private final String API_SECRET = "AxGPEF3FsM2Fp2NJ_WN7OTAuyIA";
//
//    public FragmentFarmerProfile() {
//        // Required empty constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.new_profile, container, false);
//
//        // Initialize Firebase Auth and Database
//        auth = FirebaseAuth.getInstance();
//
//        // Get userId from arguments (if passed)
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String userId = bundle.getString("userId");
//            if (userId != null && !userId.isEmpty()) {
//                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//            } else {
//                Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            FirebaseUser currentUser = auth.getCurrentUser();
//            if (currentUser != null) {
//                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
//            } else {
//                Toast.makeText(getContext(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
//                return view;
//            }
//        }
//
//        // Initialize views
//        editTextName = view.findViewById(R.id.editTextName);
//        editTextEmail = view.findViewById(R.id.editTextEmail);
//        editTextPassword = view.findViewById(R.id.editTextPassword);
//        editTextPhone = view.findViewById(R.id.editTextPhone);
//        btnEditProfile = view.findViewById(R.id.btnEditProfile);
//        btnChooseImage = view.findViewById(R.id.btnChooseImage);
//        profileImage = view.findViewById(R.id.profileImage);
//        logout = view.findViewById(R.id.imgLogout);
//        approveBtn = view.findViewById(R.id.approveBtn);
//
//        // Logout button click listener
//        logout.setOnClickListener(v -> {
//            auth.signOut();
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        });
//
//        // Load user data
//        loadUserData();
//
//        // Choose image button click listener
//        btnChooseImage.setOnClickListener(v -> selectImage());
//
//        // Edit profile button click listener
//        btnEditProfile.setOnClickListener(v -> updateUserProfile());
//
//        // Long press listener for profile image
//        profileImage.setOnLongClickListener(v -> {
//            if (uploadedImageUrl != null) {
//                Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
//                intent.putExtra("image_url", uploadedImageUrl);
//                startActivity(intent);
//            } else {
//                Toast.makeText(getContext(), "No image to display", Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        });
//
//        // Initialize Cloudinary
//        cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", CLOUD_NAME,
//                "api_key", API_KEY,
//                "api_secret", API_SECRET
//        ));
//
//        // Check if the logged-in user is an admin
//        checkIfAdmin();
//
//        return view;
//    }
//
//    // Check if the logged-in user is an admin
//    private void checkIfAdmin() {
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
//            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        String userType = snapshot.child("userType").getValue(String.class);
//                        if ("admin".equalsIgnoreCase(userType)) {
//                            approveBtn.setVisibility(View.VISIBLE); // Show the approve button for admin
//                        } else {
//                            approveBtn.setVisibility(View.GONE); // Hide the approve button for non-admin
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getContext(), "Failed to fetch user type.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    // Load user data from Firebase
//    private void loadUserData() {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String name = snapshot.child("name").getValue(String.class);
//                    String email = snapshot.child("email").getValue(String.class);
//                    String password = snapshot.child("password").getValue(String.class);
//                    String phone = snapshot.child("phoneNumber").getValue(String.class);
//                    String profileImageUrl = snapshot.child("userImage").getValue(String.class);
//
//                    // Log the profileImageUrl for debugging
//                    Log.d("ProfileImageUrl", "Image URL: " + profileImageUrl);
//
//                    // Set data in fields
//                    editTextName.setText(name);
//                    editTextEmail.setText(email);
//                    editTextPassword.setText(password);
//                    editTextPhone.setText(phone);
//
//                    // Load profile image if available
//                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
//                        if (profileImageUrl.startsWith("http://")) {
//                            profileImageUrl = profileImageUrl.replace("http://", "https://");
//                        }
//
//                        Glide.with(requireContext())
//                                .load(profileImageUrl)
//                                .placeholder(R.drawable.ic_image_final)
//                                .error(R.drawable.ic_image_final)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        Log.e("GlideError", "Image loading failed: " + (e != null ? e.getMessage() : "Unknown error"));
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        Log.d("GlideSuccess", "Image loaded successfully.");
//                                        return false;
//                                    }
//                                })
//                                .into(profileImage);
//                        profileImage.setVisibility(View.VISIBLE);
//                        uploadedImageUrl = profileImageUrl; // Save the image URL for full-screen view
//                    } else {
//                        Log.d("ProfileImageUrl", "No image URL found in Firebase.");
//                    }
//                } else {
//                    Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Open image picker
//    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    // Handle image selection result
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            if (selectedImageUri != null) {
//                Glide.with(requireContext())
//                        .load(selectedImageUri)
//                        .placeholder(R.drawable.ic_image_final)
//                        .error(R.drawable.ic_image_final)
//                        .into(profileImage);
//                profileImage.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    // Update user profile
//    private void updateUserProfile() {
//        String name = editTextName.getText().toString().trim();
//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//        String phone = editTextPhone.getText().toString().trim();
//
//        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
//            Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (selectedImageUri != null) {
//            uploadImageToCloudinary();
//        } else {
//            saveUserData(name, email, password, phone, null);
//        }
//    }
//
//    // Upload image to Cloudinary
//    private void uploadImageToCloudinary() {
//        new Thread(() -> {
//            try {
//                File file = new File(getRealPathFromURI(selectedImageUri));
//                if (!file.exists()) {
//                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "File not found!", Toast.LENGTH_SHORT).show());
//                    return;
//                }
//
//                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
//                uploadedImageUrl = (String) uploadResult.get("url");
//
//                requireActivity().runOnUiThread(() -> {
//                    Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//                    saveUserData(editTextName.getText().toString().trim(),
//                            editTextEmail.getText().toString().trim(),
//                            editTextPassword.getText().toString().trim(),
//                            editTextPhone.getText().toString().trim(),
//                            uploadedImageUrl);
//                });
//
//            } catch (Exception e) {
//                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//            }
//        }).start();
//    }
//
//    // Save user data to Firebase
//    private void saveUserData(String name, String email, String password, String phone, String imageUrl) {
//        databaseReference.child("name").setValue(name);
//        databaseReference.child("email").setValue(email);
//        databaseReference.child("password").setValue(password);
//        databaseReference.child("phoneNumber").setValue(phone);
//
//        if (imageUrl != null) {
//            databaseReference.child("userImage").setValue(imageUrl);
//        }
//
//        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
//    }
//
//    // Get real path from URI
//    private String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(projection[0]);
//                return cursor.getString(columnIndex);
//            }
//        }
//        return null;
//    }
//}
package com.example.agriecommerce.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.agriecommerce.R;
import com.example.agriecommerce.presentation.activity.FullScreenImageActivity;
import com.example.agriecommerce.presentation.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Map;

public class FragmentFarmerProfile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 100;
    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
    private Button btnEditProfile, btnChooseImage, approveBtn;
    private ImageView profileImage, logout;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Uri selectedImageUri;
    private Cloudinary cloudinary;
    private String uploadedImageUrl;
    private final String CLOUD_NAME = "dz36r5plo";
    private final String API_KEY = "625591387314229";
    private final String API_SECRET = "AxGPEF3FsM2Fp2NJ_WN7OTAuyIA";

    public FragmentFarmerProfile() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile, container, false);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();

        // Get userId from arguments (if passed)
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userId = bundle.getString("userId");
            if (userId != null && !userId.isEmpty()) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            } else {
                Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
            }
        } else {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            } else {
                Toast.makeText(getContext(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
                return view;
            }
        }

        // Initialize views
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        profileImage = view.findViewById(R.id.profileImage);
        logout = view.findViewById(R.id.imgLogout);
        approveBtn = view.findViewById(R.id.approveBtn);

        // Logout button click listener
        logout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Load user data
        loadUserData();

        // Choose image button click listener
        btnChooseImage.setOnClickListener(v -> selectImage());

        // Edit profile button click listener
        btnEditProfile.setOnClickListener(v -> updateUserProfile());

        // Long press listener for profile image
        profileImage.setOnLongClickListener(v -> {
            if (uploadedImageUrl != null) {
                Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
                intent.putExtra("image_url", uploadedImageUrl);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No image to display", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // Initialize Cloudinary
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));

        // Check if the logged-in user is an admin
        checkIfAdmin();

        return view;
    }

    // Check if the logged-in user is an admin
    private void checkIfAdmin() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userType = snapshot.child("userType").getValue(String.class);
                        if ("admin".equalsIgnoreCase(userType)) {
                            approveBtn.setVisibility(View.VISIBLE); // Show the approve button for admin
                            setupApproveButton(); // Set up the button based on the farmer's approval status
                        } else {
                            approveBtn.setVisibility(View.GONE); // Hide the approve button for non-admin
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to fetch user type.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Set up the approve/disapprove button based on the farmer's approval status
    private void setupApproveButton() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String approvalStatus = snapshot.child("approval").getValue(String.class);
                    if ("False".equalsIgnoreCase(approvalStatus)) {
                        approveBtn.setText("Approve");
                        approveBtn.setOnClickListener(v -> updateApprovalStatus("True"));
                    } else {
                        approveBtn.setText("Disapprove");
                        approveBtn.setOnClickListener(v -> updateApprovalStatus("False"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch approval status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update the approval status in Firebase
    private void updateApprovalStatus(String newStatus) {
        databaseReference.child("approval").setValue(newStatus)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Approval status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                        setupApproveButton(); // Refresh the button state
                    } else {
                        Toast.makeText(getContext(), "Failed to update approval status.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Load user data from Firebase
    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String phone = snapshot.child("phoneNumber").getValue(String.class);
                    String profileImageUrl = snapshot.child("userImage").getValue(String.class);

                    // Log the profileImageUrl for debugging
                    Log.d("ProfileImageUrl", "Image URL: " + profileImageUrl);

                    // Set data in fields
                    editTextName.setText(name);
                    editTextEmail.setText(email);
                    editTextPassword.setText(password);
                    editTextPhone.setText(phone);

                    // Load profile image if available
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        if (profileImageUrl.startsWith("http://")) {
                            profileImageUrl = profileImageUrl.replace("http://", "https://");
                        }

                        Glide.with(requireContext())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_image_final)
                                .error(R.drawable.ic_image_final)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("GlideError", "Image loading failed: " + (e != null ? e.getMessage() : "Unknown error"));
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.d("GlideSuccess", "Image loaded successfully.");
                                        return false;
                                    }
                                })
                                .into(profileImage);
                        profileImage.setVisibility(View.VISIBLE);
                        uploadedImageUrl = profileImageUrl; // Save the image URL for full-screen view
                    } else {
                        Log.d("ProfileImageUrl", "No image URL found in Firebase.");
                    }
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

    // Open image picker
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle image selection result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(requireContext())
                        .load(selectedImageUri)
                        .placeholder(R.drawable.ic_image_final)
                        .error(R.drawable.ic_image_final)
                        .into(profileImage);
                profileImage.setVisibility(View.VISIBLE);
            }
        }
    }

    // Update user profile
    private void updateUserProfile() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            uploadImageToCloudinary();
        } else {
            saveUserData(name, email, password, phone, null);
        }
    }

    // Upload image to Cloudinary
    private void uploadImageToCloudinary() {
        new Thread(() -> {
            try {
                File file = new File(getRealPathFromURI(selectedImageUri));
                if (!file.exists()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "File not found!", Toast.LENGTH_SHORT).show());
                    return;
                }

                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                uploadedImageUrl = (String) uploadResult.get("url");

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    saveUserData(editTextName.getText().toString().trim(),
                            editTextEmail.getText().toString().trim(),
                            editTextPassword.getText().toString().trim(),
                            editTextPhone.getText().toString().trim(),
                            uploadedImageUrl);
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Save user data to Firebase
    private void saveUserData(String name, String email, String password, String phone, String imageUrl) {
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        databaseReference.child("password").setValue(password);
        databaseReference.child("phoneNumber").setValue(phone);

        if (imageUrl != null) {
            databaseReference.child("userImage").setValue(imageUrl);
        }

        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
    }

    // Get real path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }
}