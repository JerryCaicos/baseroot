package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern;

/**
 * Created by JerryCaicos on 2017/5/9.
 */

public abstract class Clone
{
    private int num;

    private double price;

    private double rebate;

    private Address address;

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

    public void setAddress(Address addre)
    {
        this.address = addre;
    }

    public Address getAddress()
    {
        return address;
    }

    public abstract Clone clone();
}
