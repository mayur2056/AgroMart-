package com.example.agriecommerce.presentation.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Order;
import com.example.agriecommerce.model.OrderProduct;
import com.example.agriecommerce.presentation.adapter.OrdersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryViewDeliveredActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private DatabaseReference ordersRef;
    private FirebaseUser currentUser;
    private List<Order> ordersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_view_delivered);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ordersRecyclerView = findViewById(R.id.deliveredordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, ordersList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail().replace(".", "_").replace("@", "_");
            ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
            fetchShippedOrders();
        }
    } private void fetchShippedOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                shippedOrdersList.clear();
                for (DataSnapshot userOrders : snapshot.getChildren()) {  // Iterate through users
                    for (DataSnapshot orderSnapshot : userOrders.getChildren()) {  // Iterate through orders
                        String orderID = orderSnapshot.child("orderID").getValue(String.class);
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderTime = orderSnapshot.child("orderTime").getValue(String.class);
                        Double latitude = orderSnapshot.child("latitude").getValue(Double.class);
                        Double longitude = orderSnapshot.child("longitude").getValue(Double.class);

                        List<OrderProduct> shippedProducts = new ArrayList<>();
                        for (DataSnapshot productSnapshot : orderSnapshot.child("products").getChildren()) {
                            OrderProduct product = productSnapshot.getValue(OrderProduct.class);
                            if (product != null && "Delivered".equalsIgnoreCase(product.getStatus())) {
                                shippedProducts.add(product);
                            }
                        }

                        // Only add orders that contain shipped products
                        if (!shippedProducts.isEmpty()) {
                            Order order = new Order(orderID, orderDate, orderTime, shippedProducts,latitude,longitude);
                            ordersList.add(order);
                        }
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load shipped orders!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
