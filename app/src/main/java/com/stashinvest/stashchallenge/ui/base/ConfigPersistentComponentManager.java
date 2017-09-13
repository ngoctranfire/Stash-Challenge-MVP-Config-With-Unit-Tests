package com.stashinvest.stashchallenge.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stashinvest.stashchallenge.injection.component.ConfigPersistentComponent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ngoctranfire on 9/10/17.
 */

public class ConfigPersistentComponentManager implements ConfigPersistentMapper {

    private SparseArray<ConfigPersistentComponent> map = new SparseArray<>();
    private AtomicInteger idIncrement = new AtomicInteger();

    @Override
    public int generateId() {
        return idIncrement.getAndIncrement();
    }

    @Override
    public void remove(int id) {
        map.remove(id);
    }

    @Nullable @Override
    public ConfigPersistentComponent get(int id) {
        return map.get(id, null);
    }

    @Override
    public void put(int id, @NonNull ConfigPersistentComponent configPersistentComponent) {
        if (get(id) != null) {
            throw new IllegalStateException("Cannot map to a key that has been used by a previous ConfigPersistentComponent. " +
                    "Please check to make sure that you have cleared the ConfigPersistentComponent associated with this key" +
                    ", otherwise make you have a unique and valid key generated using ConfigPersistentMap#generateId");
        }
        map.put(id, configPersistentComponent);
    }
}
