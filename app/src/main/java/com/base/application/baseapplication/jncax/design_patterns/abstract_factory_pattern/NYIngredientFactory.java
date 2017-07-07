package com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern;

import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.DoughIngredient;
import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.SauceIngredient;

/**
 * Created by chenaxing on 2017/5/8.
 */

public class NYIngredientFactory implements IngredientFactory
{
    @Override
    public DoughIngredient getDoughIngredient()
    {
        return new DoughIngredient();
    }

    @Override
    public SauceIngredient getSauceIngredient()
    {
        return new SauceIngredient();
    }
}