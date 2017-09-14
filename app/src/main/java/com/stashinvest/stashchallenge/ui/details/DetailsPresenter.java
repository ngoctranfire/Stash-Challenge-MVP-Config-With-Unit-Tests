package com.stashinvest.stashchallenge.ui.details;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.api.model.Metadata;
import com.stashinvest.stashchallenge.util.RxUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


/**
 * Created by ngoctranfire on 9/13/17.
 */

public class DetailsPresenter extends DetailsContract.Presenter {

    private GettyImageService gettyImageService;
    private CompositeDisposable bin = new CompositeDisposable();

    @Inject
    public DetailsPresenter(@NonNull GettyImageService gettyImageService) {
        this.gettyImageService = gettyImageService;
    }

    @Override
    public void detachView() {
        super.detachView();
        bin.dispose();
    }

    @Override
    void subscribe(@NonNull String imageUri, @NonNull String imageId) {
        view.showMainImage(imageUri);
        fetchImageData(imageId);
        fetchSimilarImages(imageId);
    }

    @Override
    void fetchImageData(@NonNull String imageId) {
        bin.add(gettyImageService.getImageMetadata(imageId)
                .compose(RxUtils.applySchedulers())
                .subscribe(metadataResponse -> {
                    checkViewAttached();
                    List<Metadata> metadataList = metadataResponse.getMetadata();
                    if (metadataList != null && metadataList.size() > 0) {
                        Metadata metadata = metadataResponse.getMetadata().get(0);
                        view.displayImageDescription(metadata.getTitle(), metadata.getArtist());
                    }
                }, throwable -> {
                    Timber.e(throwable, "Error trying to fetch image metaData");
                    view.onDisplayImageMetaDataError(throwable.getMessage());
                }));
    }

    @Override
    void fetchSimilarImages(@NonNull String imageId) {
        bin.add(gettyImageService.getSimilarImages(imageId)
        .compose(RxUtils.applySchedulers())
        .subscribe(imageResponse -> {
            checkViewAttached();
            List<ImageResult> imageResultList = imageResponse.getImages();
            List<String> imageUris = new ArrayList<>();
            int length = imageResultList.size();
            for(int i = 0; i < length; i++) {
                imageUris.add(imageResultList.get(i).getThumbUri());
            }

            view.showSimilarImages(imageUris);
        }, throwable -> {
            Timber.e(throwable, "Error trying to fetch similar images");
            view.onSimilarImagesError(throwable.getMessage());
        }));
    }
}
