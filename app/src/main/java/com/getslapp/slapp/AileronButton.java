/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Class to use a custom font (Aileron Regular) for Button text.
 */
public class AileronButton extends Button {
    public AileronButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    public AileronButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public AileronButton(Context context) {
        super(context);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface custom_font = FontCache.getTypeFace("Aileron-Regular.otf", context);
        setTypeface(custom_font);
    }
}