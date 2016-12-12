package com.project.food.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CategoryOffersAdapter extends RecyclerView.Adapter<CategoryOffersAdapter.ViewHolder> {
    private Context context;
    private List<OfferEntity> offerEntityList = new ArrayList<>();

    public CategoryOffersAdapter(Context context, List<OfferEntity> offerEntityList) {
        this.context = context;
        this.offerEntityList = offerEntityList;
    }

    @Override
    public long getItemId(int i) {
        if (i < 0 || i > offerEntityList.size() - 1) {
            return -1;
        }
        return offerEntityList.get(i).getId();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOfferPicture;
        TextView tvOfferName;
        TextView tvOfferWeight;
        TextView tvOfferPrice;

        ViewHolder(View v) {
            super(v);
            imgOfferPicture = (ImageView) v.findViewById(R.id.imgOfferPicture);
            tvOfferName = (TextView) v.findViewById(R.id.tvOfferName);
            tvOfferWeight = (TextView) v.findViewById(R.id.tvOfferWeight);
            tvOfferPrice = (TextView) v.findViewById((R.id.tvOfferPrice));
        }
    }

    @Override
    public int getItemCount() {
        return offerEntityList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_layout, parent, false);

        return new CategoryOffersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OfferEntity offer = offerEntityList.get(position);
        if (offer.getPicture() != null) {
            Picasso.with(context).load(offer.getPicture()).into(holder.imgOfferPicture);
        }

        holder.tvOfferName.setText(offer.getName());
        holder.tvOfferPrice.setText(offer.getPrice() + "р.");
        String weight = "";
        for (ParamEntity p : offer.getParams()) {
            if (p.getKey().equals("Вес")) {
                weight = p.getValue();
            }
        }
        holder.tvOfferWeight.setText(weight);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOffersItemClick(offer, context);
            }
        });
    }

    private void onOffersItemClick(OfferEntity offer, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.offer_card_layout, null, false);
        ImageView imgCardPicture = (ImageView) view.findViewById(R.id.imgCardPicture);
        TextView tvName = (TextView) view.findViewById(R.id.tvCardName);
        TextView tvWeight = (TextView) view.findViewById(R.id.tvCardWeight);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvCardPrice);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvCardDesc);

        if (offer.getPicture() != null) {
            Picasso.with(context).load(offer.getPicture()).into(imgCardPicture);
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
