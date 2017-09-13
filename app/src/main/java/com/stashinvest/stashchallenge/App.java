package com.stashinvest.stashchallenge;

import android.app.Application;

import com.stashinvest.stashchallenge.injection.component.AppComponent;
import com.stashinvest.stashchallenge.injection.component.DaggerAppComponent;
import com.stashinvest.stashchallenge.injection.module.AppModule;

import timber.log.Timber;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    protected static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }


    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
