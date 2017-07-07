package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern2;

/**
 * Created by JerryCaicos on 2017/5/9.
 */

public class Address2 implements Cloneable
{
    private String address;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String addr)
    {
        address = addr;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
