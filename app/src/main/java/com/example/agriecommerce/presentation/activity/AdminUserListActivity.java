package com.example.agriecommerce.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.User;
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.presentation.adapter.UserAdapter;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {
    private RecyclerView userRecyclerView;
    private DatabaseReference chatRef;
    private List<Users> userList;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatRef = FirebaseDatabase.getInstance().getReference("Users");

        userList = new ArrayList<>();
        adapter = new UserAdapter(userList, this);
        userRecyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Users user = data.getValue(Users.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
