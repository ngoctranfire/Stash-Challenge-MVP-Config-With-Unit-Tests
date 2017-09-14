package com.stashinvest.stashchallenge.ui.base;

import android.support.annotation.CallSuper;

import com.stashinvest.stashchallenge.ui.main.MainContract;

import timber.log.Timber;

/**
 * Created by ngoctranfire on 9/9/17.
 */

public interface BaseContract {

    interface View {
    }

    abstract class Presenter<V extends View> {

        protected V view;

        @CallSuper
        public void attachView(V view) {
            Timber.i("Attaching view %s", view);
            this.view = view;
        }

        @CallSuper
        public void detachView() {
            Timber.i("Detaching view %s", view);
            this.view = null;
        }

        @CallSuper
        public boolean isViewAttached() {
            return this.view != null;
        }

        public void checkViewAttached() {
            if (!isViewAttached()) {
                throw new IllegalStateException("View is not attached!");
            }
        }
    }
}
