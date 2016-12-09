package com.project.food;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.project.food.adapters.CategoriesAdapter;
import com.project.food.db.DBHelper;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;
import com.project.food.fragments.ContactsFragment;
import com.project.food.fragments.ViewCategoryFragment;
import com.project.food.xml.Catalog;
import com.project.food.xml.Category;
import com.project.food.xml.Offer;
import com.project.food.xml.Param;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Загрузка...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

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
                new String[]{
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                },
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
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GET_URL)
                            .addConverterFactory(SimpleXmlConverterFactory.create())
                            .build();

                    APIService myAPIService = retrofit.create(APIService.class);
                    Call<Catalog> call = myAPIService.getCatalog();

                    call.enqueue(this);

                    try {
                        initDB();
                    } catch (SQLException sqlex) {
                        sqlex.printStackTrace();
                    }

                }
            }
        }
    }

    private void initDB() throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        categoryDao = dbHelper.getCategoryDao();
        offerDao = dbHelper.getOfferDao();
        paramDao = dbHelper.getParamDao();

       /* List<OfferEntity> offerEntities = offerDao.queryForAll();
        List<ParamEntity> paramEntities = paramDao.queryForAll();
        List<CategoryEntity> categoryEntities = categoryDao.queryForAll();*/

        Log.w("DBInit", "db success!");
    }

    @Override
    public void onFailure(Call<Catalog> call, Throwable t) {
        progressDialog.hide();

        Toast.makeText(this, "Can not get response from " + call.request().toString(), Toast.LENGTH_LONG).show();
        Log.e("RESPONSE", "Can not get response from " + call.request().toString() + "\n Exception = " + t.getMessage());
        final ListView listView = (ListView) findViewById(R.id.lvCategories);

        try {
            listView.setAdapter(new CategoriesAdapter(this, categoryDao.queryForAll()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onCategoryItemClick(adapterView.getAdapter().getItemId(i), listView);
                }
            });
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<Catalog> call, Response<Catalog> response) {
        try {
            saveCategoriesToDB(response.body().shop.categories.categoryList);
            saveOffersToDB(response.body().shop.offers.offerList);

            final ListView listView = (ListView) findViewById(R.id.lvCategories);
            listView.setAdapter(new CategoriesAdapter(this, categoryDao.queryForAll()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onCategoryItemClick(adapterView.getAdapter().getItemId(i), listView);
                }
            });

            progressDialog.hide();
            Log.w("Response", "saved to db!");
        } catch (SQLException sqlex) {
            progressDialog.hide();
            Log.e("Response", "error while saving to db!");
            sqlex.printStackTrace();
        }
    }

    private void onCategoryItemClick(long id, ListView listView) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewCategoryFragment viewCategoryFragment = new ViewCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("c_id", id);
        viewCategoryFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frag_container, viewCategoryFragment)
                .addToBackStack("vcf")
                .commit();
        listView.setVisibility(View.GONE);
    }

    /**
     * Save new offers into DB
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                fragmentManager.popBackStack();
                if (count == 1) {
                    ListView listView = (ListView) findViewById(R.id.lvCategories);
                    listView.setVisibility(View.VISIBLE);
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
        ListView listView = (ListView) findViewById(R.id.lvCategories);
        if (id == R.id.nav_catalog) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            for (int i = 0; i < count; i++) {
                fragmentManager.popBackStack();
            }
            listView.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_contacts) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ContactsFragment contactsFragment = new ContactsFragment();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.frag_container, contactsFragment)
                    .addToBackStack("map")
                    .commit();
            listView.setVisibility(View.GONE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
