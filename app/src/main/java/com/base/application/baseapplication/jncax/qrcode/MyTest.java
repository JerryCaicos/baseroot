package com.base.application.baseapplication.jncax.qrcode;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by JerryCaicos on 2017/5/4.
 */

public class MyTest
{
    public static void main(String[] str)
    {
        Subscriber<String> subscriber = new Subscriber<String>()
        {
            @Override
            public void onStart()
            {
                super.onStart();
                System.out.printf("onStart");
                System.out.printf("\n");
            }

            @Override
            public void onCompleted()
            {
                System.out.printf("onCompleted");
                System.out.printf("\n");
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(String s)
            {
                System.out.printf(s);
                System.out.printf("\n");
            }
        };

        String[] words = {"Hello", "Hi", "Aloha"};
        Observable<String> observable = Observable.from(words);
        observable.subscribe(subscriber);
    }
}
