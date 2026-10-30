package com.example.agriecommerce.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Product;
import com.example.agriecommerce.presentation.activity.AdminUserListActivity;
import com.example.agriecommerce.presentation.adapter.ProductAdminAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminHomeFragment extends Fragment implements ProductAdminAdapter.OnProductActionListener{
    private RecyclerView recyclerView;
    private ProductAdminAdapter productAdapter;
    private ArrayList<Product> productList, filteredList;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    ImageButton btnEditProduct;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

//        View view1=getLayoutInflater().inflate(R.layout.single_product_admin,null);
//        btnEditProduct=view1.findViewById(R.id.btnEditProduct);
//        btnEditProduct.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.newRecView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchView = view.findViewById(R.id.searchView);

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        productAdapter = new ProductAdminAdapter(getContext(), productList,this,false);
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

//        ImageView messageIcon = view.findViewById(R.id.messageIcon);
//        messageIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), AdminUserListActivity.class);
//            startActivity(intent);
//        });

        return view;
    }

    private void loadProductData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        ArrayList<Product> tempList = new ArrayList<>();

                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null) {
                                tempList.add(product);
                            }
                        }

                        // 🔹 लोकल साइडवर पूर्ण केस-इन्सेन्सिटिव्ह सर्च
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
