package com.stashinvest.stashchallenge.rules;

import android.support.annotation.Nullable;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ngoctranfire on 9/13/17.
 */

public class RxHooksTestRule extends TestWatcher {

    @Override
    public void starting(@Nullable Description description) {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> Schedulers.trampoline());

        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        super.starting(description);
    }

    @Override
    public void finished(@Nullable Description description) {
        super.finished(description);
    }

}
