package com.stashinvest.stashchallenge.ui.details;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.api.model.Metadata;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.rules.RxHooksTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;

/**
 * Created by ngoctranfire on 9/13/17.
 */

/**
 * * Just as a note, I like to put business logic only in my presenters. Therefore, the presenters
 * should be the only ones in Unit Test. If you want to test UI logic, I believe in avoiding Robolectric since that is
 * another dependency (and its a pain to upgrade between new Android SDK releases).... Therefore, just test UI using an Instrumentation/Espresso test
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsPresenterTest {

    @Rule
    public RxHooksTestRule rxHooksTestRule = new RxHooksTestRule();
    private GettyImageService gettyImageService;
    private DetailsPresenter detailsPresenter;
    private DetailsContract.View view;

    private @Captor ArgumentCaptor<ArrayList<String>> listStringCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gettyImageService = Mockito.mock(GettyImageService.class);
        view = Mockito.mock(DetailsContract.View.class);
        detailsPresenter = new DetailsPresenter(gettyImageService);

        detailsPresenter.attachView(view);
    }

    @After
    public void tearDown() {
        gettyImageService = null;
        detailsPresenter.detachView();
        detailsPresenter = null;
    }

    @Test
    public void testSubscribe() {
        String randomImageUri = "randomImageUri";
        String imageId = "imageId";

        DetailsPresenter spyDetailsPresenter = Mockito.spy(detailsPresenter);
        Mockito.doNothing().when(spyDetailsPresenter).fetchImageData(imageId);
        Mockito.doNothing().when(spyDetailsPresenter).fetchSimilarImages(imageId);

        spyDetailsPresenter.subscribe("randomImageUri", "imageId");
        Mockito.verify(view, Mockito.times(1)).showMainImage(randomImageUri);
        Mockito.verify(spyDetailsPresenter, Mockito.times(1)).fetchSimilarImages(imageId);
        Mockito.verify(spyDetailsPresenter, Mockito.times(1)).fetchImageData(imageId);
    }

    @Test
    public void testFetchImageData() {
        String imageId = "imageId";
        MetadataResponse response = new MetadataResponse();
        List<Metadata> metadataList = new ArrayList<>();
        Metadata metadata = new Metadata();
        String artist = "artist";
        String title = "title";

        metadata.setArtist(artist);
        metadata.setTitle(title);

        metadataList.add(metadata);
        response.setMetadata(metadataList);

        Mockito.when(gettyImageService.getImageMetadata(imageId)).thenReturn(Flowable.just(response));
        detailsPresenter.fetchImageData(imageId);

        InOrder inOrder = Mockito.inOrder(gettyImageService, view);
        inOrder.verify(gettyImageService, Mockito.times(1)).getImageMetadata(imageId);
        inOrder.verify(view, Mockito.times(1)).displayImageDescription(title, artist);
    }


    @Test
    public void testFetchSimilarImages() {
        ImageResponse imageResponse = new ImageResponse();
        List<ImageResult> imageResults = new ArrayList<>();

        ImageResult image1 = Mockito.mock(ImageResult.class);
        String uri1 = "uri1";
        Mockito.when(image1.getThumbUri()).thenReturn(uri1);

        ImageResult image2 = Mockito.mock(ImageResult.class);
        String uri2 = "uri2";
        Mockito.when(image2.getThumbUri()).thenReturn(uri2);

        imageResults.add(image1);
        imageResults.add(image2);

        imageResponse.setImages(imageResults);
        String imageId = "test";

        Mockito.when(gettyImageService.getSimilarImages(imageId))
                .thenReturn(Flowable.just(imageResponse));

        detailsPresenter.fetchSimilarImages(imageId);

        InOrder inOrder = Mockito.inOrder(view, gettyImageService);
        inOrder.verify(gettyImageService, Mockito.times(1)).getSimilarImages(imageId);
        inOrder.verify(view, Mockito.times(1)).showSimilarImages(listStringCaptor.capture());

        List<String> stringUris = listStringCaptor.getValue();

        assertEquals(stringUris.get(0), uri1);
        assertEquals(stringUris.get(1), uri2);
    }

    @Test
    public void testFetchSimilarError() {
        String imageId = "imageId";
        String errorMessage = "errorMessage";
        Mockito.when(gettyImageService.getSimilarImages(imageId)).thenReturn(Flowable.error(new Exception(errorMessage)));

        detailsPresenter.fetchSimilarImages(imageId);

        InOrder inOrder = Mockito.inOrder(view, gettyImageService);
        inOrder.verify(gettyImageService, Mockito.times(1)).getSimilarImages(imageId);
        inOrder.verify(view, Mockito.times(1)).onSimilarImagesError(errorMessage);
    }


    @Test
    public void testFetchImageDataError() {
        String imageId = "imageId";
        String errorMessage = "errorMessage";
        Mockito.when(gettyImageService.getImageMetadata(imageId)).thenReturn(Flowable.error(new Exception(errorMessage)));

        detailsPresenter.fetchImageData(imageId);

        InOrder inOrder = Mockito.inOrder(view, gettyImageService);
        inOrder.verify(gettyImageService, Mockito.times(1)).getImageMetadata(imageId);
        inOrder.verify(view, Mockito.times(1)).onDisplayImageMetaDataError(errorMessage);
    }

    @Test
    public void testUnsubsribeCancelsOperations() {

        String imageId = "imageId";

        Mockito.when(gettyImageService.getImageMetadata(imageId)).thenReturn(Flowable.create(new FlowableOnSubscribe<MetadataResponse>() {
            @Override
            public void subscribe(FlowableEmitter<MetadataResponse> e) throws Exception {
                detailsPresenter.detachView();
                e.onNext(Mockito.mock(MetadataResponse.class));
            }
        }, BackpressureStrategy.DROP));

        detailsPresenter.fetchImageData(imageId);
        Mockito.verify(view, never()).displayImageDescription(Mockito.any(), Mockito.any());


    }
}
