package com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model;

import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.IngredientFactory;

/**
 * Created by chenaxing on 2017/5/8.
 */

public abstract class Pizza
{
    IngredientFactory ingredientFactory;

    DoughIngredient doughIngredient;

    SauceIngredient sauceIngredient;

    public Pizza(IngredientFactory factory)
    {
        ingredientFactory = factory;
    }

    abstract void operationPizza();

    abstract Pizza getPizza();

    public Pizza doPizza()
    {
        Pizza pizza = getPizza();
        pizza.operationPizza();
        System.out.printf("\n");
        System.out.printf("enjoy pizza");

        return pizza;
    }
}
