package com.example.agriecommerce.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Category;
import com.example.agriecommerce.presentation.adapter.CategoryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private ArrayList<Category> cateList;


    private CategoryAdapter categoryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        if (getActivity() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        RecyclerView categoriesRecView = view.findViewById(R.id.categoriesRecView);

        cateList = new ArrayList<>();



        setCategoryData();




        categoriesRecView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoriesRecView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(getActivity(), cateList);
        categoriesRecView.setAdapter(categoryAdapter);

        return view;
    }

    private void setCategoryData() {
        cateList.add(new Category("Fruits", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884697/fruits_catg_ddce3p.jpg"));
        cateList.add(new Category("Vegetables", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884887/vegetables_catg_issyiy.jpg"));
        cateList.add(new Category("Flowers", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884705/flowers_catg_cnfbu0.jpg"));
        cateList.add(new Category("Pulses", "https://res.cloudinary.com/dzqohamay/image/upload/v1739885406/pulses_mhpy8b.jpg"));

        cateList.add(new Category("Oil Seeds", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884698/oilseeds_catg_uieex8.jpg"));
        cateList.add(new Category("Cereal Crop", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884879/cerealcrops_catg_var3mn.jpg"));
        cateList.add(new Category("Others", "https://res.cloudinary.com/dzqohamay/image/upload/v1739884879/wheat_whole_yq8oml.jpg"));
    }

    private String getJsonData(Context context, String fileName) {
        String jsonString;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }


}
