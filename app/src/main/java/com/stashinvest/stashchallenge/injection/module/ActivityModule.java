package com.stashinvest.stashchallenge.injection.module;

import android.app.Activity;
import android.content.Context;

import com.stashinvest.stashchallenge.injection.qualifier.ForActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ngoctranfire on 9/10/17.
 */

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ForActivity
    Context providesContext() {
        return activity;
    }
}
