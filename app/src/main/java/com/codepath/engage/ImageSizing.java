package com.codepath.engage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by emilyz on 7/12/17.
 */

public class ImageSizing extends android.support.v7.widget.AppCompatImageView {
    public ImageSizing(Context context) {
        super(context);
    }

    public ImageSizing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ImageSizing(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        setMeasuredDimension(width, height);
    }
}
