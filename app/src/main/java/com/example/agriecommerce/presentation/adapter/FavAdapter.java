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
import com.example.agriecommerce.model.FavouriteItem;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

    private final Context context;
    private final List<FavouriteItem> favList;
    private final OnFavItemClickListener listener;

    public interface OnFavItemClickListener {
        void onRemoveItem(FavouriteItem item);
    }

    public FavAdapter(Context context, List<FavouriteItem> favList, OnFavItemClickListener listener) {
        this.context = context;
        this.favList = favList != null ? new ArrayList<>(favList) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item_single, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        FavouriteItem item = favList.get(position);
        if (item != null) {
            Log.d("FavAdapter", "Binding Item: " + item.getName() + " | " + item.getImageUrl());

            holder.productName.setText(item.getName() != null ? item.getName() : "Unknown");

            String secureImageUrl = item.getImageUrl();
            if (secureImageUrl != null && secureImageUrl.startsWith("http://")) {
                secureImageUrl = secureImageUrl.replace("http://", "https://");
            }

            Glide.with(context)
                    .load(secureImageUrl)
                    .placeholder(R.drawable.bn)
                    .error(R.drawable.bn)
                    .into(holder.productImage);


            holder.removeItem.setOnClickListener(v -> listener.onRemoveItem(item));
        }
    }


    @Override
    public int getItemCount() {
        return favList.size();
    }

    public void updateList(List<FavouriteItem> newFavList) {
        if (newFavList == null) return;

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FavDiffCallback(favList, newFavList));
        favList.clear();
        favList.addAll(newFavList);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage, removeItem;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.favName);
            productImage = itemView.findViewById(R.id.favImage);
            removeItem = itemView.findViewById(R.id.favremove);
        }
    }

    private static class FavDiffCallback extends DiffUtil.Callback {
        private final List<FavouriteItem> oldList;
        private final List<FavouriteItem> newList;

        public FavDiffCallback(List<FavouriteItem> oldList, List<FavouriteItem> newList) {
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
            return oldList.get(oldItemPosition).getFavId().equals(newList.get(newItemPosition).getFavId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
