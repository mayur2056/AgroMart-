package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Order;
import com.example.agriecommerce.model.OrderProduct;
import com.example.agriecommerce.model.Product;
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

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private DatabaseReference ordersRef;
    private FirebaseUser currentUser;
    private List<Order> ordersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ordersList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(getContext(), ordersList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail().replace(".", "_").replace("@", "_");
            ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(userEmail);
            fetchOrders();
        }

        return view;
    }

    private void fetchOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderID = orderSnapshot.child("orderID").getValue(String.class);
                    String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                    String orderTime = orderSnapshot.child("orderTime").getValue(String.class);
                    Double latitude = orderSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = orderSnapshot.child("longitude").getValue(Double.class);

                    List<OrderProduct> products = new ArrayList<>();
                    for (DataSnapshot productSnapshot : orderSnapshot.child("products").getChildren()) {
                        OrderProduct product = productSnapshot.getValue(OrderProduct.class);
                        if (product != null) {
                            products.add(product);
                        }
                    }

                    Order order = new Order(orderID, orderDate, orderTime, products,latitude,longitude);
                    ordersList.add(order);
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load orders!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
