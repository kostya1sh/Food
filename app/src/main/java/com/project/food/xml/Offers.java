package com.project.food.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by kostya on 08.12.2016.
 */

@Root(name = "offers")
public class Offers {

    @ElementList(name = "offer", inline = true)
    public List<Offer> offerList;
}
