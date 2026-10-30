package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Order;
import com.example.agriecommerce.model.OrderProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<OrderProduct> productList;
    private Order order; // ✅ Order object added

    // ✅ Updated constructor to accept Order object
    public ProductsAdapter(Context context, List<OrderProduct> productList, Order order) {
        this.context = context;
        this.productList = productList;
        this.order = order; // ✅ Initialize order
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        OrderProduct product = productList.get(position);
        Log.d("ProductsAdapter", "Product Status: " + product.getStatus());

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("Rs. " + product.getProductPrice());
        holder.quantity.setText("Quantity: " + product.getQuantity());
        holder.farmName.setText("Farm: " + product.getFarmName());

        // Set status text
        String status = product.getStatus();
        holder.status.setText(status != null && !status.isEmpty() ? status : "Status not available");

        // Load product image
        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.bn)
                .into(holder.productImage);

        // Handle status update when clicked
        holder.status.setOnClickListener(v -> {
            Log.d("ProductsAdapter", "Status before click: " + product.getStatus());

            if ("Shipped".equalsIgnoreCase(product.getStatus())) {
                if (order == null || order.getOrderDate() == null || order.getOrderTime() == null) {
                    Log.e("ProductsAdapter", "❌ Order object or order details are NULL!");
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

                // Query orders based on productName, farmName, orderDate, and orderTime
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isProductUpdated = false;

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) { // Loop through users
                            for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) { // Loop through orders
                                String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                                String orderTime = orderSnapshot.child("orderTime").getValue(String.class);

                                if (orderDate == null || orderTime == null) continue; // Skip invalid data

                                for (DataSnapshot productSnapshot : orderSnapshot.child("products").getChildren()) { // Loop through products
                                    String fetchedProductName = productSnapshot.child("productName").getValue(String.class);
                                    String fetchedFarmName = productSnapshot.child("farmName").getValue(String.class);
                                    String fetchedStatus = productSnapshot.child("status").getValue(String.class);

                                    Log.d("ProductsAdapter", "Checking Product: " + fetchedProductName +
                                            " | Farm: " + fetchedFarmName + " | Status: " + fetchedStatus +
                                            " | OrderDate: " + orderDate + " | OrderTime: " + orderTime);

                                    if (fetchedProductName != null && fetchedFarmName != null &&
                                            fetchedProductName.trim().equalsIgnoreCase(product.getProductName().trim()) &&
                                            fetchedFarmName.trim().equalsIgnoreCase(product.getFarmName().trim()) &&
                                            orderDate.equals(order.getOrderDate()) &&  // ✅ Match order date
                                            orderTime.equals(order.getOrderTime())) {  // ✅ Match order time

                                        // ✅ Update status to "Delivered"
                                        productSnapshot.getRef().child("status").setValue("Delivered")
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        holder.status.setEnabled(false);
                                                        holder.status.setText("Delivered");
                                                        holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                                        Log.d("ProductsAdapter", "✅ Status updated for product: " + fetchedProductName);
                                                    } else {
                                                        Log.e("ProductsAdapter", "❌ Failed to update product status");
                                                    }
                                                });

                                        isProductUpdated = true;
                                        return;  // ✅ Stop searching after updating the first match
                                    }
                                }
                            }
                        }

                        if (!isProductUpdated) {
                            Log.d("ProductsAdapter", "❌ No matching product found in Firebase!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ProductsAdapter", "Firebase query failed: " + error.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, quantity, farmName;
        ImageView productImage;
        Button status;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.productQuantity);
            farmName = itemView.findViewById(R.id.farmName);
            productImage = itemView.findViewById(R.id.productImage);
            status = itemView.findViewById(R.id.status);
        }
    }
}
