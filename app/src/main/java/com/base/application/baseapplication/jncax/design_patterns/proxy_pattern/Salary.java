package com.base.application.baseapplication.jncax.design_patterns.proxy_pattern;

/**
 * Created by chenaxing on 2017/5/10.
 */

public class Salary implements SalaryInterface
{
    @Override
    public void doSalary()
    {
        System.out.printf("do salary calculate program" + "\n");
    }
}
