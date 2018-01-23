/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class which downloads and parses the DCU timetable.
 *
 * The URL from the timetable is built from the previous
 * input given by the user.
 *
 * The information from the parsed timetable is stored
 * in JSON format in the timetable shared preferences.
 *
 * The format of the timetable shared preferences is:
 *
 * {
 *     "Monday":[
 *          {
 *              "starttime" : "time1",
 *              "endtime" : "time2",
 *              "module_name" : "name1",
 *              "module_code" : "code1",
 *              "location" : "location1",
 *              "lecturer" : "lecturer1",
 *              "weeks" : "week1 - week6",
 *              "type" : "type1",
 *              "colour" : "colour1"
 *          },
 *          {
 *              ...
 *          }
 *     ],
 *     "Tuesday":[
 *          ...
 *     ],
 *     ...
 * }
 */
public class DCUDownloader {

    DCUDownloader()
    {

    }

    // Builds the timetable URL from previous user input
    public String getTimetableLink(String courseCode, String year, int semester)
    {
        String link = "https://www101.dcu.ie/timetables/feed.php?";
        link += "prog=" + courseCode + "&";
        link += "per=" + year + "&";
        if(semester == 1)
        {
            link += "week1=1" + "&";
            link += "week2=12" + "&";
        }
        else
        {
            link += "week1=13" + "&";
            link += "week2=52" + "&";
        }
        link += "day=1-7" + "&";
        link += "template=student";
        return link;
    }

    public String downloadTimetable(String link)
    {
        Log.i("downloadTimetable", link);
        InputStream is;

        try
        {
            URL url = new URL(link);
            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
            c.setDoInput(true);
            c.connect();
            is = c.getInputStream();
            String contentAsString = getStringFromInputStream(is);

            if (is != null) {
                is.close();
            }

            return contentAsString;
        }
        catch ( MalformedURLException e )
        {
            Log.e( "downloadTimetable", "invalid URL: " + link );
        }
        catch ( IOException e )
        {
            Log.e( "downloadTimetable", "IO exception: " + e );
        }
        return null;
    }

    public String extractTimetable(String html_string)
    {
        // extract the HTML table containing the timetable from the page source
        int index1 = html_string.indexOf("<!-- END REPORT HEADER -->");
        int index2 = html_string.lastIndexOf("<!-- START REPORT FOOTER -->");
        JSONObject json_timetable = parseCourse(html_string.substring(index1, index2));
        String string_timetable = json_timetable.toString();
        return string_timetable;
    }

