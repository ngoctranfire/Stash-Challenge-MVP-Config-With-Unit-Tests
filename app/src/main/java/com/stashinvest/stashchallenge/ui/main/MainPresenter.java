package com.stashinvest.stashchallenge.ui.main;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.injection.scope.PerConfig;
import com.stashinvest.stashchallenge.util.RxUtils;


import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by ngoctranfire on 9/10/17.
 */

@PerConfig
public class MainPresenter extends MainContract.Presenter {

    @NonNull private GettyImageService gettyImageService;
    @NonNull private CompositeDisposable bin;

    @Inject
    public MainPresenter(@NonNull GettyImageService gettyImageService) {

        this.gettyImageService = gettyImageService;
        this.bin = new CompositeDisposable();
        Timber.i("MainPresenter#init()");
    }

    @Override
    public void detachView() {
        super.detachView();
        bin.dispose();
    }

    @Override
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new IllegalStateException("View is not attached!");
        }
    }

    @Override
    public void searchTasks(String search) {
        if (search.isEmpty()) {
            return;
        }

        this.view.showLoading();
        this.view.hideKeyboard();

        bin.add(gettyImageService.searchImages(search)
                .compose(RxUtils.applySchedulers())
                .subscribe(imageResponse -> {
                    checkViewAttached();
                    view.hideLoading();
                    view.showSearchImages(imageResponse);
                }, throwable -> {
                    checkViewAttached();
                    view.hideLoading();
                    view.showSearchError(throwable);
                }));
    }
}
