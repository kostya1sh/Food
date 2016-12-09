package com.project.food.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "param", strict = false)
public class Param {

    @Attribute
    public String name;

    @Text
    public String value;
}
