package com.stashinvest.stashchallenge.ui.main;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.injection.scope.PerConfig;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ngoctranfire on 9/10/17.
 */

@PerConfig
public class MainPresenter extends MainContract.Presenter {

    @NonNull private GettyImageService gettyImageService;
    @NonNull private CompositeDisposable bin;
    @NonNull private GettyImageFactory gettyImageFactory;

    private MainContract.View view;

    @Inject
    public MainPresenter(@NonNull GettyImageService gettyImageService,
                         @NonNull GettyImageFactory gettyImageFactory) {

        this.gettyImageService = gettyImageService;
        this.bin= new CompositeDisposable();
        this.gettyImageFactory = gettyImageFactory;
        Timber.i("MainPresenter#init()");
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        bin.dispose();
    }

    @Override
    public boolean isViewAttached() {
        return this.view != null;
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
            this.view.hideKeyboard();
            return;
        }

        this.view.showLoading();
        this.view.hideKeyboard();

        gettyImageService.searchImages(search)
                .subscribeOn(Schedulers.io())
                .map(imageResponse -> {
                    List<ImageResult> images = imageResponse.getImages();
                    List<BaseViewModel> viewModels = new ArrayList<>();
                    int i = 0;
                    for (ImageResult imageResult : images) {
                        viewModels.add(gettyImageFactory.createGettyImageViewModel(i++, imageResult, view));
                    }
                    return viewModels;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageResponse -> {
                    view.hideLoading();
                    view.showSearchImages(imageResponse);
                }, throwable -> {
                    view.hideLoading();
                    view.showSearchError(throwable);
                });
    }

    @Override
    void getImageDetails(String id, String uri) {

    }
}
