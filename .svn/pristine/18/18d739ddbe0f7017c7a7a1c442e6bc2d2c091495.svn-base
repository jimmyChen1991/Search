package com.hhyg.TyClosing.di.componet;

import com.hhyg.TyClosing.di.module.SearchGoodsModule;
import com.hhyg.TyClosing.di.scope.PerActivity;
import com.hhyg.TyClosing.ui.SearchGoodActivity;

import dagger.Component;

/**
 * Created by chenqiayng on 2017/6/8.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class,CommonNetParamComponent.class},modules = SearchGoodsModule.class)
public interface SearchGoodComponent {
    void inject(SearchGoodActivity aty);
}
