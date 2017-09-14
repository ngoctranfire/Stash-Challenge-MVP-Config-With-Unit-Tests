package com.stashinvest.stashchallenge.ui.main;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.rules.RxHooksTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import static org.mockito.Mockito.never;

/**
 * Created by ngoctranfire on 9/13/17.
 */

/**
 * Just as a note, I like to put business logic only in my presenters. Therefore, the presenters
 * should be the only ones in Unit Test. If you want to test UI logic, I believe in avoiding Robolectric since that is
 * another dependency (and its a pain to upgrade between new Android SDK releases).... Therefore, just test UI using an Instrumentation/Espresso test
 */
public class MainPresenterTest {

    private MainPresenter mainPresenter;
    private GettyImageService gettyImageService;
    private MainContract.View view;

    @Rule
    public RxHooksTestRule rxHooksTestRule = new RxHooksTestRule();

    @Before
    public void setup() {

        gettyImageService = Mockito.mock(GettyImageService.class);
        view = Mockito.mock(MainContract.View.class);

        mainPresenter = new MainPresenter(gettyImageService);

        // Attach View to make sure it works correctly.
        mainPresenter.attachView(view);
    }

    @After
    public void tearDown() {
        gettyImageService = null;
        mainPresenter.detachView();
        mainPresenter = null;
    }


    @Test
    public void testSearchImagesValidResults() {
        ImageResponse imageResponse = new ImageResponse();
        List<ImageResult> imageResults = new ArrayList<>();

        ImageResult image1 = new ImageResult();
        image1.setId("id1");
        image1.setTitle("title1");

        ImageResult image2 = new ImageResult();
        image2.setId("id2");
        image2.setTitle("title2");

        imageResults.add(image1);
        imageResults.add(image2);

        imageResponse.setImages(imageResults);
        String searchString = "test";

        Mockito.when(gettyImageService.searchImages(searchString))
                .thenReturn(Flowable.just(imageResponse));

        mainPresenter.searchTasks(searchString);

        InOrder inOrder = Mockito.inOrder(view, gettyImageService);
        inOrder.verify(view, Mockito.times(1)).showLoading();
        inOrder.verify(view, Mockito.times(1)).hideKeyboard();

        inOrder.verify(gettyImageService, Mockito.times(1)).searchImages(searchString);
        inOrder.verify(view, Mockito.times(1)).hideLoading();
        inOrder.verify(view, Mockito.times(1)).showSearchImages(imageResponse);
    }

    @Test
    public void testSearchEmptyString() {

        String searchString = "";
        mainPresenter.searchTasks(searchString);
        Mockito.verify(view, never()).showLoading();
        Mockito.verify(gettyImageService, never()).searchImages(searchString);
    }

    @Test
    public void testSearchError() {

        String searchString = "test";

        Throwable throwable = new Exception("Test error");

        Mockito.when(gettyImageService.searchImages(searchString))
                .thenReturn(Flowable.error(throwable));


        mainPresenter.searchTasks(searchString);

        InOrder inOrder = Mockito.inOrder(view, gettyImageService);
        inOrder.verify(view, Mockito.times(1)).showLoading();
        inOrder.verify(view, Mockito.times(1)).hideKeyboard();

        inOrder.verify(gettyImageService, Mockito.times(1)).searchImages(searchString);
        inOrder.verify(view, Mockito.times(1)).hideLoading();
        inOrder.verify(view, Mockito.times(1)).showSearchError(throwable);
    }

    @Test
    public void testDetachViewCancelsSubscription() {
        String searchString = "test";


        ImageResponse mockImageResponse = Mockito.mock(ImageResponse.class);
        Flowable<ImageResponse> imageResponseFlowable = Flowable.create(e -> {
            mainPresenter.detachView();
            e.onNext(mockImageResponse);
        }, BackpressureStrategy.DROP);

        Mockito.when(gettyImageService.searchImages(searchString))
                .thenReturn(imageResponseFlowable);

        mainPresenter.searchTasks(searchString);

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, Mockito.times(1)).showLoading();
        inOrder.verify(view, Mockito.times(1)).hideKeyboard();

        // Unsubscribed before we even got the flowable..., so none of these calls should we here anymore.
        Mockito.verify(view, never()).showSearchImages(mockImageResponse);
        Mockito.verify(view, never()).hideLoading();

    }
}
