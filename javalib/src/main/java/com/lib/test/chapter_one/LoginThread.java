package com.lib.test.chapter_one;

/**
 * Created by JerryCaicos on 2017/8/17.
 */

public class LoginThread extends Thread
{
    private String name;
    private String pwd;

    public LoginThread(String name, String pwd)
    {
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public void run()
    {
        super.run();
        LoginServlet.doPost(name, pwd);
    }
}
