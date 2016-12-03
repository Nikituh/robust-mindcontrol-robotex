package com.choosemuse.example.libmuse.Networking;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aareundo on 06/09/16.
 */
public class Networking {

    static final String WHEELCHAIR_BASE = "http://192.168.37.157/cgi-bin/";

    public static final String WHEELFORWARDAPI = "forward";
    public static final String WHEELSTOPAPI = "stop";

    public static void SendCommand(String command)
    {
        URL url = null;

        try {
            url = new URL(WHEELCHAIR_BASE + command);
        } catch (MalformedURLException e) {
            System.out.println("NETWORKING: MalformedUrlException");
        }

        if (url == null) {
            System.out.println("NETWORKING: Malformed Url. Returning");
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("NETWORKING: IOException 1");
        }

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (NullPointerException e) {
            System.out.println("NETWORKING: NullPointerException");
        } catch (IOException e) {
            System.out.println("NETWORKING: IOException 2");
        }
    }

    public void DoStuff(String data)
    {
        new MyTask().execute(data);
    }

    private class MyTask extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String...params) {
            try {
                System.out.println(params);
                String data = params[0];
                SendCommand(data);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }

            return "";
        }


        protected void onProgressUpdate(Void x)
        {
        }

        protected void onPostExecute(Void y)
        {
        }

        public void SendCommand(String command)
        {
            URL url = null;

            try {
                url = new URL(WHEELCHAIR_BASE + command);
            } catch (MalformedURLException e) {
                System.out.println("NETWORKING: MalformedUrlException");
            }

            HttpURLConnection urlConnection = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                System.out.println("NETWORKING: IOException 1");
            }

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                System.out.println("NETWORKING: IOException 2");
            }
        }
    }

    public void ping(String url) {
        String str = "";

        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 8 " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();

            while ((i = reader.read(buffer)) > 0) {
                output.append(buffer, 0, i);
            }

            reader.close();

            str = output.toString();

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("PING RESULT: " + str);
    }
}
