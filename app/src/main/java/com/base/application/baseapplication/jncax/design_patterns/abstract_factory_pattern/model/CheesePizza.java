package com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model;

import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.IngredientFactory;

/**
 * Created by JerryCaicos on 2017/5/8.
 */

public class CheesePizza extends Pizza
{
    public CheesePizza(IngredientFactory factory)
    {
        super(factory);
    }

    @Override
    void operationPizza()
    {
        prepare();
        bake();
        cut();
        box();
    }

    private void prepare()
    {
        System.out.printf("\n");
        System.out.printf("prepare");
        doughIngredient = ingredientFactory.getDoughIngredient();
        sauceIngredient = ingredientFactory.getSauceIngredient();
    }

    private void bake()
    {
        System.out.printf("\n");
        System.out.printf("bake");
    }

    public void cut()
    {
        System.out.printf("\n");
        System.out.printf("cut");
    }

    public void box()
    {
        System.out.printf("\n");
        System.out.printf("box");
    }

    @Override
    Pizza getPizza()
    {
        return new CheesePizza(ingredientFactory);
    }
}
