/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

/**
 * Class for storing data about the items displayed
 * in the links list in the LinksFragment
 */
public class LinksListItem {
    private String link_text;
    private String link_url;

    public LinksListItem(String text, String url)
    {
        super();
        link_text = text;
        link_url = url;
    }

    public String getText() { return link_text; }
    public void setText(String text) {link_text = text;}

    public String getUrl() { return link_url; }
    public void setUrl(String url) {link_url = url;}
}