    public static String getStringFromInputStream(InputStream stream) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    public static JSONArray sortArray(JSONArray jsonArr, final String sortBy){
        JSONArray sortedJsonArray = new JSONArray();
        try
        {
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();


                    try {
                        valA = (String) a.get(sortBy);
                        valA=("00000" + valA).substring(valA.length());
                        valB = (String) b.get(sortBy);
                        valB=("00000" + valB).substring(valB.length());
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArr.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return sortedJsonArray;
    }

    /**
     * Steps involved in this function:
     *
     * 1. The first column of the table is processed to identify
     *    how many rows make up each day. If a day takes up two
     *    rows then the second row will have an indent of one.
     *
     * 2. Process each row in the table based on the first column
     *    information.
     *
     * 3. Sort each day to ensure that all timeslots are in order
     *    and add the day to the JSON Time table.
     */
    private JSONObject parseCourse(String sHTML) {

        Integer[][] rowstart = new Integer[255][2];
        Document doc = Jsoup.parse("<HTML><body>"+sHTML+ "</body></html>");
;
        Elements rows = doc.select("body > table > tbody > tr");
        Integer rowsize=rows.size();

        JSONObject json_timetable = new JSONObject();
        JSONArray mon_array = new JSONArray();
        JSONArray tue_array = new JSONArray();
        JSONArray wed_array = new JSONArray();
        JSONArray thu_array = new JSONArray();
        JSONArray fri_array = new JSONArray();
        JSONArray sat_array = new JSONArray();
        JSONArray sun_array = new JSONArray();
        JSONObject json_module;

        try {

            for (int i = 0; i < rowsize ; i++) {
                if (rowstart[i][0] == null) {
                    rowstart[i][0] = i;
                    rowstart[i][1] = 1;

                    if(rows.get(i).select("td").get(0).hasAttr("rowspan"))
                    {
                        int rs = Integer.parseInt(rows.get(i).select("td").get(0).attr("rowspan"));

                        for (int j = 1; j < rs; j++) {
                            rowstart[i + j][0] = i;
                            rowstart[i + j][1] = 2;
                        }
                    }
                }
            }

            Element firstrow = rows.get(0);
            for (int i = 1; i < rowsize; i++) {
                int idy = rowstart[i][0];
                Element onerow =  rows.get(i);
                String sDay = rows.get(idy).child(0).text();
                int colspan = 0;
                Integer rowcells = rows.get(i).children().size();

                for (int j = (2 - rowstart[i][1]); j < rowcells; j++) {
                    Element mod=onerow.child(j);
                    if (mod.attr("id").equals("object-cell-border") ){
                        json_module = new JSONObject();
                        int duration = 0;
                        if(mod.hasAttr("colspan")) {
                            duration = Integer.parseInt(mod.attr("colspan")) - 1;
                        }
                        // Sets the colours based on the background colour in the online timetable.
                        // (Not currently used but left in incase of future use)
                        //json_module.put("colour", Color.parseColor(mod.attr("bgcolor")));

                        json_module.put("starttime", firstrow.child(j + rowstart[i][1] + colspan - 1).text());
                        json_module.put("endtime", firstrow.child(j + rowstart[i][1] + colspan + duration).text());
                        String courseinfo = mod.html();
                        json_module.put("type", courseinfo.substring(0,courseinfo.indexOf("<!-- START OBJECT-CELL -->") - 1));
                        Document coursedetails = Jsoup.parse(courseinfo);
                        Elements ctable = coursedetails.select("Table");
                        json_module.put("location", ctable.get(0).text());
                        json_module.put("lecturer", ctable.get(1).select("td").get(0).text());
                        json_module.put("module_name", ctable.get(1).select("td").get(1).text());
                        json_module.put("module_code",ctable.get(2).select("td").get(0).text());

                        // Set colours based on SLAPP colours instead of the background colours from the webpage.
                        switch(json_module.getString("type"))
                        {
                            case "Lec. ": json_module.put("colour", Color.parseColor("#48e651"));
                                break;
                            case "Tut. ": json_module.put("colour", Color.parseColor("#19AD21"));
                                break;
                            case "Sem. ": json_module.put("colour", Color.parseColor("#E448DC"));
                                break;
                            case "Pra. ": json_module.put("colour", Color.parseColor("#E448DC"));
                                break;
                        }

                        //Possible Dev: Figure out a way to convert weeks to a list
                        String weeks = ctable.get(2).select("td").get(1).text();
                        json_module.put("weeks", weeks);
                        colspan = colspan + duration;

                        switch(sDay)
                        {
                            case "Mon":
                                mon_array.put(json_module);
                                break;
                            case "Tue":
                                tue_array.put(json_module);
                                break;
                            case "Wed":
                                wed_array.put(json_module);
                                break;
                            case "Thu":
                                thu_array.put(json_module);
                                break;
                            case "Fri":
                                fri_array.put(json_module);
                                break;
                            case "Sat":
                                sat_array.put(json_module);
                                break;
                            case "Sun":
                                sun_array.put(json_module);
                                break;
                        }

                    }
                }
            }

            json_timetable.put("monday", sortArray(mon_array,"starttime"));
            json_timetable.put("tuesday", sortArray(tue_array,"starttime"));
            json_timetable.put("wednesday", sortArray(wed_array,"starttime"));
            json_timetable.put("thursday", sortArray(thu_array,"starttime"));
            json_timetable.put("friday", sortArray(fri_array,"starttime"));
            json_timetable.put("saturday", sortArray(sat_array,"starttime"));
            json_timetable.put("sunday", sortArray(sun_array,"starttime"));
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json_timetable;
    }
}
