package com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern;

import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.CheesePizza;
import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.Pizza;

/**
 * Created by JerryCaicos on 2017/5/8.
 */

public class PizzaStore
{
    NYIngredientFactory nyIngredientFactory = new NYIngredientFactory();

    public static void main(String[] strings)
    {
        new PizzaStore().createPizza("cheese");
    }

    public Pizza createPizza(String type)
    {
        if("cheese".equals(type))
        {
            Pizza pizza = new CheesePizza(nyIngredientFactory);
            return pizza.doPizza();
        }
        return null;
    }

}
