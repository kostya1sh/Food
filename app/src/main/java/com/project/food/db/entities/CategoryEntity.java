package com.project.food.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by kostya on 08.12.2016.
 */

@DatabaseTable(tableName = "categories")
public class CategoryEntity {

    @DatabaseField(generatedId = true)
    private int genId;

    @DatabaseField(uniqueIndex = true, uniqueIndexName = "category_id")
    private int id;

    @DatabaseField(columnName = "category")
    @ForeignCollectionField(eager = true)
    private String category;

    @ForeignCollectionField(eager = true)
    private Collection<OfferEntity> offerList;

    public CategoryEntity() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<OfferEntity> getOfferList() {
        return offerList;
    }

    public void setOfferList(Collection<OfferEntity> offerList) {
        this.offerList = offerList;
    }
}
