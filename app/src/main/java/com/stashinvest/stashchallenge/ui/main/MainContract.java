package com.stashinvest.stashchallenge.ui.main;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.ui.base.BaseContract;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.ui.viewmodel.GettyImageViewModel;

import java.util.List;

/**
 * Created by ngoctranfire on 9/10/17.
 */


public interface MainContract {
    interface View extends BaseContract.View, GettyImageViewModel.Listener {
        void showLoading();

        void hideLoading();

        void hideKeyboard();

        void showSearchError(Throwable t);

        void showSearchImages(List<BaseViewModel> viewModelList);
    }

    abstract class Presenter implements BaseContract.Presenter<MainContract.View> {
        abstract void searchTasks(String search);
        abstract void getImageDetails(String id, String uri);
    }
}
