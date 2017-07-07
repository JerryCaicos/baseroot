package com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern;

import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.DoughIngredient;
import com.base.application.baseapplication.jncax.design_patterns.abstract_factory_pattern.model.SauceIngredient;

/**
 * Created by JerryCaicos on 2017/5/8.
 */

public interface IngredientFactory
{
    DoughIngredient getDoughIngredient();

    SauceIngredient getSauceIngredient();

}
