package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.FavouriteItem;
import com.example.agriecommerce.presentation.adapter.FavAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment {

    private RecyclerView favRecView;
    private FavAdapter favAdapter;
    private LottieAnimationView animationView;
    private LinearLayout emptyFavMsgLayout;
    private List<FavouriteItem> favItems;
    private DatabaseReference favRef;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        favRecView = view.findViewById(R.id.favRecView);
        animationView = view.findViewById(R.id.animationViewFavPage);
        emptyFavMsgLayout = view.findViewById(R.id.emptyBagMsgLayout);

        favItems = new ArrayList<>();
        favAdapter = new FavAdapter(getContext(), favItems, this::removeFavItemFromDatabase);

        favRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        favRecView.setAdapter(favAdapter);

        animationView.playAnimation();
        animationView.setRepeatCount(LottieDrawable.INFINITE);
        emptyFavMsgLayout.setVisibility(View.VISIBLE);

        // ✅ Get current authenticated user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String sanitizedEmail = sanitizeEmail(currentUser.getEmail());
            favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(sanitizedEmail);
            fetchFavoriteItems();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchFavoriteItems() {
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FavouriteItem favItem = dataSnapshot.getValue(FavouriteItem.class);
                    if (favItem != null) {
                        favItem.setFavId(dataSnapshot.getKey());
                        favItems.add(favItem);
                        Log.d("FavDebug", "Added to list: " + favItem.getName() + " | " + favItem.getImageUrl());
                    }
                }

                Log.d("FavDebug", "Final item count: " + favItems.size());

                updateUI();
                favAdapter = new FavAdapter(getContext(), favItems, FavFragment.this::removeFavItemFromDatabase);
                favRecView.setAdapter(favAdapter);  // Force RecyclerView to rebind

                favAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show();
                Log.e("FavError", "Firebase Database Error: " + error.getMessage());
            }
        });
    }



    private void removeFavItemFromDatabase(FavouriteItem item) {
        if (item.getFavId() != null) {
            favRef.child(item.getFavId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                        favItems.remove(item);
                        updateUI();
                        favAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.e("FavDeleteError", e.getMessage()));
        }
    }

    private void updateUI() {
        if (favItems.isEmpty()) {
            animationView.playAnimation();
            emptyFavMsgLayout.setVisibility(View.VISIBLE);
            favRecView.setVisibility(View.GONE);
        } else {
            animationView.pauseAnimation();
            emptyFavMsgLayout.setVisibility(View.GONE);
            favRecView.setVisibility(View.VISIBLE);
        }
    }

    private String sanitizeEmail(String email) {
        if (email != null) {
            return email.replace(".", "_").replace("@", "_");
        }
        return "";
    }
}
