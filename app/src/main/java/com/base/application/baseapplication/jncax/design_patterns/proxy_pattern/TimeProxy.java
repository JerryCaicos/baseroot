package com.base.application.baseapplication.jncax.design_patterns.proxy_pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chenaxing on 2017/5/10.
 */

public class TimeProxy implements InvocationHandler
{
    private Object target;

    private Advice advice;

    public Object bindObject(Object target,Advice advice)
    {
        this.target = target;
        this.advice = advice;
        return Proxy.newProxyInstance(this.target.getClass().getClassLoader(), this.target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object result = null;
        advice.before();
        result = method.invoke(this.target,args);
        advice.after();
        return result;
    }
}
