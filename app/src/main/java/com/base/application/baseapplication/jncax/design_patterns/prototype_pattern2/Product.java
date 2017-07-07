package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern2;


/**
 * Created by JerryCaicos on 2017/5/9.
 */

public class Product implements Cloneable
{
    private int num;

    private double price;

    private double rebate;

    private Address2 address;

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getRebate()
    {
        return rebate;
    }

    public void setRebate(double rebate)
    {
        this.rebate = rebate;
    }

    public void setAddress(Address2 addre)
    {
        this.address = addre;
    }

    public Address2 getAddress()
    {
        return address;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        Product product;
        product = (Product) super.clone();
        //有对象为属性，clone时则该类也要实现cloneable接口，并对该对象clone
        product.setAddress((Address2)address.clone());
        return product;
    }
}
