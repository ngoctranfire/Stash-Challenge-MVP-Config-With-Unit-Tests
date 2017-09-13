package com.stashinvest.stashchallenge.ui.base;

/**
 * Created by ngoctranfire on 9/9/17.
 */

public interface BaseContract {

    interface View {
    }

    interface Presenter<V extends View> {
        void attachView(V view);
        void detachView();
        boolean isViewAttached();
        void checkViewAttached();
    }
}
