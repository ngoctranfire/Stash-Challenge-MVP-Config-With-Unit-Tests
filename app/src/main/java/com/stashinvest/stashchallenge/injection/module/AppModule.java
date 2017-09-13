package com.stashinvest.stashchallenge.injection.module;

import android.app.Application;
import android.content.Context;

import com.stashinvest.stashchallenge.injection.qualifier.ForApplication;
import com.stashinvest.stashchallenge.ui.base.ConfigPersistentComponentManager;
import com.stashinvest.stashchallenge.ui.base.ConfigPersistentMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application application;


    public AppModule(Application application) {
        this.application = application;
    }


    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    static ConfigPersistentMapper configPersistentComponentManager() {
        return new ConfigPersistentComponentManager();
    }

}
