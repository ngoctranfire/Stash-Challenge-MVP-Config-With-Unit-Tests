package com.stashinvest.stashchallenge.injection.component;

import com.stashinvest.stashchallenge.injection.module.ActivityModule;
import com.stashinvest.stashchallenge.injection.scope.PerActivity;
import com.stashinvest.stashchallenge.ui.main.MainActivity;
import com.stashinvest.stashchallenge.ui.main.MainFragment;

import dagger.Subcomponent;

/**
 * Created by ngoctranfire on 9/10/17.
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);
}
