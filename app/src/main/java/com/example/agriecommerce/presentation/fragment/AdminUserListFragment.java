package com.example.agriecommerce.presentation.fragment;

import android.os.Bundle;
import android.util.Log; // Debugging साठी
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.presentation.adapter.UserAdapter;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListFragment extends Fragment {
    private RecyclerView userRecyclerView;
    private DatabaseReference chatRef;
    private List<Users> userList;
    private UserAdapter adapter;
    private ValueEventListener valueEventListener;
    private static final String TAG = "AdminUserListFragment"; // Logcat tag

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_user_list, container, false);

        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatRef = FirebaseDatabase.getInstance().getReference("Users");

        userList = new ArrayList<>();
        adapter = new UserAdapter(userList, getContext());
        userRecyclerView.setAdapter(adapter);

        loadUsers();

        return view;
    }

    private void loadUsers() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Users> tempUserList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Users user = data.getValue(Users.class);
                    if (user != null) {
                        Log.d("AdminUserListFragment", "User Loaded: " + user.getName());
                        tempUserList.add(user);
                    }
                }

                if (!tempUserList.isEmpty()) {
                    if (adapter == null) {
                        adapter = new UserAdapter(tempUserList, getContext());
                        userRecyclerView.setAdapter(adapter);
                        Log.d("AdminUserListFragment", "Adapter set with " + tempUserList.size() + " users");
                    } else {
                        adapter.updateUserList(tempUserList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminUserListFragment", "Database Error: " + error.getMessage());
            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (chatRef != null && valueEventListener != null) {
            chatRef.removeEventListener(valueEventListener);
        }
    }
}
