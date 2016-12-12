package com.project.food.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.food.R;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.fragments.ViewCategoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Context context;
    private FragmentManager fragmentManager;
    private List<CategoryEntity> categoryEntityList = new ArrayList<>();

    public CategoriesAdapter(Context context, FragmentManager fragmentManager, List<CategoryEntity> categoryEntityList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.categoryEntityList = categoryEntityList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        ViewHolder(View v) {
            super(v);
            tvCategory = (TextView) v.findViewById(R.id.tvCategory);
        }
    }

    @Override
    public int getItemCount() {
        return categoryEntityList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvCategory.setText(categoryEntityList.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryItemClick(categoryEntityList.get(position).getId());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_category_layout, parent, false);

        return new ViewHolder(v);
    }

    private void onCategoryItemClick(long id) {
        ViewCategoryFragment viewCategoryFragment = new ViewCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("c_id", id);
        viewCategoryFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frag_container, viewCategoryFragment)
                .addToBackStack("vcf")
                .commit();
    }
}
