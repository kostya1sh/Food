package com.project.food.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */
@Root(name = "categories")
public class Categories {

    @ElementList(name = "category", inline = true)
    public List<Category> categoryList;
}
