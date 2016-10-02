package com.nutikorv.andreas.nutikorvalpha.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ANDREAS on 22.09.2016.
 */
public class IntroSlidesTextView extends TextView {
    public static Typeface TITILLIUM_WEB_FONT;


    public IntroSlidesTextView(Context context) {
        super(context);
        if (TITILLIUM_WEB_FONT == null)
            TITILLIUM_WEB_FONT = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-Regular.ttf");
        this.setTypeface(TITILLIUM_WEB_FONT);
    }

    public IntroSlidesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (TITILLIUM_WEB_FONT == null)
            TITILLIUM_WEB_FONT = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-Regular.ttf");
        this.setTypeface(TITILLIUM_WEB_FONT);
    }

    public IntroSlidesTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (TITILLIUM_WEB_FONT == null)
            TITILLIUM_WEB_FONT = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-Regular.ttf");
        this.setTypeface(TITILLIUM_WEB_FONT);
    }
}
