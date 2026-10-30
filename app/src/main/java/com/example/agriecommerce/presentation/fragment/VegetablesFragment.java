package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;
import com.example.agriecommerce.presentation.adapter.ProductAdapter;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class VegetablesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vegetables, container, false);

        recyclerView = view.findViewById(R.id.newRecView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize product list
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Load data from Firebase
        loadProductData();

        return view;
    }

    private void loadProductData() {
        databaseReference.orderByChild("category").equalTo("Vegetables").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Clear existing list to avoid duplicates
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}