package com.stashinvest.stashchallenge.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stashinvest.stashchallenge.injection.component.ConfigPersistentComponent;

/**
 * Created by ngoctranfire on 9/10/17.
 */

public interface ConfigPersistentMapper {
    int generateId();
    void remove(int id);
    @Nullable ConfigPersistentComponent get(int id);
    void put(int id, @NonNull ConfigPersistentComponent configPersistentComponent);
}
