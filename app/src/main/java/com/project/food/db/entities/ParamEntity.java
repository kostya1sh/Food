package com.project.food.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

@DatabaseTable(tableName = "params")
public class ParamEntity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "key", canBeNull = false)
    private String key;

    @DatabaseField(columnName = "value", canBeNull = false)
    private String value;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private OfferEntity offer;

    public ParamEntity() {}

    public static boolean isContain(ParamEntity p, List<ParamEntity> pList) {
        if (pList == null || pList.isEmpty()) {
            return false;
        }

        for (ParamEntity paramEntity : pList) {
            if (paramEntity.getKey().equals(p.getKey())
                    && paramEntity.getValue().equals(p.getValue())) {
                return true;
            }
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
