package com.project.food;

import com.project.food.xml.Catalog;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kostya on 08.12.2016.
 */

public interface APIService {
    @GET("getyml/?key=ukAXxeJYZN")
    Call<Catalog> getCatalog();
}
