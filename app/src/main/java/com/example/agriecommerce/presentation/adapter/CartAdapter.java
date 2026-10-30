package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context ctx;
    private final List<CartItem> cartList;
    private final OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onQuantityIncrease(CartItem item);
        void onQuantityDecrease(CartItem item);
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(Context ctx, List<CartItem> cartList, OnCartItemClickListener listener) {
        this.ctx = ctx;
        this.cartList = cartList != null ? new ArrayList<>(cartList) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cartView = LayoutInflater.from(ctx).inflate(R.layout.cart_item_single, parent, false);
        return new CartViewHolder(cartView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);

        if (cartItem != null) {
            Log.d("CartAdapter", "Binding Item: " + cartItem.getName() + " | Price: " + cartItem.getPrice()+" | Qunatity: " + cartItem.getQuantity());

            holder.cartName.setText(cartItem.getName() != null ? cartItem.getName() : "Unknown");
            holder.cartPrice.setText(String.format("Rs. %s", cartItem.getPrice() != null ? cartItem.getPrice() : "0"));
            holder.quantityTvCart.setText(cartItem.getQuantity() + " Kg");
//            holder.quantityPerKgTv.setText(String.format("Rs. %s", cartItem.getPrice() != null ? cartItem.getPrice() : "0"));
//            holder.quantityTvCart.setText(String.valueOf(cartItem.getQuantity()));

            // Load image using Glide
            if (cartItem.getImageUrl() != null && !cartItem.getImageUrl().isEmpty()) {
                Glide.with(ctx)
                        .load(cartItem.getImageUrl())
                        .placeholder(R.drawable.bn)
                        .error(R.drawable.bn)
                        .into(holder.cartImage);
            } else {
                holder.cartImage.setImageResource(R.drawable.bn);
            }

            // Handle quantity increase
//            holder.plusLayout.setOnClickListener(v -> listener.onQuantityIncrease(cartItem));
//
//            // Handle quantity decrease
//            holder.minusLayout.setOnClickListener(v -> {
//                if (cartItem.getQuantity() > 1) {
//                    listener.onQuantityDecrease(cartItem);
//                }
//            });

            // Handle item removal
            holder.cartMore.setOnClickListener(v -> listener.onRemoveItem(cartItem));
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage, cartMore;
        TextView cartName, cartPrice, quantityTvCart;
        View plusLayout, minusLayout;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImage);
            cartMore = itemView.findViewById(R.id.cartMore);
            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            quantityTvCart = itemView.findViewById(R.id.cartQuantity);
//            plusLayout = itemView.findViewById(R.id.plusLayout);
//            minusLayout = itemView.findViewById(R.id.minusLayout);
        }
    }

    // Efficient list update using DiffUtil
    public void updateList(List<CartItem> newList) {
        if (newList == null) return;

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CartDiffCallback(cartList, newList));
        cartList.clear();
        cartList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    // DiffUtil for better performance
    private static class CartDiffCallback extends DiffUtil.Callback {
        private final List<CartItem> oldList;
        private final List<CartItem> newList;

        public CartDiffCallback(List<CartItem> oldList, List<CartItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getCartId().equals(newList.get(newItemPosition).getCartId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
