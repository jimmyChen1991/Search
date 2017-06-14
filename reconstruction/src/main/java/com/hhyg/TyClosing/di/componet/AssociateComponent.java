package com.hhyg.TyClosing.di.componet;

import com.hhyg.TyClosing.di.module.AssociateModule;
import com.hhyg.TyClosing.di.scope.PerActivity;
import com.hhyg.TyClosing.ui.SearchActivity;

import dagger.Component;

/**
 * Created by user on 2017/6/14.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class,CommonNetParamComponent.class},modules = AssociateModule.class)
public interface AssociateComponent {
    void inject(SearchActivity aty);
}
