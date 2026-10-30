//package com.example.agriecommerce.presentation.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Users;
//import com.example.agriecommerce.presentation.adapter.FarmerAdapter;
//import com.example.agriecommerce.presentation.adapter.UserAdapter;
//import com.google.firebase.database.*;
//import java.util.ArrayList;
//import java.util.List;

//public class AdminFarmerFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private FarmerAdapter userAdapter;
//    private List<Users> userList;
//    private DatabaseReference databaseReference;
//
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_farmer_admin, container, false);
//
//        recyclerView = view.findViewById(R.id.newRecView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        userList = new ArrayList<>();
//        userAdapter = new FarmerAdapter(getContext(), userList);
//        recyclerView.setAdapter(userAdapter);  // ✅ Set adapter before fetching data
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        fetchFarmers();
//
//        return view;
//    }
//
//    private void fetchFarmers() {
//        databaseReference.orderByChild("userType").equalTo("Farmer")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        userList.clear();
//                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                            Users user = userSnapshot.getValue(Users.class);
//                            if (user != null) {
//                                userList.add(user);
//                            }
//                        }
//                        userAdapter.notifyDataSetChanged();  // ✅ Now it will not be null
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "Failed to load farmers", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//    private void fetchCustomers() {
//        databaseReference.orderByChild("userType").equalTo("Farmer")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        userList.clear();
//                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                            Users user = userSnapshot.getValue(Users.class);
//                            if (user != null) {
//                                userList.add(user);
//                            }
//                        }
//                        userAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "Failed to load customers", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void deleteUser(Users user) {
//        if (user.getUserId() == null || user.getUserId().isEmpty()) {
//            Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        databaseReference.child(user.getUserId()).removeValue()
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getContext(), "Deleted " + user.getName(), Toast.LENGTH_SHORT).show();
//                    userList.remove(user);
//                    userAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show());
//    }
//}


//
//package com.example.agriecommerce.presentation.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Users;
//import com.example.agriecommerce.presentation.adapter.FarmerAdapter;
//import com.google.firebase.database.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdminFarmerFragment extends Fragment implements FarmerAdapter.OnDeleteClickListener {
//    private RecyclerView recyclerView;
//    private FarmerAdapter userAdapter;
//    private List<Users> userList;
//    private DatabaseReference databaseReference;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_farmer_admin, container, false);
//
//        recyclerView = view.findViewById(R.id.newRecView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        userList = new ArrayList<>();
//        userAdapter = new FarmerAdapter(getContext(), userList, this); // Pass 'this' as the OnDeleteClickListener
//        recyclerView.setAdapter(userAdapter);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        fetchFarmers();
//
//        return view;
//    }
//
//    @Override
//    public void onDeleteClick(Users user) {
//        deleteUser(user); // Call the delete method
//    }
//
//    private void fetchFarmers() {
//        databaseReference.orderByChild("userType").equalTo("Farmer")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        userList.clear();
//                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                            Users user = userSnapshot.getValue(Users.class);
//                            if (user != null) {
//                                userList.add(user);
//                            }
//                        }
//                        userAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "Failed to load farmers", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void deleteUser(Users user) {
//        if (user.getUserId() == null || user.getUserId().isEmpty()) {
//            Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        databaseReference.child(user.getUserId()).removeValue()
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getContext(), "Deleted " + user.getName(), Toast.LENGTH_SHORT).show();
//                    userList.remove(user);
//                    userAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show());
//    }
//}



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
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.presentation.adapter.FarmerAdapter;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class AdminFarmerFragment extends Fragment implements FarmerAdapter.OnDeleteClickListener, FarmerAdapter.OnEyeClickListener {
    private RecyclerView recyclerView;
    private FarmerAdapter userAdapter;
    private List<Users> userList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_admin, container, false);

        recyclerView = view.findViewById(R.id.newRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userAdapter = new FarmerAdapter(getContext(), userList, this, this); // Pass 'this' as both OnDeleteClickListener and OnEyeClickListener
        recyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fetchFarmers();

        return view;
    }

    @Override
    public void onDeleteClick(Users user) {
        deleteUser(user); // Call the delete method
    }

    @Override
    public void onEyeClick(Users user) {
        // Navigate to the profile fragment based on user type
        if (user.getUserType().equals("Farmer")) {
            navigateToFarmerProfile(user.getUserId());
        } else if (user.getUserType().equals("Customer")) {
            navigateToCustomerProfile(user.getUserId());
        }
    }

//    private void navigateToFarmerProfile(String userId) {
//        FragmentFarmerProfile fragment = new FragmentFarmerProfile();
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", userId); // Pass the userId to the fragment
//        fragment.setArguments(bundle);
//
//        // Replace the current fragment with the farmer profile fragment
//        requireActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null)
//                .commit();
//    }

    private void navigateToFarmerProfile(String userId) {
        // Hide RecyclerView and show fragment container
        recyclerView.setVisibility(View.GONE);
        View fragmentContainer = requireView().findViewById(R.id.fragment_container);
        fragmentContainer.setVisibility(View.VISIBLE);

        // Replace the current fragment with the farmer profile fragment
        FragmentFarmerProfile fragment = new FragmentFarmerProfile();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId); // Pass the userId to the fragment
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToCustomerProfile(String userId) {
        AdminProfileFragment fragment = new AdminProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId); // Pass the userId to the fragment
        fragment.setArguments(bundle);

        // Replace the current fragment with the customer profile fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void fetchFarmers() {
        databaseReference.orderByChild("userType").equalTo("Farmer")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Users user = userSnapshot.getValue(Users.class);
                            if (user != null) {
                                userList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load farmers", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteUser(Users user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            Toast.makeText(getContext(), "Error: User ID missing", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(user.getUserId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Deleted " + user.getName(), Toast.LENGTH_SHORT).show();
                    userList.remove(user);
                    userAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show());
    }
}