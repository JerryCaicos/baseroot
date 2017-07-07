package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern;

/**
 * Created by JerryCaicos on 2017/5/9.
 */

public class ProtoTypeTest
{
    public static void main(String[] strings)
    {
        ProductClone productOne = new ProductClone();
        productOne.setPrice(23.34);
        productOne.setRebate(0.4);
        productOne.setNum(23);

        Address address = new Address();
        address.setAddress("location 1");
        productOne.setAddress(address);

        printProduct("productOne",productOne);

        ProductClone productTwo = (ProductClone) productOne.clone();

        printProduct("productTwo",productTwo);

        productTwo.setNum(45);

        productTwo.getAddress().setAddress("location 2");

        printProduct("productOne after productTwo setNum",productOne);
        printProduct("productTwo after productTwo setNum",productTwo);


    }

    public static void printProduct(String name, ProductClone productClone)
    {
        System.out.printf(name + ":num = "
                + productClone.getNum()
                + ",price = "
                + productClone.getPrice()
                + ",rebate = "
                + productClone.getRebate()
                + ",address = "
                + productClone.getAddress().getAddress()
                + "\n");
    }
}
