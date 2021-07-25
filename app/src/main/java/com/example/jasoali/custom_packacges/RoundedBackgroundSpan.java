package com.example.jasoali.custom_packacges;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jasoali.R;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private final int mPadding = 15;
    private int mBackgroundColor;
    private int mTextColor;
    public static int[] tagsColors = new int[]{
            R.color.tags0,
            R.color.tags1,
            R.color.tags2,
            R.color.tags3,
            R.color.tags4,
            R.color.tags5,
            R.color.tags6,
            R.color.tags7
    };
    static int cnt = 0;
    static public int getNextColor(){
        cnt ++;
        return tagsColors[(cnt-1)%tagsColors.length];
    }

    public RoundedBackgroundSpan(int backgroundColor, int textColor) {
        super();
        mBackgroundColor = backgroundColor;
        mTextColor = textColor;
    }


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float width = paint.measureText(text.subSequence(start, end).toString());
        RectF rect = new RectF(x, top + mPadding, x + width + 2 * mPadding, bottom);
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(rect, mPadding, mPadding, paint);
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x + mPadding, y, paint);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) (mPadding + paint.measureText(text.subSequence(start, end).toString()) + mPadding);
    }
}