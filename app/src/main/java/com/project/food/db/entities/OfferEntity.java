package com.project.food.db.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by kostya on 08.12.2016.
 */

@DatabaseTable(tableName = "offers")
public class OfferEntity {

    @DatabaseField(generatedId = true)
    private int genId;

    @DatabaseField(uniqueIndex = true, uniqueIndexName = "offer_id")
    private int id;

    @DatabaseField(columnName = "url")
    private String url;

    @DatabaseField(columnName = "name", dataType = DataType.STRING)
    private String name;

    @DatabaseField(columnName = "price", dataType = DataType.FLOAT)
    private float price;

    @DatabaseField(columnName = "description", dataType = DataType.STRING)
    private String description;

    @DatabaseField(columnName = "picture", dataType = DataType.STRING)
    private String picture;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private CategoryEntity category;

    @ForeignCollectionField(eager = true)
    private Collection<ParamEntity> params;

    public OfferEntity() {}

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ParamEntity> getParams() {
        return params;
    }

    public void setParams(Collection<ParamEntity> params) {
        this.params = params;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
