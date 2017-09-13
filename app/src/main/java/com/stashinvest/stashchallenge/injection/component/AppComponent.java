package com.stashinvest.stashchallenge.injection.component;

import android.content.Context;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.injection.module.AppModule;
import com.stashinvest.stashchallenge.injection.module.NetworkModule;
import com.stashinvest.stashchallenge.injection.qualifier.ForApplication;
import com.stashinvest.stashchallenge.ui.base.BaseActivity;
import com.stashinvest.stashchallenge.ui.base.ConfigPersistentMapper;
import com.stashinvest.stashchallenge.ui.details.PopUpDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(BaseActivity baseActivity);

    void inject(PopUpDialogFragment fragment);

    @ForApplication Context context();
    ConfigPersistentMapper getConfigPersistentMapper();
    GettyImageService getGettyImageService();
}