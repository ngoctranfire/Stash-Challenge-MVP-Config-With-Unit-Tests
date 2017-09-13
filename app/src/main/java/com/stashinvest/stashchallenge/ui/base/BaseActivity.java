package com.stashinvest.stashchallenge.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.injection.component.ActivityComponent;
import com.stashinvest.stashchallenge.injection.component.ConfigPersistentComponent;
import com.stashinvest.stashchallenge.injection.component.DaggerConfigPersistentComponent;
import com.stashinvest.stashchallenge.injection.module.ActivityModule;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by ngoctranfire on 9/10/17.
 */

/**
 * Activity should be implemented by other activities as it persists the Presenter.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private int mActivityId;
    private ActivityComponent activityComponent;

    @Inject ConfigPersistentMapper configPersistentMapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.i("BaseActivity#onCreate()");
        App.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        mActivityId = savedInstanceState != null ? savedInstanceState.getInt(KEY_ACTIVITY_ID) : configPersistentMapper.generateId();

        final ConfigPersistentComponent configPersistentComponent;

        if (configPersistentMapper.get(mActivityId) != null) {

            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = configPersistentMapper.get(mActivityId);
        } else {
            Timber.i(" Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent
                    .builder()
                    .appComponent(App.getAppComponent())
                    .build();
            configPersistentMapper.put(mActivityId, configPersistentComponent);
        }

        if (configPersistentComponent != null) {
            activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
        } else {
            throw new IllegalStateException(" config persistent component should never be null!");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Timber.i("BaseActivity#onSaveInstanceState()");
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        Timber.i("BaseActivity#onDestroy()");
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentMapper.remove(mActivityId);
        }
        super.onDestroy();

    }

    public ActivityComponent activityComponent() {
        return activityComponent;
    }
}
