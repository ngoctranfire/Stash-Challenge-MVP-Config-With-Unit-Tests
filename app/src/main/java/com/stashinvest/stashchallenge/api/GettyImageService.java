package com.stashinvest.stashchallenge.api;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class GettyImageService {
    public static final String FIELDS = "id,title,thumb";
    public static final String SORT_ORDER = "best";

    @Inject
    GettyImagesApi api;

    @Inject
    public GettyImageService() {
    }

    public Flowable<ImageResponse> searchImages(String phrase) {
        return api.searchImages(phrase, FIELDS, SORT_ORDER);
    }


    public Flowable<MetadataResponse> getImageMetadata(String id) {
        return api.getImageMetadata(id);
    }

    public Flowable<ImageResponse> getSimilarImages(String id) {
        return api.getSimilarImages(id);
    }
}
