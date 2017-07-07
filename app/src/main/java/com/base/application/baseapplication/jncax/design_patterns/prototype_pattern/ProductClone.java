package com.base.application.baseapplication.jncax.design_patterns.prototype_pattern;

/**
 * Created by chenaxing on 2017/5/9.
 */

public class ProductClone extends Clone
{
    @Override
    public Clone clone()
    {
        ProductClone product = new ProductClone();

        product.setNum(super.getNum());
        product.setPrice(super.getPrice());
        product.setRebate(super.getRebate());

//        product.setAddress(super.getAddress());
        //java中对象传递是地址传递
        Address address = new Address();
        address.setAddress(super.getAddress().getAddress());
        product.setAddress(address);
        return product;
    }
}
