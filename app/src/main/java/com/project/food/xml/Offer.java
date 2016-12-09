package com.project.food.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "offer", strict = false)
public class Offer {

    @Attribute
    public int id;

    @Element(name = "url")
    public String url;

    @Element(name = "name")
    public String name;

    @Element(name = "price")
    public Float price;

    @Element(name = "description", required = false)
    public String description;

    @Element(name = "picture", required = false)
    public String picture;

    @Element(name = "categoryId")
    public int categoryId;

    @ElementList(inline = true, required = false)
    public List<Param> params;
}
