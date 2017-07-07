package com.base.application.baseapplication.jncax.design_patterns.proxy_pattern;

/**
 * Created by chenaxing on 2017/5/10.
 */

public class TestProxy
{
    public static void main(String[] strings)
    {
        TimeProxy timeProxy = new TimeProxy();
        SalaryInterface salaryInterface = (SalaryInterface) timeProxy.bindObject(new Salary(),new ControlAdvice());
        salaryInterface.doSalary();

    }
}
