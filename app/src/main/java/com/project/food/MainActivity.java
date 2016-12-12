package com.project.food;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.project.food.db.DBHelper;
import com.project.food.db.SaveToDBTask;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;
import com.project.food.fragments.CategoriesFragment;
import com.project.food.fragments.ContactsFragment;
import com.project.food.xml.Catalog;

import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback<Catalog> {

    public static final int PERMISSIONS_REQUEST_CODE = 1337;
    public static final String GET_URL = "http://ufa.farfor.ru/";

    private Dao<CategoryEntity, Integer> categoryDao;
    private Dao<OfferEntity, Integer> offerDao;
    private Dao<ParamEntity, Integer> paramDao;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_catalog);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_CODE
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MainActivity.this, "Permissions denied", Toast.LENGTH_SHORT).show();
                    onStop();
                } else {
                    try {
                        if (!initDB()) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(GET_URL)
                                    .addConverterFactory(SimpleXmlConverterFactory.create())
                                    .build();

                            APIService myAPIService = retrofit.create(APIService.class);
                            Call<Catalog> call = myAPIService.getCatalog();

                            call.enqueue(this);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            CategoriesFragment categoriesFragment = new CategoriesFragment();
                            categoriesFragment.setCategoryDao(categoryDao);
                            fragmentManager.beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.frag_container, categoriesFragment)
                                    .addToBackStack("cf")
                                    .commit();
                        }
                    } catch (SQLException sqlex) {
                        sqlex.printStackTrace();
                    }

                }
            }
        }
    }

    private boolean initDB() throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        categoryDao = dbHelper.getCategoryDao();
        offerDao = dbHelper.getOfferDao();
        paramDao = dbHelper.getParamDao();

        if (!categoryDao.queryForAll().isEmpty()) {
            Log.w("DBInit", "db success! have records");
            return true;
        } else {
            Log.w("DBInit", "db success! empty");
            return false;
        }

       /* List<OfferEntity> offerEntities = offerDao.queryForAll();
        List<ParamEntity> paramEntities = paramDao.queryForAll();
        List<CategoryEntity> categoryEntities = categoryDao.queryForAll();*/


    }

    @Override
    public void onFailure(Call<Catalog> call, Throwable t) {
        Toast.makeText(this, "Can not get response from " + call.request().toString(), Toast.LENGTH_LONG).show();
        Log.e("RESPONSE", "Can not get response from " + call.request().toString() + "\n Exception = " + t.getMessage());
    }

    @Override
    public void onResponse(Call<Catalog> call, Response<Catalog> response) {
        SaveToDBTask saveToDBTask = new SaveToDBTask(
                response.body().shop.offers.offerList,
                response.body().shop.categories.categoryList,
                categoryDao, offerDao, paramDao,
                getSupportFragmentManager(),
                progressBar);
        saveToDBTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                if (count == 1) {
                    if ("cf".equals(fragmentManager.getBackStackEntryAt(0).getName())) {
                        fragmentManager.popBackStack();
                        super.onBackPressed();
                    } else {
                        fragmentManager.popBackStack();
                        CategoriesFragment categoriesFragment = new CategoriesFragment();
                        categoriesFragment.setCategoryDao(categoryDao);
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.frag_container, categoriesFragment)
                                .addToBackStack("cf")
                                .commit();
                    }
                } else {
                    fragmentManager.popBackStack();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_catalog) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            CategoriesFragment categoriesFragment = new CategoriesFragment();
            categoriesFragment.setCategoryDao(categoryDao);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.frag_container, categoriesFragment)
                    .addToBackStack("cf")
                    .commit();
        } else if (id == R.id.nav_contacts) {
            progressBar.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            ContactsFragment contactsFragment = new ContactsFragment();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.frag_container, contactsFragment)
                    .addToBackStack("map")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
