//package com.example.agriecommerce.presentation.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Users;
//import com.example.agriecommerce.presentation.activity.ChatAdminActivity;
//
//import java.util.List;
//
//public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.ViewHolder> {
//    private List<Users> userList;  // ✅ Use Users model instead of User
//    private Context context;
//
//    public FarmerAdapter(Context context, List<Users> userList) {
//        this.context = context;
//        this.userList = userList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmer, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Users user = userList.get(position);
//        holder.userName.setText(user.getName());  // ✅ Display user name properly
//
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, ChatAdminActivity.class);
//            intent.putExtra("userId", user.getUserId());
//            context.startActivity(intent);
//        });
//    }
//
//    @Override
//    public int getItemCount() { return userList.size(); }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView userName;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.tvFarmerName);
//        }
//    }
//}



//
//package com.example.agriecommerce.presentation.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Users;
//import com.example.agriecommerce.presentation.activity.ChatAdminActivity;
//
//import java.util.List;
//
//public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.ViewHolder> {
//    private List<Users> userList;  // ✅ Use Users model instead of User
//    private Context context;
//    private OnDeleteClickListener onDeleteClickListener; // Interface for delete click listener
//
//    // Interface to handle delete button clicks
//    public interface OnDeleteClickListener {
//        void onDeleteClick(Users user);
//    }
//
//    public FarmerAdapter(Context context, List<Users> userList, OnDeleteClickListener listener) {
//        this.context = context;
//        this.userList = userList;
//        this.onDeleteClickListener = listener; // Initialize the listener
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmer, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Users user = userList.get(position);
//        holder.userName.setText(user.getName());  // ✅ Display user name properly
//
//        // Set click listener for the item view (for chat functionality)
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, ChatAdminActivity.class);
//            intent.putExtra("userId", user.getUserId());
//            context.startActivity(intent);
//        });
//
//        // Set click listener for the delete button
//        holder.btnDelete.setOnClickListener(view -> {
//            if (onDeleteClickListener != null) {
//                onDeleteClickListener.onDeleteClick(user); // Pass the user to the listener
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() { return userList.size(); }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView userName;
//        ImageButton btnDelete; // Delete button
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.tvFarmerName);
//            btnDelete = itemView.findViewById(R.id.btnDelete); // Initialize delete button
//        }
//    }
//}




package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Users;
import com.example.agriecommerce.presentation.activity.ChatAdminActivity;

import java.util.List;

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.ViewHolder> {
    private List<Users> userList;  // ✅ Use Users model instead of User
    private Context context;
    private OnDeleteClickListener onDeleteClickListener; // Interface for delete click listener
    private OnEyeClickListener onEyeClickListener; // Interface for eye button click listener

    // Interface to handle delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(Users user);
    }

    // Interface to handle eye button clicks
    public interface OnEyeClickListener {
        void onEyeClick(Users user);
    }

    public FarmerAdapter(Context context, List<Users> userList, OnDeleteClickListener deleteListener, OnEyeClickListener eyeListener) {
        this.context = context;
        this.userList = userList;
        this.onDeleteClickListener = deleteListener; // Initialize the delete listener
        this.onEyeClickListener = eyeListener; // Initialize the eye listener
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.userName.setText(user.getName());  // ✅ Display user name properly

        // Set click listener for the item view (for chat functionality)
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatAdminActivity.class);
            intent.putExtra("userId", user.getUserId());
            context.startActivity(intent);
        });

        // Set click listener for the delete button
        holder.btnDelete.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(user); // Pass the user to the delete listener
            }
        });

        // Set click listener for the eye button
        holder.btnEye.setOnClickListener(view -> {
            if (onEyeClickListener != null) {
                onEyeClickListener.onEyeClick(user); // Pass the user to the eye listener
            }
        });
    }

    @Override
    public int getItemCount() { return userList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageButton btnDelete; // Delete button
        ImageButton btnEye; // Eye button

        ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tvFarmerName);
            btnDelete = itemView.findViewById(R.id.btnDelete); // Initialize delete button
            btnEye = itemView.findViewById(R.id.btnUpdate); // Initialize eye button
        }
    }
}