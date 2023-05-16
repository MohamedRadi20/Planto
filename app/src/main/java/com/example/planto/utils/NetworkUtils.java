package com.example.planto.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String Edible = "&edible=1";
    final static String Poisonous = "&poisonous=1&6";
    final static String indoor = "&indoor=1";
    final static String pages = "&page=1";
    final static String USADARA_BASE_URL="https://perenual.com/api/species-list?&key=sk-tSG4645bc67775b7f873"+pages+indoor;

    public static URL buildUrl(){

        Uri urlStr = Uri.parse(USADARA_BASE_URL)
                .buildUpon().build();
        URL url= null;
        try {
            url = new URL(urlStr.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getDatafromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasnext = scanner.hasNext();
            if (hasnext) {
                return scanner.next();
            } else
                return null;
        }
        finally {
            connection.disconnect();
        }

    }
}
