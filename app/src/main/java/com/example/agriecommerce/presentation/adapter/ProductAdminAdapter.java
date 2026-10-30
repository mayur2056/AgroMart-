//package com.example.agriecommerce.presentation.adapter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Product;
//
//import java.util.ArrayList;
//
//public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {
//
//    private Context ctx;
//    private ArrayList<Product> productList;
//
//    public ProductAdminAdapter(Context ctx, ArrayList<Product> productList) {
//        this.ctx = ctx;
//        this.productList = productList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_admin, parent, false);
//        return new ViewHolder(productView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Product item = productList.get(position);
//
//        // Set Product Details
//        holder.productName.setText(item.getName());
//        holder.productPrice.setText("Price:₹ " + item.getPrice());
//        holder.productDescription.setText(item.getFarmName());
//        holder.productQunatity.setText("Avaiable Stock "+item.getQuantity()+"kg");
//        Log.d("GlideDebug", "Loading Image URL: " + item.getImageUrl());
//
//        // Load Image using Glide
//        String imageUrl = item.getImageUrl();
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            if (!imageUrl.startsWith("https://")) {
//                imageUrl = imageUrl.replace("http://", "https://");
//            }
//            Glide.with(ctx)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.bn)
//                    .error(R.drawable.bn)
//                    .into(holder.productImage);
//        } else {
//            holder.productImage.setImageResource(R.drawable.bn);
//        }
//
//        // ✅ Open Product Details Activity on Image Click
////        holder.productImage.setOnClickListener(view -> {
////            Intent intent = new Intent(ctx, ProductDetailsActivity.class);
////            intent.putExtra("name", item.getName());
////            intent.putExtra("price", item.getPrice());
////            intent.putExtra("description", item.getDescription());
////            intent.putExtra("imageUrl", item.getImageUrl());
//////            intent.putExtra("rating", item.getRating());
////
////            ctx.startActivity(intent);
////        });
//
//        // ✅ Handle Favorite Button Click
////        holder.favButton.setOnClickListener(view -> addToFavorites(item));
//    }
//
//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView productImage, favButton;
//        public TextView productName, productPrice, productDescription,productQunatity;
//        public RatingBar productRating;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            // Initialize Views - Ensure these IDs match `single_product.xml`
//            productImage = itemView.findViewById(R.id.productImage_singleProduct);
////            favButton = itemView.findViewById(R.id.productAddToFav_singleProduct); // Favorite Button
//            productName = itemView.findViewById(R.id.productName_singleProduct);
//            productPrice = itemView.findViewById(R.id.productPrice_singleProduct);
//            productDescription = itemView.findViewById(R.id.productBrandName_singleProduct);
//
//            productQunatity=itemView.findViewById(R.id.productQuantity);
//        }
//    }
//
//    // ✅ Method to Add Product to Firebase Favorites
//    public void updateList(ArrayList<Product> newList) {
//        productList.clear();
//        productList.addAll(newList);
//        notifyDataSetChanged();
//    }
//}


package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;

import java.util.ArrayList;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<Product> productList;
    private OnProductActionListener listener; // Listener for update and delete actions
    private boolean showEditButton;

    // Interface to handle update and delete actions
    public interface OnProductActionListener {
        void onEditProduct(Product product);
        void onDeleteProduct(Product product);
    }

    public ProductAdminAdapter(Context ctx, ArrayList<Product> productList, OnProductActionListener listener,boolean showEditButton) {
        this.ctx = ctx;
        this.productList = productList;
        this.listener = listener; // Initialize the listener
        this.showEditButton = showEditButton;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_admin, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = productList.get(position);

        // Set the visibility of the edit button based on the flag
        if (showEditButton) {
            holder.btnEditProduct.setVisibility(View.VISIBLE);
        } else {
            holder.btnEditProduct.setVisibility(View.GONE);
        }

        // Set Product Details
        holder.productName.setText(item.getName());
        holder.productPrice.setText("Price:₹ " + item.getPrice());
        holder.productDescription.setText(item.getFarmName());
        holder.productQuantity.setText("Available Stock: " + item.getQuantity() + "kg");
        Log.d("GlideDebug", "Loading Image URL: " + item.getImageUrl());

        // Load Image using Glide
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

        // Handle Edit Button Click
        holder.btnEditProduct.setOnClickListener(view -> {
            if (listener != null) {
                listener.onEditProduct(item); // Pass the product to the listener
            }
        });

        // Handle Delete Button Click
        holder.btnDeleteProduct.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteProduct(item); // Pass the product to the listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice, productDescription, productQuantity;
        public ImageButton btnEditProduct, btnDeleteProduct; // Edit and Delete buttons

        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize Views - Ensure these IDs match `single_product_admin.xml`
            productImage = itemView.findViewById(R.id.productImage_singleProduct);
            productName = itemView.findViewById(R.id.productName_singleProduct);
            productPrice = itemView.findViewById(R.id.productPrice_singleProduct);
            productDescription = itemView.findViewById(R.id.productBrandName_singleProduct);
            productQuantity = itemView.findViewById(R.id.productQuantity);

            // Initialize Edit and Delete buttons
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }

    // Method to update the product list
    public void updateList(ArrayList<Product> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }
}