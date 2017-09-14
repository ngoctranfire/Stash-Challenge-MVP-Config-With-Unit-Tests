package com.stashinvest.stashchallenge.ui.factory;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.viewmodel.GettyImageViewModel;

import javax.inject.Inject;

public class GettyImageFactory {
    @Inject
    public GettyImageFactory() {
    }

    @NonNull
    public GettyImageViewModel createGettyImageViewModel(int id, @NonNull ImageResult imageResult, @NonNull GettyImageViewModel.Listener listener) {
        return new GettyImageViewModel(id, imageResult, listener);
    }
}
