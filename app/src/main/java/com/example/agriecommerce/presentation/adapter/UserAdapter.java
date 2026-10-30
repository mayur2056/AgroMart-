package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.presentation.activity.ChatAdminActivity;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<Users> userList;
    private Context context;

    public UserAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userList.get(position);
        Log.d("UserAdapter", "Binding user: " + user.getName() + " (ID: " + user.getUserId() + ")");
        holder.userName.setText(user.getName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatAdminActivity.class);
            intent.putExtra("userId", user.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ✅ Data Update Method
    public void updateUserList(List<Users> newUserList) {
        userList.clear();
        userList.addAll(newUserList);
        Log.d("UserAdapter", "User list updated, new size: " + userList.size());
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;

        ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
