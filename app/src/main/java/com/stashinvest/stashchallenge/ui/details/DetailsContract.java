package com.stashinvest.stashchallenge.ui.details;

import android.support.annotation.NonNull;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.base.BaseContract;

import java.util.List;


/**
 * Created by ngoctranfire on 9/12/17.
 */

interface DetailsContract {

    interface View extends BaseContract.View {
        void showMainImage(String uri);
        void showSimilarImages(List<String> imageUris);
        void onSimilarImagesError(@NonNull String errorMessage);
        void onDisplayImageMetaDataError(@NonNull String imageMetaDataError);
        void displayImageDescription(String title, String artist);
    }

    abstract class Presenter extends BaseContract.Presenter<DetailsContract.View> {
        abstract void subscribe(@NonNull String mainImageUri, @NonNull String imageId);
        abstract void fetchImageData(@NonNull String imageId);
        abstract void fetchSimilarImages(@NonNull String imageId);
    }

}
