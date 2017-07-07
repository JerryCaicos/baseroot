package com.base.application.baseapplication.jncax.design_patterns.proxy_pattern;

/**
 * Created by JerryCaicos on 2017/5/10.
 */

public class ControlAdvice extends TimeAdvice
{
    @Override
    public void before()
    {
        super.before();
        System.out.printf("ensure program \n");
    }
}
