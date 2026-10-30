package com.example.agriecommerce.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Category;
import com.example.agriecommerce.presentation.fragment.CerealCropFragment;
import com.example.agriecommerce.presentation.fragment.FlowerFragment;
import com.example.agriecommerce.presentation.fragment.FruitsFragment;
import com.example.agriecommerce.presentation.fragment.OilSeedsFragment;
import com.example.agriecommerce.presentation.fragment.OthersFragment;
import com.example.agriecommerce.presentation.fragment.PulsesFragment;
import com.example.agriecommerce.presentation.fragment.VegetablesFragment;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<Category> categoryList;

    public CategoryAdapter(Context ctx, ArrayList<Category> categoryList) {
        this.ctx = ctx;
        this.categoryList = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_single, parent, false);
        return new ViewHolder(categoryView);
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        Category item = categoryList.get(position);
        holder.categoryName_CateSingle.setText(item.getName());

        Glide.with(ctx)
                .load(item.getImage())
                .into(holder.categoryImage_CateSingle);

        // Set click listener
        holder.itemView.setOnClickListener(view -> {


            if (item.getName().equalsIgnoreCase("Flowers")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new FlowerFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Pulses")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new PulsesFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Vegetables")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new VegetablesFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Fruits")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new FruitsFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Others")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new OthersFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Oil Seeds")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new OilSeedsFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if (item.getName().equalsIgnoreCase("Cereal Crop")) {  // Check if it's "Fruits"
                FragmentTransaction transaction = ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_fragment, new CerealCropFragment());  // Open FruitFragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView categoryImage_CateSingle;
        public TextView categoryName_CateSingle;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryImage_CateSingle = itemView.findViewById(R.id.categoryImage_CateSingle);
            categoryName_CateSingle = itemView.findViewById(R.id.categoryName_CateSingle);
        }
    }
}
