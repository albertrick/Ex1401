package com.example.ex1401;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements DownloadTaskListener {

    ListView lV;
    ArrayList<String> fileData = new ArrayList<>();
    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lV= findViewById(R.id.lV);
    }

    public void getData(View view) {
        DownloadTask dt = new DownloadTask(this);
        dt.execute();
    }

    @Override
    public void onDownloadComplete(String result) {
        Log.i("MainActivity", "listener called");
        fileData.clear();
//        fileData = parseXmlData(result);
        fileData = parseJsonData(result);
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileData);
        lV.setAdapter(adp);
    }

    public ArrayList<String> parseJsonData(String jsonData) {
        String all = "";
        ArrayList<String> dataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray exchangeRates = jsonObject.getJSONArray("exchangeRates");
            for (int i = 0; i < exchangeRates.length(); i++) {
                JSONObject currencyData = exchangeRates.getJSONObject(i);
                String key = currencyData.getString("key");
                double currentExchangeRate = currencyData.getDouble("currentExchangeRate");
                double currentChange = currencyData.getDouble("currentChange");
                int unit = currencyData.getInt("unit");
                String lastUpdate = currencyData.getString("lastUpdate");
                // Add data to the list
                all += key + ", ";
                all += currentExchangeRate + ", ";
                all += unit;
//                all += lastUpdate);
                dataList.add(all);
                all = "";
            }
        } catch (JSONException e) {
            Log.e("MainActivity","Error parsing JSON: " + e.getMessage());
        }
        return dataList;
    }

    public ArrayList<String> parseXmlData(String xmlData) {
        String all = "";
        ArrayList<String> dataList = new ArrayList<>();
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            Reader in = new StringReader(xmlData);
            parser.setInput(in);
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                Log.i("MainActivity", "event: " + event);
                if (event == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "key":
                            parser.next();
                            all += parser.getText() + " ";
                            break;
                        case "COUNTRY":
                            parser.next();
                            all += parser.getText() + " ";
                            break;
                        case "CURRENCYCODE":
                            parser.next();
                            all += parser.getText() + " ";
                            break;
                        case "RATE":
                            parser.next();
                            all += parser.getText() + " ";
                            break;
                        case "CHANGE":
                            dataList.add(all);
                            all = "";
                            break;
                        default:
                            break;
                    } //end of switch
                }  //end of if
                event=parser.next();
            } //end of while
        } catch (XmlPullParserException e) {
            Log.e("MainActivity", "Error parsing XML: " + e.getMessage());
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading XML: " + e.getMessage());
        }
        return dataList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}