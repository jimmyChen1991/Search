package com.hhyg.TyClosing.di.componet;

import com.hhyg.TyClosing.di.module.CommonNetParamModule;
import com.hhyg.TyClosing.entities.CommonParam;


import dagger.Component;

/**
 * Created by user on 2017/6/9.
 */
@Component(modules = CommonNetParamModule.class)
public interface CommonNetParamComponent {
    CommonParam getCommonparam();
}
