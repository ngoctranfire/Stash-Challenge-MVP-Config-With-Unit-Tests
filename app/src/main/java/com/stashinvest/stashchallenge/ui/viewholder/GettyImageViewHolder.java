package com.stashinvest.stashchallenge.ui.viewholder;

import android.annotation.SuppressLint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.util.LongPressGestureDetector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GettyImageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_view)
    ImageView imageView;

    public GettyImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind(ImageResult imageResult, LongPressGestureDetector.Listener listener) {
        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(itemView.getContext(), new LongPressGestureDetector(listener));

        itemView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        Glide.with(itemView.getContext())
                .load(imageResult.getThumbUri())
                .placeholder(R.drawable.ic_photo_black_24dp)
                .crossFade()
                .into(imageView);
    }
}
