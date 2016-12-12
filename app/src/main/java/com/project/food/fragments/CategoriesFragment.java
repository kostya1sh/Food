package com.project.food.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.project.food.R;
import com.project.food.adapters.CategoriesAdapter;
import com.project.food.db.entities.CategoryEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kostya on 12.12.2016.
 */

public class CategoriesFragment extends Fragment {
    private Dao<CategoryEntity, Integer> categoryDao;

    public void setCategoryDao(Dao<CategoryEntity, Integer> categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_layout, null, false);


        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        try {
           categoryEntityList = categoryDao.queryForAll();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        RecyclerView rvCategories = (RecyclerView) rootView.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategories.setAdapter(new CategoriesAdapter(getContext(),
                getActivity().getSupportFragmentManager(), categoryEntityList));


        return rootView;
    }
}
