package com.example.agriecommerce.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//public class ProductUploadFragment extends Fragment {
//
//    private EditText productNameEt, productPriceEt, farmNameEt, productDescriptionEt, productQuantityEt;
//    private Spinner categorySpinner;
//    private Button uploadImageBtn, submitProductBtn;
//    private ImageView productImagePreview;
//    private Uri selectedImageUri;
//    private String uploadedImageUrl;
//    private FirebaseAuth auth;
//    private String userEmail;
//
//    // Cloudinary Credentials
//    private final String CLOUD_NAME = "dz36r5plo";
//    private final String API_KEY = "625591387314229";
//    private final String API_SECRET = "AxGPEF3FsM2Fp2NJ_WN7OTAuyIA";
//
//    private Cloudinary cloudinary;
//    private DatabaseReference databaseRef;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_add_product, container, false);
//
//        // Initialize Firebase Authentication
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            userEmail = user.getEmail();  // Get logged-in user's email
//        } else {
//            userEmail = "Unknown User";
//        }
//
//        // Initialize views
//        productNameEt = view.findViewById(R.id.productNameEt);
//        productPriceEt = view.findViewById(R.id.productPriceEt);
//        farmNameEt = view.findViewById(R.id.farmNameEt);
//        productDescriptionEt = view.findViewById(R.id.productDescriptionEt);
//        productQuantityEt = view.findViewById(R.id.productQuantity);
//        categorySpinner = view.findViewById(R.id.categorySpinner);
//        uploadImageBtn = view.findViewById(R.id.uploadImageBtn);
//        submitProductBtn = view.findViewById(R.id.submitProductBtn);
//        productImagePreview = view.findViewById(R.id.productImagePreview);
//
//        // Initialize Firebase Database reference
//        databaseRef = FirebaseDatabase.getInstance().getReference("products");
//
//        // Initialize Cloudinary
//        cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", CLOUD_NAME,
//                "api_key", API_KEY,
//                "api_secret", API_SECRET
//        ));
//
//        // Setup category spinner
//        String[] categories = {"Fruits", "Flowers", "Vegetables", "Pulses", "Oil Seeds", "Cereal Crop", "Others"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
//        categorySpinner.setAdapter(adapter);
//
//        // Handle image upload button click
//        uploadImageBtn.setOnClickListener(v -> selectImage());
//
//        // Handle product submission
//        submitProductBtn.setOnClickListener(v -> uploadProductData());
//
//        return view;
//    }
//
//    // Method to select an image from the gallery
//    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, 100);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            productImagePreview.setImageURI(selectedImageUri);
//            productImagePreview.setVisibility(View.VISIBLE);
//        }
//    }
//
//    // Method to upload product data to Firebase
//    private void uploadProductData() {
//        String productName = productNameEt.getText().toString().trim();
//        String productPrice = productPriceEt.getText().toString().trim();
//        String farmName = farmNameEt.getText().toString().trim();
//        String productDescription = productDescriptionEt.getText().toString().trim();
//        String category = categorySpinner.getSelectedItem().toString();
//        String productQuantityStr = productQuantityEt.getText().toString().trim();
//
//        if (productName.isEmpty() || productPrice.isEmpty() || farmName.isEmpty() || productDescription.isEmpty() || productQuantityStr.isEmpty() || selectedImageUri == null) {
//            Toast.makeText(getContext(), "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int productQuantity = Integer.parseInt(productQuantityStr);
//        boolean isStock = productQuantity > 0; // Set stock availability
//
//        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();
//
//        // Upload image to Cloudinary
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
//                    saveToFirebase(productName, productPrice, farmName, productDescription, category, productQuantity, isStock, uploadedImageUrl, userEmail);
//                });
//
//            } catch (Exception e) {
//                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//            }
//        }).start();
//    }
//
//    // Save product data to Firebase
//    private void saveToFirebase(String productName, String productPrice, String farmName, String productDescription, String category, int quantity, boolean isStock, String imageUrl, String userEmail) {
//        String id = databaseRef.push().getKey();
//
//        if (id != null) {
//            Map<String, Object> productData = new HashMap<>();
//            productData.put("id", id);
//            productData.put("name", productName);
//            productData.put("price", productPrice);
//            productData.put("farmName", farmName);
//            productData.put("description", productDescription);
//            productData.put("category", category);
//            productData.put("quantity", quantity);
//            productData.put("isStock", isStock);
//            productData.put("imageUrl", imageUrl);
//            productData.put("userEmail", userEmail);
//
//            databaseRef.child(id).setValue(productData).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(getContext(), "Product uploaded successfully!", Toast.LENGTH_SHORT).show();
//                    resetForm();
//                } else {
//                    Toast.makeText(getContext(), "Failed to upload product!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    // Reset form after successful upload
//    private void resetForm() {
//        productNameEt.setText("");
//        productPriceEt.setText("");
//        farmNameEt.setText("");
//        productDescriptionEt.setText("");
//        productQuantityEt.setText("");
//        productImagePreview.setVisibility(View.GONE);
//        selectedImageUri = null;
//    }
//
//    // Get file path from URI
//    private String getRealPathFromURI(Uri uri) {
//        String[] projection = {android.provider.MediaStore.Images.Media.DATA};
//        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(projection[0]);
//                return cursor.getString(columnIndex);
//            }
//        }
//        return null;
//    }
//}





