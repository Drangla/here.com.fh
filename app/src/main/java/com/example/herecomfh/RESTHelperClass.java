package com.example.herecomfh;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RESTHelperClass {

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String result;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()) {
                result =  scanner.next();
            } else {
                result = null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
        return result;
    }

    public static String postToHttpURL(URL url, String command) throws IOException {
        String result;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","text/plain");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            try {
                OutputStream os = urlConnection.getOutputStream();
                byte[] input = command.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e){
                Log.d("test", e.getMessage());
            }

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()) {
                result =  scanner.next();
            } else {
                result = null;
            }


        }
        finally {
            urlConnection.disconnect();
        }
        return result;
    }
}
