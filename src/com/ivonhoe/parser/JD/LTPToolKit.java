package com.ivonhoe.parser.JD;

import util.JL;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LTPToolKit {

    private static final String URL_PATH = "http://api.ltp-cloud.com/analysis/?api_key=m1Z1d9l5wANy0feM0UnL1QJmTrMhLEDw6kpwiQLH&text=我是中国人。&pattern=dp&format=plain";

    public static InputStream getInputStream(String urlPath) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(URL_PATH);
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // set timeout
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.setDoInput(true);
                // set Method "GET"
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                JL.d("responseCode=" + responseCode);
                if (responseCode == 200) {
                    inputStream = httpURLConnection.getInputStream();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }

    public static void analyzeHttpResponse() {
        BufferedReader innet;
        try {
            InputStream inputStream = getInputStream("");
            if (inputStream == null) {
                return;
            }
            innet = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line;
            while ((line = innet.readLine()) != null) {
                System.out.println(line);
            }
            innet.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
