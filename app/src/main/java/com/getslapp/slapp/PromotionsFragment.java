/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Fragment for displaying the Promotions page.
 *
 * Display layout for this fragment is described within its XML file
 * (fragment_promotions_fragment.xml).
 *
 * An image is downloaded from a URL stored in sharedPreferences and
 * displayed. The onClick function for this image is implemented in
 * the MainActivity class.
 */
public class PromotionsFragment extends Fragment {

    public View mLayout;

    public PromotionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mLayout= inflater.inflate(R.layout.fragment_promotions_fragment, container, false);

        try {
            setImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mLayout;
    }

    public void setImage()throws IOException
    {
        String promo_image_link = ((MainActivity)getActivity()).sharedPref.getString(getString(R.string.shared_pref_promo_image_link), "");
        Log.i("Str img link 1", promo_image_link);

        if(promo_image_link != "")
        {
            Bitmap bitmap = fetchImage(promo_image_link);
            ImageButton btn = (ImageButton) mLayout.findViewById(R.id.imageButton4);
            btn.setImageBitmap(bitmap);
        }
        else
        {
            ImageButton btn = (ImageButton) mLayout.findViewById(R.id.imageButton4);
            btn.setImageResource(R.drawable.logo_white_background);
        }
    }

    private Bitmap fetchImage(String image_url)
    {
        InputStream input_stream = fetchStream(image_url);
        if(input_stream != null)
        {
            Bitmap image = BitmapFactory.decodeStream(fetchStream(image_url));
            return image;
        }
        return null;
    }

    private InputStream fetchStream(String url_string)
    {
        try
        {
            URL url = new URL(url_string);
            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
            c.setDoInput( true );
            c.connect();
            InputStream input_stream = c.getInputStream();
            return input_stream;
        }
        catch ( MalformedURLException e )
        {
            Log.d( "Promo Frag", "fetchStream - MalformedURLException: URL " + url_string + " error " + e);
        }
        catch ( IOException e )
        {
            Log.d( "Promo Frag", "fetchStream - IOException: " + e );
        }
        return null;
    }
}
