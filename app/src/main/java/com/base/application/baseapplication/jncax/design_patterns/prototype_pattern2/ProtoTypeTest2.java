package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern2;

import com.base.application.baseapplication.jncax.design_patterns.prototype_pattern.Address;
import com.base.application.baseapplication.jncax.design_patterns.prototype_pattern.ProductClone;

import java.io.CharArrayReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenaxing on 2017/5/9.
 */

public class ProtoTypeTest2
{
    public static void main(String[] strings)
    {
        Product productOne = new Product();
        productOne.setPrice(23.34);
        productOne.setRebate(0.4);
        productOne.setNum(23);

        Address2 address = new Address2();
        address.setAddress("location 1");
        productOne.setAddress(address);

        printProduct("productOne",productOne);

        Product productTwo = null;
        try
        {
            productTwo = (Product) productOne.clone();
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        printProduct("productTwo",productTwo);

        productTwo.setNum(45);

        productTwo.getAddress().setAddress("location 2");

        printProduct("productOne after productTwo setNum",productOne);
        printProduct("productTwo after productTwo setNum",productTwo);


    }

    public static void printProduct(String name, Product product)
    {
        System.out.printf(name + ":num = "
                + product.getNum()
                + ",price = "
                + product.getPrice()
                + ",rebate = "
                + product.getRebate()
                + ",address = "
                + product.getAddress().getAddress()
                + "\n");

    }
}
