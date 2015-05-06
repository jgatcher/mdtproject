package com.walkwithme.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Utility class containing various static methods used throughout the app
 * @author heinz
 *
 */
public class Util {

    /**
     * Utility method to return a String from contents at url
     * @param urlString a string representing a url
     * @return an InputStream from given url
     */
    public static String getURL(String urlString) {
        String responseString = "";
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            responseString=streamToString(conn.getInputStream());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseString;
    }

    /**
     * Utility method to return an InputStream created from a url
     * @param urlString a string representing a url
     * @return an InputStream from given url
     */
    public static InputStream getURLStream(String urlString){
        URL url;
        URLConnection conn=null;
        InputStream is=null;
        try {
            url = new URL(urlString);//create a url object from given string
            conn =  url.openConnection();//open a url connection
            is=conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
    /**
     * converts contents of stream into a string
     * @param is
     * @return a string representation of the inputstream'scontents
     * @throws IOException
     */
    static String streamToString(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        //try with resources available from Java 7 should auto close the reader without all the ugly code :-)

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));//accented characters didn't appear with UTF8
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        }catch (IOException ioe){
            Log.d("wvm",ioe.toString());

        }
        return builder.toString();
    }




    /* Given a URL referring to an image, return a bitmap of that image
             */
    static Bitmap getRemoteImage(final String url) {
        try {
            BufferedInputStream bis = new BufferedInputStream(getURLStream(url));
            Bitmap bm = BitmapFactory.decodeStream(bis);//convert stream content into a bitmap
            bis.close();
            return bm;
        } catch (IOException e) {
            // e.printStackTrace();
            return null;
        }
    }

}
