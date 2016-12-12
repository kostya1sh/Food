package com.project.food.db;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.j256.ormlite.dao.Dao;
import com.project.food.R;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;
import com.project.food.fragments.CategoriesFragment;
import com.project.food.xml.Category;
import com.project.food.xml.Offer;
import com.project.food.xml.Param;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 12.12.2016.
 */

public class SaveToDBTask extends AsyncTask<Void, Integer, Integer> {
    private List<Offer> offers;
    private List<Category> categories;
    private Dao<CategoryEntity, Integer> categoryDao;
    private Dao<OfferEntity, Integer> offerDao;
    private Dao<ParamEntity, Integer> paramDao;
    private FragmentManager fragmentManager;
    private ProgressBar progressBar;

    public SaveToDBTask(List<Offer> offers, List<Category> categories,
                        Dao<CategoryEntity, Integer> categoryDao,
                        Dao<OfferEntity, Integer> offerDao,
                        Dao<ParamEntity, Integer> paramDao,
                        FragmentManager fragmentManager,
                        ProgressBar progressBar) {
        this.offers = offers;
        this.categories = categories;
        this.categoryDao = categoryDao;
        this.offerDao = offerDao;
        this.paramDao = paramDao;
        this.fragmentManager = fragmentManager;
        this.progressBar = progressBar;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            saveCategoriesToDB(categories);
            saveOffersToDB(offers);
            Log.w("Response", "saved to db!");
            return 0;
        } catch (SQLException sqlex) {
            Log.e("Response", "error while saving to db!");
            sqlex.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer aInt) {
        if (aInt == 0) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            CategoriesFragment categoriesFragment = new CategoriesFragment();
            categoriesFragment.setCategoryDao(categoryDao);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.frag_container, categoriesFragment)
                    .addToBackStack("cf")
                    .commit();
        } else {
            progressBar.setVisibility(View.GONE);
        }
        super.onPostExecute(aInt);
    }

    /**
     * Save new offers into DB
     *
     * @param offers offers from xml
     * @throws SQLException
     */
    private void saveOffersToDB(List<Offer> offers) throws SQLException {
        for (Offer offer : offers) {
            OfferEntity offerEntity = new OfferEntity();
            offerEntity.setId(offer.id);
            offerEntity.setName(offer.name);
            offerEntity.setUrl(offer.url);
            offerEntity.setPicture(offer.picture);
            offerEntity.setPrice(offer.price);
            offerEntity.setDescription(offer.description);

            List<ParamEntity> paramEntityList = createParamEntityList(offer.params, offerEntity);
            for (ParamEntity paramEntity : paramEntityList) {
                paramEntity.setOffer(offerEntity);
                paramDao.createOrUpdate(paramEntity);
            }


            List<CategoryEntity> categoryEntities = categoryDao.queryForEq("id", offer.categoryId);
            if (categoryEntities != null && !categoryEntities.isEmpty()) {
                offerEntity.setCategory(categoryEntities.get(0));
            }

            List<OfferEntity> offerEntities = offerDao.queryForEq("id", offerEntity.getId());
            if (offerEntities.isEmpty()) {
                offerDao.createOrUpdate(offerEntity);
            }
            if (!paramEntityList.isEmpty()) {
                offerDao.update(offerEntity);
            }
        }
    }

    /**
     * Save new categories into DB
     *
     * @param categories received categories from xml
     * @throws SQLException
     */
    private void saveCategoriesToDB(List<Category> categories) throws SQLException {
        for (Category category : categories) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(category.id);
            categoryEntity.setCategory(category.name);
            List<CategoryEntity> categoryEntities = categoryDao.queryForEq("id", category.id);
            if (categoryEntities == null || categoryEntities.isEmpty()) {
                categoryDao.createOrUpdate(categoryEntity);
            }
        }
    }

    /**
     * Create param entities form received xml
     * if current param exist for current offer do not create it
     *
     * @param params      received params
     * @param offerEntity current offer
     * @return entities
     * @throws SQLException
     */
    private List<ParamEntity> createParamEntityList(List<Param> params, OfferEntity offerEntity) throws SQLException {
        if (params == null) {
            return new ArrayList<>();
        }

        List<ParamEntity> paramEntityList = new ArrayList<>();
        List<OfferEntity> offerEntities = offerDao.queryForEq("id", offerEntity.getId());
        List<ParamEntity> savedParams = new ArrayList<>();
        if (!offerEntities.isEmpty()) {
            savedParams = new ArrayList<>(offerEntities.get(0).getParams());
        }
        for (Param param : params) {
            ParamEntity paramEntity = new ParamEntity();
            paramEntity.setKey(param.name);
            paramEntity.setValue(param.value);
            if (!ParamEntity.isContain(paramEntity, savedParams)) {
                paramEntityList.add(paramEntity);
            }
        }
        return paramEntityList;
    }
}
