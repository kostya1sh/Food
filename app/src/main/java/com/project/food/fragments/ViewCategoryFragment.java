package com.project.food.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

            ListView lvOffers = (ListView) rootView.findViewById(R.id.lvOffers);
            List<CategoryEntity> categoryEntityList = categoryDao.queryForEq("id", getArguments().getLong("c_id"));
            if (categoryEntityList != null && !categoryEntityList.isEmpty()) {
                List<OfferEntity> offerEntityList = new ArrayList<>(categoryEntityList.get(0).getOfferList());
                if (offerEntityList != null && !offerEntityList.isEmpty()) {
                    lvOffers.setAdapter(new CategoryOffersAdapter(getContext(), offerEntityList));
                }
            }
            lvOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OfferEntity offerEntity = (OfferEntity) adapterView.getAdapter().getItem(i);
                    onOffersItemClick(offerEntity);
                }
            });
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

        return rootView;
    }

    private void onOffersItemClick(OfferEntity offer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.offer_card_layout, null, false);
        ImageView imgCardPicture = (ImageView) view.findViewById(R.id.imgCardPicture);
        TextView tvName = (TextView) view.findViewById(R.id.tvCardName);
        TextView tvWeight = (TextView) view.findViewById(R.id.tvCardWeight);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvCardPrice);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvCardDesc);

        if (offer.getPicture() != null) {
            Picasso.with(getContext()).load(offer.getPicture()).into(imgCardPicture);
        }

        tvName.setText(" " + offer.getName());

        String weight = "";
        for (ParamEntity p: offer.getParams()) {
            if (p.getKey().equals("Вес")) {
                weight = p.getValue();
            }
        }
        tvWeight.setText(" " + weight);

        tvPrice.setText(" " + offer.getPrice());

        tvDesc.setText(" " + offer.getDescription());

        builder.setView(view).show();
    }
}
