/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

/**
 * Class for storing data about the items displayed
 * in the event list in the EventsFragment
 */
public class EventListItem {
    private String event_id;
    private String event_url;
    private String event_short_desc;
    private String event_long_desc;
    private String event_address;
    private String event_time;
    private String event_requester;
    private String event_contact_info;

    public EventListItem(String id, String url, String short_desc, String long_desc, String address, String time, String requester, String contact_info)
    {
        super();
        event_id = id;
        event_url = url;
        event_short_desc = short_desc;
        event_long_desc = long_desc;
        event_address = address;
        event_time = time;
        event_requester = requester;
        event_contact_info = contact_info;
    }

    public String getId() { return event_id; }
    public void setId(String id) {event_id = id;}

    public String getUrl() { return event_url; }
    public void setUrl(String url) {event_url = url;}

    public String getShortDesc() { return event_short_desc; }
    public void setShortDesc(String short_desc) {event_short_desc = short_desc;}

    public String getLongDesc() { return event_long_desc; }
    public void setLongDesc(String long_desc) {event_long_desc = long_desc;}

    public String getAddress() { return event_address; }
    public void setAddress(String address) {event_address = address;}

    public String getTime() { return event_time; }
    public void setTime(String time) {event_time = time;}

    public String getRequester() { return event_requester; }
    public void setRequester(String requester) {event_requester = requester;}

    public String getContactInfo() { return event_contact_info; }
    public void setContactInfo(String contact_info) {event_contact_info = contact_info;}

}
