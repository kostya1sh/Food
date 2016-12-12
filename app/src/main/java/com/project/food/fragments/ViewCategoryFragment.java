package com.project.food.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.project.food.R;
import com.project.food.adapters.CategoryOffersAdapter;
import com.project.food.db.DBHelper;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

public class ViewCategoryFragment extends Fragment {

    private Dao<CategoryEntity, Integer> categoryDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "chosen category id = " + getArguments().getLong("c_id"), Toast.LENGTH_LONG).show();
        View rootView = inflater.inflate(R.layout.category_offers_layout, null, false);

        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(getContext(), DBHelper.class);
            categoryDao = dbHelper.getCategoryDao();

            RecyclerView rvOffers = (RecyclerView) rootView.findViewById(R.id.lvOffers);
            rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
            List<CategoryEntity> categoryEntityList = categoryDao.queryForEq("id", getArguments().getLong("c_id"));
            if (categoryEntityList != null && !categoryEntityList.isEmpty()) {
                List<OfferEntity> offerEntityList = new ArrayList<>(categoryEntityList.get(0).getOfferList());
                if (offerEntityList != null && !offerEntityList.isEmpty()) {
                    rvOffers.setAdapter(new CategoryOffersAdapter(getContext(), offerEntityList));
                }
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

        return rootView;
    }

}
