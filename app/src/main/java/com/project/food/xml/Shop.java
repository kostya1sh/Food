package com.project.food.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "shop", strict = false)
public class Shop {

    @Element(name = "categories", type = Categories.class)
    public Categories categories;

    @Element(name = "offers", type = Offers.class)
    public Offers offers;
}
