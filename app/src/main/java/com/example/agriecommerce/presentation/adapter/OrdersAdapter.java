package com.example.agriecommerce.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.agriecommerce.Manifest;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Order;
import com.example.agriecommerce.model.OrderProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> ordersList;

    public OrdersAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);

        // Display order date and time
        holder.orderDateTime.setText("Ordered on: " + order.getOrderDate() + " at " + order.getOrderTime());

        // Display order status
//        holder.orderStatus.setText("Status: " + (order.getStatus() != null ? order.getStatus() : "Pending"));

        // Ensure products list is not null
        List<OrderProduct> products = order.getProducts();
        if (products != null && !products.isEmpty()) {
            // ✅ Pass `order` to `ProductsAdapter`
            ProductsAdapter productsAdapter = new ProductsAdapter(context, products, order);
            holder.productsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.productsRecyclerView.setAdapter(productsAdapter);
        }

        holder.btnCMap.setOnClickListener(v -> {
            // Check permission first
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Double lat = order.getLatitude();
                Double lng = order.getLongitude();
                Log.d("latitude", String.valueOf(lat));
                Log.d("longitude", String.valueOf(lng));
                Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Google Maps not installed", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Request permission if not granted
//                ActivityCompat.requestPermissions(
//                        (Activity) context,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_LOCATION_PERMISSION
//                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTime, orderStatus;
        RecyclerView productsRecyclerView;
        Button btnCMap;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDateTime = itemView.findViewById(R.id.orderDateTime);
//            orderStatus = itemView.findViewById(R.id.orderStatus);
            productsRecyclerView = itemView.findViewById(R.id.productsRecyclerView);
            btnCMap=itemView.findViewById(R.id.btnCMap);
        }
    }
}
