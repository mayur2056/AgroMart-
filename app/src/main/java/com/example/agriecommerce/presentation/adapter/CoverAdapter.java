package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.CoverItem;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.CoverViewHolder> {
    private final Context context;
    private final List<CoverItem> coverList;

    public CoverAdapter(Context context, List<CoverItem> coverList) {
        this.context = context;
        this.coverList = coverList;
    }

    @NonNull
    @Override
    public CoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cover_single, parent, false);
        return new CoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoverViewHolder holder, int position) {
        CoverItem coverItem = coverList.get(position);

        // Load Image using Glide
        Glide.with(context)
                .load(coverItem.getImageUrl())
                .placeholder(R.drawable.bn)
                .error(R.drawable.bn)
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return coverList.size();
    }

    public static class CoverViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;

        public CoverViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.productImage_coverPage);
        }
    }
}
