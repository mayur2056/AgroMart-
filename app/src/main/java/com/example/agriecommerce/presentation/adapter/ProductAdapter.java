package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<Product> productList;
    private DatabaseReference favRef;
    private FirebaseUser currentUser;
    public ProductAdapter(Context ctx, ArrayList<Product> productList) {
        this.ctx = ctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = productList.get(position);

        // **Prevent NullPointerException by checking for null views**
        if (holder.productName != null) holder.productName.setText(item.getName());
        if (holder.productPrice != null) holder.productPrice.setText("Price: " + item.getPrice());
        if (holder.productDescription != null) holder.productDescription.setText(item.getDescription());
        if (holder.productQuantity != null) holder.productQuantity.setText("Quantity: " + item.getQuantity());
        if (holder.productUserEmail != null) holder.productUserEmail.setText("Seller: " + item.getUserEmail());
        if (holder.productFarmName != null) {
            holder.productFarmName.setText(item.getFarmName() != null ? item.getFarmName() : "No Farm Name");
        }
        if (holder.productStockStatus != null) {
            boolean isStockAvailable = item.isInStock() != false ? item.isInStock() : false;
            holder.productStockStatus.setText(isStockAvailable ? "In Stock" : "Out of Stock");
        }
        Log.d("GlideDebug", "Loading Image URL: " + item.getImageUrl());

        // **Load Image using Glide**
        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (!imageUrl.startsWith("https://")) {
                imageUrl = imageUrl.replace("http://", "https://");
            }
            Glide.with(ctx)
                    .load(imageUrl)
                    .placeholder(R.drawable.bn)
                    .error(R.drawable.bn)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.bn);
        }

        // **Open Product Details Activity on Image Click**
        holder.productImage.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, ProductDetailsActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("imageUrl", item.getImageUrl());
            intent.putExtra("quantity", String.valueOf(item.getQuantity()));
            intent.putExtra("inStock", item.isInStock());
            intent.putExtra("farmName",item.getFarmName());
            intent.putExtra("userEmail", item.getUserEmail());
            ctx.startActivity(intent);
        });

        // **Handle Favorite Button Click**
        if (holder.favButton != null) {
            holder.favButton.setOnClickListener(view -> addToFavorites(item));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage, favButton;
        public TextView productName, productPrice, productDescription, productFarmName, productQuantity, productUserEmail, productStockStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            // **Ensure these IDs match `single_product.xml`**
            productImage = itemView.findViewById(R.id.productImage_singleProduct);
            favButton = itemView.findViewById(R.id.productAddToFav_singleProduct);
            productName = itemView.findViewById(R.id.productName_singleProduct);
            productPrice = itemView.findViewById(R.id.productPrice_singleProduct);
//            productDescription = itemView.findViewById(R.id.productDescription_singleProduct);
            productFarmName = itemView.findViewById(R.id.productFarmName_singleProduct);
//            productQuantity = itemView.findViewById(R.id.productQuantity_singleProduct);
//            productUserEmail = itemView.findViewById(R.id.productUserEmail_singleProduct);
//            productStockStatus = itemView.findViewById(R.id.productStockStatus_singleProduct);
        }
    }

    // **Method to Add Product to Firebase Favorites**
    private void addToFavorites(Product product) {
        favRef = FirebaseDatabase.getInstance().getReference("Favorites");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
                        Toast.makeText(ctx, "Already in Favorites!", Toast.LENGTH_SHORT).show();
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
                                    .addOnSuccessListener(aVoid -> Toast.makeText(ctx, "Added to Favorites!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(ctx, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(ctx, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(ctx, "User email not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ctx, "Please log in to add items to favorites", Toast.LENGTH_SHORT).show();
        }
    }


}
