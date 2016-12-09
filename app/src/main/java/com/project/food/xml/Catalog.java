package com.project.food.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "yml_catalog", strict = false)
public class Catalog {

    @Element(name = "shop")
    public Shop shop;
}
