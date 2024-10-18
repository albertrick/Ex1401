package com.example.ex1401;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<Void, Void, String> {
    private DownloadTaskListener listener;

    public DownloadTask(DownloadTaskListener listener) {
        this.listener = listener;
    }
    @Override
    protected String doInBackground(Void... params) {
        String url = "https://boi.org.il/currency.xml";
        String downloadedData = "";
        try {
            URL urlObject = new URL(url);
            URLConnection connection = urlObject.openConnection();
            InputStream inputStream = connection.getInputStream();
            StringBuilder sb = new StringBuilder();
            int data;
            while ((data = inputStream.read()) != -1) {
                sb.append((char) data);
            }
            downloadedData = sb.toString();
            inputStream.close();
        } catch (MalformedURLException e) {
            Log.e("Downloader", "Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            Log.e("Downloader", "Error downloading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e("Downloader", "Security restriction on accessing URL: " + e.getMessage());
        }
        return downloadedData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onDownloadComplete(result);
    }

}
