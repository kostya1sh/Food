package com.project.food.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.food.R;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

public class CategoryOffersAdapter extends BaseAdapter {
    private Context context;
    private List<OfferEntity> offerEntityList = new ArrayList<>();

    public CategoryOffersAdapter(Context context, List<OfferEntity> offerEntityList) {
        this.context = context;
        this.offerEntityList = offerEntityList;
    }

    @Override
    public int getCount() {
        return offerEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        if (i < 0 || i > offerEntityList.size() - 1) {
            return null;
        }
        return offerEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (i < 0 || i > offerEntityList.size() - 1) {
            return -1;
        }
        return offerEntityList.get(i).getId();
    }

    private static class ViewHolder {
        private long id;
        private ImageView imgOfferPicture;
        private TextView tvOfferName;
        private TextView tvOfferWeight;
        private TextView tvOfferPrice;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_offer_layout, viewGroup, false);
            viewHolder.id = i;
            viewHolder.imgOfferPicture = (ImageView) view.findViewById(R.id.imgOfferPicture);
            viewHolder.tvOfferName = (TextView) view.findViewById(R.id.tvOfferName);
            viewHolder.tvOfferWeight = (TextView) view.findViewById(R.id.tvOfferWeight);
            viewHolder.tvOfferPrice = (TextView) view.findViewById(R.id.tvOfferPrice);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        OfferEntity offer = offerEntityList.get(i);
        if (offer.getPicture() != null) {
            Picasso.with(context).load(offer.getPicture()).into(viewHolder.imgOfferPicture);
        }

        viewHolder.tvOfferName.setText(offer.getName());
        viewHolder.tvOfferPrice.setText(offer.getPrice() + "р.");
        String weight = "";
        for (ParamEntity p: offer.getParams()) {
            if (p.getKey().equals("Вес")) {
                weight = p.getValue();
            }
        }
        viewHolder.tvOfferWeight.setText(weight);

        return view;
    }
}
