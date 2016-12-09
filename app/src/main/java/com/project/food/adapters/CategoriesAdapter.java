package com.project.food.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.food.R;
import com.project.food.db.entities.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

public class CategoriesAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryEntity> categoryEntityList = new ArrayList<>();

    public CategoriesAdapter(Context context, List<CategoryEntity> categoryEntityList) {
        this.context = context;
        this.categoryEntityList = categoryEntityList;
    }

    @Override
    public int getCount() {
        return categoryEntityList.size();
    }

    @Nullable
    @Override
    public Object getItem(int i) {
        if (i < 0 || i > categoryEntityList.size() - 1) {
            return null;
        }

        return categoryEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (i < 0 || i > categoryEntityList.size() - 1) {
            return -1;
        }

        return categoryEntityList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_category_layout, viewGroup, false);

        TextView textView = (TextView) view.findViewById(R.id.tvCategory);
        textView.setText(categoryEntityList.get(i).getCategory());

        return view;
    }
}
