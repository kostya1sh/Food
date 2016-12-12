package com.project.food.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;

import java.sql.SQLException;

/**
 * Created by kostya on 08.12.2016.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ormlite.db";
    private static final int DATABASE_VERSION = 16;

    private Dao<CategoryEntity, Integer> mCategoryDao = null;
    private Dao<OfferEntity, Integer> mOfferDao = null;
    private Dao<ParamEntity, Integer> mParamDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CategoryEntity.class);
            TableUtils.createTable(connectionSource, OfferEntity.class);
            TableUtils.createTable(connectionSource, ParamEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, CategoryEntity.class, true);
            TableUtils.dropTable(connectionSource, OfferEntity.class, true);
            TableUtils.dropTable(connectionSource, ParamEntity.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Category */
    public Dao<CategoryEntity, Integer> getCategoryDao() throws SQLException {
        if (mCategoryDao == null) {
            mCategoryDao = getDao(CategoryEntity.class);
        }

        return mCategoryDao;
    }

    /* Offer */
    public Dao<OfferEntity, Integer> getOfferDao() throws SQLException {
        if (mOfferDao == null) {
            mOfferDao = getDao(OfferEntity.class);
        }

        return mOfferDao;
    }

    /* Param */
    public Dao<ParamEntity, Integer> getParamDao() throws SQLException {
        if (mParamDao == null) {
            mParamDao = getDao(ParamEntity.class);
        }

        return mParamDao;
    }

    @Override
    public void close() {
        mCategoryDao = null;
        mOfferDao = null;
        mParamDao = null;
        super.close();
    }
}