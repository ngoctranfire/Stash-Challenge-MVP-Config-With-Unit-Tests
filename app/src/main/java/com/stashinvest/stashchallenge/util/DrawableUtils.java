package com.stashinvest.stashchallenge.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

/**
 * Created by ngoctranfire on 9/13/17.
 */

public class DrawableUtils {
    public static ColorDrawable createColorDrawableFromResource(@NonNull Context context, @ColorRes int colorRes) {
        return createColorDrawableFromColor(context.getResources().getColor(colorRes));
    }

    public static ColorDrawable createColorDrawableFromColor(@ColorInt int color) {
        return new ColorDrawable(color);
    }
}
