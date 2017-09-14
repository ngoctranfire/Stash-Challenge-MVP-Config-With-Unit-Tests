package com.stashinvest.stashchallenge.ui.details;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.injection.qualifier.ForApplication;
import com.stashinvest.stashchallenge.util.DrawableUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class PopUpDialogFragment extends DialogFragment implements DetailsContract.View {
    private static final String ID_KEY = "id";
    private static final String IMAGE_URI = "image_uri";

    Unbinder unbinder;

    private String imageId;
    private String imageUri;
    @Inject DetailsPresenter detailsPresenter;
    @Inject @ForApplication Context context;

    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.title_view) TextView titleView;
    @BindViews({R.id.similar_image_view1, R.id.similar_image_view2, R.id.similar_image_view3})
    List<ImageView> similarImagesList;

    @Inject GettyImageService gettyImageService;

    public static PopUpDialogFragment newInstance(String id, String imageUri) {
        PopUpDialogFragment popUpDialogFragment = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        args.putString(IMAGE_URI, imageUri);

        popUpDialogFragment.setArguments(args);
        return popUpDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Popup should be instantiated with correct parameters");
        }

        imageId = args.getString(ID_KEY);
        imageUri = args.getString(IMAGE_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.i("PopupDialogFragment#onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dialog_popup, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        unbinder = ButterKnife.bind(this, view);

        detailsPresenter.attachView(this);
        detailsPresenter.subscribe(imageUri, imageId);
        return view;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        detailsPresenter.detachView();
    }

    @Override
    public void showMainImage(String uri) {
        Glide.with(this)
                .load(imageUri)
                .crossFade()
                .placeholder(DrawableUtils.createColorDrawableFromResource(PopUpDialogFragment.this.getActivity(), R.color.lightBlack))
                .into(imageView);
    }

    @Override @UiThread
    public void showSimilarImages(List<String> imageUris) {
        int length = imageUris.size();
        int similarImageSizes = similarImagesList.size();

        for (int i = 0; i < length && i < similarImageSizes; i++) {
            Glide.with(PopUpDialogFragment.this)
                    .load(imageUris.get(i))
                    .crossFade()
                    .placeholder(DrawableUtils.createColorDrawableFromResource(PopUpDialogFragment.this.getActivity(), R.color.lightBlack))
                    .into(similarImagesList.get(i));
        }
    }

    @Override
    public void onSimilarImagesError(@NonNull String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisplayImageMetaDataError(@NonNull String imageMetaDataError) {
        Toast.makeText(context, imageMetaDataError, Toast.LENGTH_LONG).show();
    }

    @Override @UiThread
    public void displayImageDescription(String title, String artist) {
        titleView.setText(context.getString(R.string.metadata_title_format, title, artist));
    }
}
