package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;
import com.example.agriecommerce.presentation.adapter.ProductAdminAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FarmerHomeFragment extends Fragment implements ProductAdminAdapter.OnProductActionListener {
    private RecyclerView recyclerView;
    private ProductAdminAdapter productAdapter;
    private ArrayList<Product> productList, filteredList;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private String userEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        recyclerView = view.findViewById(R.id.newRecView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchView = view.findViewById(R.id.searchView);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
//        productAdapter = new ProductAdminAdapter(getContext(), productList,this);
        productAdapter = new ProductAdminAdapter(getContext(), productList, this,true); // Pass 'this' as the listener
        recyclerView.setAdapter(productAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        loadProductData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });

        return view;
    }

    private void loadProductData() {
        databaseReference.orderByChild("userEmail").equalTo(userEmail) .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
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



    private void filterProducts(String query) {
        if (TextUtils.isEmpty(query)) {
            loadProductData();
            return;
        }

        String lowerCaseQuery = query.toLowerCase();


        databaseReference.orderByChild("name")
                .startAt(lowerCaseQuery)
                .endAt(lowerCaseQuery + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        ArrayList<Product> tempList = new ArrayList<>();
//
//                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
//                            Product product = productSnapshot.getValue(Product.class);
//                            if (product != null) {
//                                tempList.add(product);
//                            }
//                        }


                        ArrayList<Product> filteredList = new ArrayList<>();
                        for (Product product : productList) {
                            if (product.getName().toLowerCase().contains(lowerCaseQuery)) {
                                filteredList.add(product);
                            }
                        }

                        productAdapter.updateList(filteredList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onEditProduct(Product product) {

        // Hide RecyclerView and show fragment container
        recyclerView.setVisibility(View.GONE);
        View fragmentContainer = requireView().findViewById(R.id.fragment_container);
        fragmentContainer.setVisibility(View.VISIBLE);

        // Navigate to ProductUploadFragment for editing
        ProductUploadFragment fragment = new ProductUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteProduct(Product product) {
        // Delete the product from Firebase
        databaseReference.child(product.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    loadProductData(); // Refresh the product list
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to delete product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}



