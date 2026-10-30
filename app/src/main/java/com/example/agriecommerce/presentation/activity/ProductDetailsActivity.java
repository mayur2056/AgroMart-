//package com.example.agriecommerce.presentation.activity;
//
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.agriecommerce.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.HashMap;
//
//public class ProductDetailsActivity extends AppCompatActivity {
//
//    private ImageView productImage;
//    private TextView productName, productPrice, productDescription, productFarm, productStock;
//    private Button addToCartButton;
//
//    private String name, price, description, imageUrl, farmName, stockStatus;
//    private DatabaseReference cartRef;
//    private FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_details);
//
//        // Initialize Views
////        productImage = findViewById(R.id.productImage_ProductDetailsPage);
//        productImage=findViewById(R.id.imageView7);
//        productName = findViewById(R.id.productName_ProductDetailsPage);
//        productPrice = findViewById(R.id.productPrice_ProductDetailsPage);
//        productDescription = findViewById(R.id.productDes_ProductDetailsPage);
//        productFarm = findViewById(R.id.productFarm_ProductDetailsPage);
//        productStock = findViewById(R.id.productStock_ProductDetailsPage);
//        addToCartButton = findViewById(R.id.addToCart_ProductDetailsPage);
//
//        // Initialize Firebase
//        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
//            addToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bag_b, 0, 0, 0);
//        } else {
//            addToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bag_w, 0, 0, 0);
//        }
//
//        // Get product data from Intent
//        Intent intent = getIntent();
//        boolean isStockAvailable;
//        if (intent != null) {
//            name = intent.getStringExtra("name");
//            price = intent.getStringExtra("price");
//            description = intent.getStringExtra("description");
//            imageUrl = intent.getStringExtra("imageUrl");
//            farmName = intent.getStringExtra("farmName");
//
//            // Retrieve 'inStock' as a boolean
//            isStockAvailable = intent.getBooleanExtra("inStock", false);
//            stockStatus = isStockAvailable ? "In Stock" : "Out of Stock";
//
//            // Set data to views
//            productName.setText(name);
//            productPrice.setText("Price: Rs " + price);
//            productDescription.setText(description);
//            productFarm.setText("Available at: " + farmName);
//            productStock.setText("Stock: " + stockStatus);
//
//            // Load image
//            if (imageUrl != null && !imageUrl.isEmpty()) {
//                if (!imageUrl.startsWith("https://")) {
//                    imageUrl = imageUrl.replace("http://", "https://");
//                }
//                Glide.with(this)
//                        .load(imageUrl)
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.top_grey_background)
//                                .error(R.drawable.top_grey_background)
//                                .fitCenter())
//                        .into(productImage);
//            } else {
//                productImage.setImageResource(R.drawable.bn);
//            }
//
//            // Disable button if Out of Stock
//            if (!isStockAvailable) { // Fix here
//                addToCartButton.setEnabled(false);
//                addToCartButton.setText("Out of Stock");
//                addToCartButton.setBackgroundColor(getResources().getColor(R.color.primary));
//            }
//        } else {
//            isStockAvailable = false;
//        }
//
//// Handle Add to Cart button click
//        addToCartButton.setOnClickListener(view -> addToCart(isStockAvailable));
//    }
//
//    private void addToCart(boolean isStockAvailable) {
//        if (currentUser != null) {
//            String userEmail = currentUser.getEmail();
//            if (userEmail != null) {
//                // Replace '.' and '@' in email to avoid Firebase key issues
//                String sanitizedEmail = userEmail.replace(".", "_").replace("@", "_");
//
//                // Query the Cart node to check if the product already exists for this user
//                cartRef.child(sanitizedEmail).orderByChild("name").equalTo(name).get()
//                        .addOnSuccessListener(dataSnapshot -> {
//                            boolean productExists = false;
//
//                            // Check if the product already exists by iterating over children
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                String existingFarmName = snapshot.child("farmName").getValue(String.class);
//                                String existingCartId = snapshot.child("cartId").getValue(String.class);
//                                if (existingFarmName != null && existingFarmName.equals(farmName) && existingCartId != null) {
//                                    // Product exists in the cart, update quantity
//                                    productExists = true;
//                                    int existingQuantity = snapshot.child("quantity").getValue(Integer.class);
//
//                                    // Update quantity by 1
//                                    if (existingQuantity >= 0) {
//                                        int newQuantity = existingQuantity + 1;
//                                        snapshot.getRef().child("quantity").setValue(newQuantity)
//                                                .addOnSuccessListener(unused -> {
//                                                    Toast.makeText(ProductDetailsActivity.this, "Quantity updated!", Toast.LENGTH_SHORT).show();
//                                                })
//                                                .addOnFailureListener(e -> {
//                                                    Toast.makeText(ProductDetailsActivity.this, "Failed to update quantity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                });
//                                    }
//                                    break; // exit loop once the item is found and updated
//                                }
//                            }
//
//                            // If product does not exist, add a new item to the cart
//                            if (!productExists) {
//                                // Generate a unique Cart ID
//                                String cartId = cartRef.child(sanitizedEmail).push().getKey();
//
//                                if (cartId != null) {
//                                    // Create cart item data
//                                    HashMap<String, Object> cartItem = new HashMap<>();
//                                    cartItem.put("cartId", cartId);
//                                    cartItem.put("name", name);
//                                    cartItem.put("price", price);
//                                    cartItem.put("imageUrl", imageUrl);
//                                    cartItem.put("quantity", 1);
//                                    cartItem.put("userEmail", userEmail);
//                                    cartItem.put("farmName", farmName);
//                                    cartItem.put("inStock", isStockAvailable);
//
//                                    // Store data in Firebase under "Cart/{UserEmail}/{CartID}"
//                                    cartRef.child(sanitizedEmail).child(cartId).setValue(cartItem)
//                                            .addOnSuccessListener(unused -> {
//                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
//                                            })
//                                            .addOnFailureListener(e -> {
//                                                Toast.makeText(ProductDetailsActivity.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            });
//                                }
//                            }
//                        })
//                        .addOnFailureListener(e -> {
//                            Toast.makeText(ProductDetailsActivity.this, "Failed to check cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//            } else {
//                Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//}
package com.example.agriecommerce.presentation.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.agriecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productDescription, productFarm, productStock, weightTxt, totalTxt, minusBtn, plusBtn;
    private Button addToCartButton;

    private String name, price, description, imageUrl, farmName, stockStatus;
    private int quantity;
    private DatabaseReference cartRef;
    private FirebaseUser currentUser;

    private int weight = 1; // Initial weight
    private double totalPrice = 0.0; // Initial total price

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize Views
        productImage = findViewById(R.id.imageView7);
        productName = findViewById(R.id.productName_ProductDetailsPage);
        productPrice = findViewById(R.id.productPrice_ProductDetailsPage);
        productDescription = findViewById(R.id.productDes_ProductDetailsPage);
        productFarm = findViewById(R.id.productFarm_ProductDetailsPage);
        productStock = findViewById(R.id.productStock_ProductDetailsPage);
        addToCartButton = findViewById(R.id.addToCart_ProductDetailsPage);
        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        weightTxt = findViewById(R.id.weightTxt);
        totalTxt = findViewById(R.id.totalTxt);

        // Initialize Firebase
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            addToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bag_b, 0, 0, 0);
        } else {
            addToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bag_w, 0, 0, 0);
        }

        // Get product data from Intent
        Intent intent = getIntent();
        boolean isStockAvailable;
        if (intent != null) {
            name = intent.getStringExtra("name");
            price = intent.getStringExtra("price");
            description = intent.getStringExtra("description");
            imageUrl = intent.getStringExtra("imageUrl");
            farmName = intent.getStringExtra("farmName");
            String quantityString = intent.getStringExtra("quantity");
            if (quantityString != null && !quantityString.isEmpty()) {
                quantity = Integer.parseInt(quantityString);
                Log.d("ProductDetails", "Quantity from Intent: " + quantity);
            } else {
                quantity = 0;
                Log.d("ProductDetails", "Quantity not available, defaulting to 0");
            }

            // Retrieve 'inStock' as a boolean
            isStockAvailable = intent.getBooleanExtra("inStock", false);
            stockStatus = isStockAvailable ? "In Stock" : "Out of Stock";

            // Set data to views
            productName.setText(name);
            productPrice.setText("Price: Rs " + price);
            productDescription.setText(description);
            productFarm.setText("Available at: " + farmName);
            productStock.setText("Stock: " + stockStatus);

            // Load image
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (!imageUrl.startsWith("https://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }
                Glide.with(this)
                        .load(imageUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.top_grey_background)
                                .error(R.drawable.top_grey_background)
                                .fitCenter())
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.bn);
            }

            // Disable button if Out of Stock
            if (!isStockAvailable) { // Fix here
                addToCartButton.setEnabled(false);
                addToCartButton.setText("Out of Stock");
                addToCartButton.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        } else {
            isStockAvailable = false;
        }

        // Initialize weight and total price
        weightTxt.setText(weight + " kg");
        totalPrice = weight * Double.parseDouble(price);
        totalTxt.setText(String.format("%.2f", totalPrice));

        // Handle minus button click
        minusBtn.setOnClickListener(view -> {
            if (weight > 1) {
                weight--;
                weightTxt.setText(weight + " kg");
                totalPrice = weight * Double.parseDouble(price);
                totalTxt.setText(String.format("%.2f", totalPrice));
            }
        });

        // Handle plus button click
        plusBtn.setOnClickListener(view -> {
            Log.d("ProductDetails", "Weight: " + weight + ", Quantity: " + quantity);
            if (weight < quantity) {
                weight++;
                weightTxt.setText(weight + " kg");
                totalPrice = weight * Double.parseDouble(price);
                totalTxt.setText(String.format("%.2f", totalPrice));
            } else {
                Toast.makeText(this, "Cannot add more than available stock", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener(view -> addToCart(isStockAvailable));
    }

    private void addToCart(boolean isStockAvailable) {
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                // Replace '.' and '@' in email to avoid Firebase key issues
                String sanitizedEmail = userEmail.replace(".", "_").replace("@", "_");

                // Query the Cart node to check if the product already exists for this user
                cartRef.child(sanitizedEmail).orderByChild("name").equalTo(name).get()
                        .addOnSuccessListener(dataSnapshot -> {
                            boolean productExists = false;

                            // Check if the product already exists by iterating over children
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String existingFarmName = snapshot.child("farmName").getValue(String.class);
                                String existingCartId = snapshot.child("cartId").getValue(String.class);
                                if (existingFarmName != null && existingFarmName.equals(farmName) && existingCartId != null) {
                                    // Product exists in the cart, update quantity
                                    productExists = true;
                                    int existingQuantity = snapshot.child("quantity").getValue(Integer.class);

                                    // Update quantity by weight
                                    if (existingQuantity >= 0) {
                                        int newQuantity = existingQuantity + weight;
                                        snapshot.getRef().child("quantity").setValue(newQuantity)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(ProductDetailsActivity.this, "Quantity updated!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(ProductDetailsActivity.this, "Failed to update quantity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                    break; // exit loop once the item is found and updated
                                }
                            }

                            // If product does not exist, add a new item to the cart
                            if (!productExists) {
                                // Generate a unique Cart ID
                                String cartId = cartRef.child(sanitizedEmail).push().getKey();

                                if (cartId != null) {
                                    // Create cart item data
                                    HashMap<String, Object> cartItem = new HashMap<>();
                                    cartItem.put("cartId", cartId);
                                    cartItem.put("name", name);
                                    cartItem.put("price", price);
                                    cartItem.put("imageUrl", imageUrl);
                                    cartItem.put("quantity", weight);
                                    cartItem.put("userEmail", userEmail);
                                    cartItem.put("farmName", farmName);
                                    cartItem.put("inStock", isStockAvailable);

                                    // Store data in Firebase under "Cart/{UserEmail}/{CartID}"
                                    cartRef.child(sanitizedEmail).child(cartId).setValue(cartItem)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProductDetailsActivity.this, "Failed to check cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
        }
    }
}