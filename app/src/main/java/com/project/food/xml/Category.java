package com.project.food.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "category")
public class Category {

    @Attribute(name = "id")
    public int id;

    @Text
    public String name;
}
