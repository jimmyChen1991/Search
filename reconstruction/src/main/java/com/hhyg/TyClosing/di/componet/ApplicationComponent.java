package com.hhyg.TyClosing.di.componet;

import com.hhyg.TyClosing.di.module.NetModule;
import com.hhyg.TyClosing.global.MyApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by user on 2017/6/7.
 */
@Singleton
@Component(modules = {NetModule.class})
public interface ApplicationComponent {
    void inject(MyApplication app);
    @Named("serviceApi")
    Retrofit getRetrofit();
    @Named("indexApi")
    Retrofit getRetrofit2();
}
