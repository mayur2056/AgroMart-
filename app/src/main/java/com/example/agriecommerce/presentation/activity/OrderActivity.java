//package com.example.agriecommerce.presentation.activity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.CartItem;
//import com.example.agriecommerce.model.Order;
//import com.example.agriecommerce.presentation.adapter.OrderAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderActivity extends AppCompatActivity {
//    private FirebaseAuth mAuth;
//    private FirebaseUser currentUser;
//    private DatabaseReference mDatabase;
//    private RecyclerView ordersRecyclerView;
//    private OrderAdapter orderAdapter;
//    private List<Order> orderList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_orders);  // Replace with your actual activity layout
//
//        // Initialize Firebase instances
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        // Get current user
//        currentUser = mAuth.getCurrentUser();
//
//        // Initialize RecyclerView
//        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
//        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize the list and adapter
//        orderList = new ArrayList<>();
//        orderAdapter = new OrderAdapter(orderList);
//        ordersRecyclerView.setAdapter(orderAdapter);
//
//        // Fetch orders from Firebase based on email
//        if (currentUser != null) {
//            fetchUserOrdersByEmail(currentUser.getEmail());
//        }
//    }
//
//    private void fetchUserOrdersByEmail(String email) {
//        Log.d("Firebase", "Querying for orders with email: " + email);
//
//        // Query the database for all orders
//        DatabaseReference ordersRef = mDatabase.child("Orders");
//
//        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                orderList.clear();
//                boolean foundOrders = false;
//
//                // Iterate through the orders
//                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
//                    // Get the order object
//                    Order order = orderSnapshot.getValue(Order.class);
//
//                    // Check if the order exists and the orderItems are available
//                    if (order != null && order.getItems() != null) {
//                        // Check each item in the order to see if it matches the email
//                        for (CartItem item : order.getItems()) {
//                            if (item.getUserEmail().equals(email)) {
//                                // If a matching email is found, add this order to the list
//                                orderList.add(order);
//                                foundOrders = true;
//                                break;  // Break after finding a match, no need to check further items
//                            }
//                        }
//                    }
//                }
//
//                // If orders are found, update the adapter
//                if (foundOrders) {
//                    orderAdapter.notifyDataSetChanged();
//                } else {
//                    Log.e("Firebase", "No orders found for this email.");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("Firebase", "Error fetching orders: " + databaseError.getMessage());
//            }
//        });
//    }
//
//
//}
