package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;
import com.example.agriecommerce.presentation.activity.ProductDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ProductViewHolder> {
    private final Context context;
    private final List<Product> productList;

    public AllProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product details
        holder.productName.setText(product.getName());
        holder.productPrice.setText("₹" + product.getPrice());

        // Load image using Glide
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Ensure secure HTTPS URL
            if (!imageUrl.startsWith("https://")) {
                imageUrl = imageUrl.replace("http://", "https://");
            }

            Log.d("GlideDebug", "Loading Image URL: " + imageUrl);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.bn) // Set a placeholder image
                    .error(R.drawable.bn) // Set an error image if URL fails
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.bn); // Default image
        }

        // Open Product Details Activity on item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("imageUrl", product.getImageUrl());
            intent.putExtra("quantity", String.valueOf(product.getQuantity()));
            intent.putExtra("inStock", product.isInStock());
            intent.putExtra("farmName", product.getFarmName());
            intent.putExtra("userEmail", product.getUserEmail());
            context.startActivity(intent);
        });

        // Handle Favorite Button Click (if it exists in the layout)
        holder.favButton.setOnClickListener(view -> addToFavorites(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage, favButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName_singleProduct);
            productPrice = itemView.findViewById(R.id.productPrice_singleProduct);
            productImage = itemView.findViewById(R.id.productImage_singleProduct);
            favButton = itemView.findViewById(R.id.productAddToFav_singleProduct); // Ensure this ID exists in your layout
        }
    }

    // Example method to handle adding to favorites (modify as needed)
    private void addToFavorites(Product product) {
        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                if (userEmail != null) {
                    String sanitizedEmail = userEmail.replace(".", "_").replace("@", "_");
                    DatabaseReference userFavRef = favRef.child(sanitizedEmail);

                    // ✅ Retrieve all favorite items and manually check for duplicates
                    userFavRef.get().addOnSuccessListener(dataSnapshot -> {
                        boolean exists = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Product favProduct = snapshot.getValue(Product.class);
                            if (favProduct != null && favProduct.getName().equals(product.getName())) {
                                exists = true;
                                break;
                            }
                        }

                        if (exists) {
                            Toast.makeText(context, "Already in Favorites!", Toast.LENGTH_SHORT).show();
                        } else {
                            String favId = userFavRef.push().getKey();
                            if (favId != null) {
                                HashMap<String, Object> favItem = new HashMap<>();
                                favItem.put("favId", favId);
                                favItem.put("name", product.getName());
                                favItem.put("price", product.getPrice());
                                favItem.put("imageUrl", product.getImageUrl());
                                favItem.put("farmName", product.getFarmName());
                                favItem.put("userEmail", userEmail);

                                userFavRef.child(favId).setValue(favItem)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "Added to Favorites!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(context, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(context, "User email not found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please log in to add items to favorites", Toast.LENGTH_SHORT).show();
            }

    }
}