public class ProductUploadFragment extends Fragment {

    private EditText productNameEt, productPriceEt, farmNameEt, productDescriptionEt, productQuantityEt;
    private Spinner categorySpinner;
    private Button uploadImageBtn, submitProductBtn;
    private ImageView productImagePreview;
    private Uri selectedImageUri;
    private String uploadedImageUrl;
    private FirebaseAuth auth;
    private String userEmail;

    // Cloudinary Credentials
    private final String CLOUD_NAME = "dz36r5plo";
    private final String API_KEY = "625591387314229";
    private final String API_SECRET = "AxGPEF3FsM2Fp2NJ_WN7OTAuyIA";

    private Cloudinary cloudinary;
    private DatabaseReference databaseRef;

    // Variables for edit mode
    private boolean isEditMode = false;
    private String productId;
    private Product existingProduct;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_product, container, false);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();  // Get logged-in user's email
        } else {
            userEmail = "Unknown User";
        }



        // Initialize views
        productNameEt = view.findViewById(R.id.productNameEt);
        productPriceEt = view.findViewById(R.id.productPriceEt);
        farmNameEt = view.findViewById(R.id.farmNameEt);
        productDescriptionEt = view.findViewById(R.id.productDescriptionEt);
        productQuantityEt = view.findViewById(R.id.productQuantity);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        uploadImageBtn = view.findViewById(R.id.uploadImageBtn);
        submitProductBtn = view.findViewById(R.id.submitProductBtn);
        productImagePreview = view.findViewById(R.id.productImagePreview);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("products");

        // Initialize Cloudinary
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));

        // Setup category spinner
        String[] categories = {"Fruits", "Flowers", "Vegetables", "Pulses", "Oil Seeds", "Cereal Crop", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);

        // Check if the fragment is in edit mode
        Bundle bundle = getArguments();
        if (bundle != null) {
            isEditMode = true;
            existingProduct = (Product) bundle.getSerializable("product");
            if (existingProduct != null) {
                productId = existingProduct.getId();
                populateFormWithExistingData(existingProduct);
            }
        }

        // Handle image upload button click
        uploadImageBtn.setOnClickListener(v -> selectImage());

        // Handle product submission
        submitProductBtn.setOnClickListener(v -> {
            if (isEditMode) {
                updateProductData();
            } else {
                uploadProductData();
            }
        });

        return view;
    }

    // Populate the form with existing product data
    private void populateFormWithExistingData(Product product) {
        productNameEt.setText(product.getName());
        productPriceEt.setText(product.getPrice());
        farmNameEt.setText(product.getFarmName());
        productDescriptionEt.setText(product.getDescription());
        productQuantityEt.setText(String.valueOf(product.getQuantity()));

        // Set the selected category in the spinner
        String category = product.getCategory();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpinner.getAdapter();
        int position = adapter.getPosition(category);
        if (position >= 0) {
            categorySpinner.setSelection(position);
        }

        // Load the existing image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(requireContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.bn)
                    .error(R.drawable.bn)
                    .into(productImagePreview);
            productImagePreview.setVisibility(View.VISIBLE);
        }
    }

    // Method to select an image from the gallery
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            productImagePreview.setImageURI(selectedImageUri);
            productImagePreview.setVisibility(View.VISIBLE);
        }
    }

    // Method to upload new product data to Firebase
    private void uploadProductData() {
        String productName = productNameEt.getText().toString().trim();
        String productPrice = productPriceEt.getText().toString().trim();
        String farmName = farmNameEt.getText().toString().trim();
        String productDescription = productDescriptionEt.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String productQuantityStr = productQuantityEt.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty() || farmName.isEmpty() || productDescription.isEmpty() || productQuantityStr.isEmpty() || selectedImageUri == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        int productQuantity = Integer.parseInt(productQuantityStr);
        boolean isStock = productQuantity > 0; // Set stock availability

        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();

        // Upload image to Cloudinary
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
                    saveToFirebase(productName, productPrice, farmName, productDescription, category, productQuantity, isStock, uploadedImageUrl, userEmail);
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Method to update existing product data in Firebase
    private void updateProductData() {
        String productName = productNameEt.getText().toString().trim();
        String productPrice = productPriceEt.getText().toString().trim();
        String farmName = farmNameEt.getText().toString().trim();
        String productDescription = productDescriptionEt.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String productQuantityStr = productQuantityEt.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty() || farmName.isEmpty() || productDescription.isEmpty() || productQuantityStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int productQuantity = Integer.parseInt(productQuantityStr);
        boolean isStock = productQuantity > 0; // Set stock availability

        // If a new image is selected, upload it to Cloudinary
        if (selectedImageUri != null) {
            Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();

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
                        updateInFirebase(productName, productPrice, farmName, productDescription, category, productQuantity, isStock, uploadedImageUrl);
                    });

                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            // If no new image is selected, use the existing image URL
            uploadedImageUrl = existingProduct.getImageUrl();
            updateInFirebase(productName, productPrice, farmName, productDescription, category, productQuantity, isStock, uploadedImageUrl);
        }
    }

    // Save new product data to Firebase
    private void saveToFirebase(String productName, String productPrice, String farmName, String productDescription, String category, int quantity, boolean isStock, String imageUrl, String userEmail) {
        String id = databaseRef.push().getKey();

        if (id != null) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("id", id);
            productData.put("name", productName);
            productData.put("price", productPrice);
            productData.put("farmName", farmName);
            productData.put("description", productDescription);
            productData.put("category", category);
            productData.put("quantity", quantity);
            productData.put("isStock", isStock);
            productData.put("imageUrl", imageUrl);
            productData.put("userEmail", userEmail);

            databaseRef.child(id).setValue(productData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Product uploaded successfully!", Toast.LENGTH_SHORT).show();
                    resetForm();
                } else {
                    Toast.makeText(getContext(), "Failed to upload product!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Update existing product data in Firebase
    private void updateInFirebase(String productName, String productPrice, String farmName, String productDescription, String category, int quantity, boolean isStock, String imageUrl) {
        if (productId != null) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("name", productName);
            productData.put("price", productPrice);
            productData.put("farmName", farmName);
            productData.put("description", productDescription);
            productData.put("category", category);
            productData.put("quantity", quantity);
            productData.put("isStock", isStock);
            productData.put("imageUrl", imageUrl);

            databaseRef.child(productId).updateChildren(productData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    resetForm();
                } else {
                    Toast.makeText(getContext(), "Failed to update product!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Reset form after successful upload or update
    private void resetForm() {
        productNameEt.setText("");
        productPriceEt.setText("");
        farmNameEt.setText("");
        productDescriptionEt.setText("");
        productQuantityEt.setText("");
        productImagePreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    // Get file path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {android.provider.MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }
}
