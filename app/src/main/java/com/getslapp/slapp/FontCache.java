/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Class used in the setting of custom fonts.
 *
 * Called by the AileronTextView, AileronButton and
 * AileronEditText classes.
 */
public class FontCache {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeFace(String fontname, Context context)
    {
        Typeface typeface = fontCache.get(fontname);

        if(typeface == null)
        {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            }
            catch (Exception e){
                Log.e("FontCache", "Error" + e);
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
