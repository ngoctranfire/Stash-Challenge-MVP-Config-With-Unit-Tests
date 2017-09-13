package com.stashinvest.stashchallenge.injection.component;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.injection.module.ActivityModule;
import com.stashinvest.stashchallenge.injection.scope.PerConfig;

import dagger.Component;

/**
 * Created by ngoctranfire on 9/10/17.
 */
@PerConfig
@Component(dependencies = {AppComponent.class})
public interface ConfigPersistentComponent {
    @NonNull ActivityComponent activityComponent(@NonNull ActivityModule activityModule);
}
