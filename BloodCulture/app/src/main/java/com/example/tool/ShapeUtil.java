package com.example.tool;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 *
 */
public class ShapeUtil {

    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth){
        //左上、右上、右下、左下的圆角半径
        float[] fradius = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(fradius);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
