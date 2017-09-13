package com.stashinvest.stashchallenge.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.injection.qualifier.ForApplication;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.base.BaseActivity;
import com.stashinvest.stashchallenge.ui.base.BaseContract;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static android.view.View.GONE;

public class MainFragment extends Fragment implements MainContract.View {

    @Inject ViewModelAdapter adapter;
    @Inject MainPresenter mainPresenter;
    @Inject Activity activity;
    @Inject @ForApplication Context context;

    @BindView(R.id.search_phrase) EditText searchView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindDimen(R.dimen.image_space)
    int space;

    Unbinder unbinder;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("MainFragment#onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.i("MainFragment#onCreateView()");
        inject();

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        mainPresenter.attachView(this);

        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });

        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space, space, space, space));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Timber.i("MainFragment#onDestroyView()");
        mainPresenter.detachView();
        super.onDestroyView();
        unbinder.unbind();
    }


    private void search() {
        mainPresenter.searchTasks(searchView.getText().toString());
    }

    public void onImageLongPress(String id, String uri) {
        mainPresenter.getImageDetails(id, uri);
    }

    @Override @UiThread
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override @UiThread
    public void hideLoading() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void hideKeyboard() {
        searchView.clearFocus();
        InputMethodManager in = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }

    @Override @UiThread
    public void showSearchError(Throwable t) {
        Timber.e(t, "Failed to fetch image results");
        Toast.makeText(context, R.string.search_error, Toast.LENGTH_LONG).show();
    }

    @Override @UiThread
    public void showSearchImages(List<BaseViewModel> viewModelList) {
        adapter.setViewModels(viewModelList);
    }

    private void inject() {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }
}
