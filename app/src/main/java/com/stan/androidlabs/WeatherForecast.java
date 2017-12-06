package com.stan.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "WeatherForecast";

    private ImageView weatherImageView;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private ProgressBar progressBar;
    private String cityName = "Ottawa";
    private String apiKey = "d99666875e0e51521f0040a3d97d0f6a";
    private String forecastURL = "http://api.openweathermap.org/data/2.5/weather?q="
            + cityName
            + ",ca&APPID="
            + apiKey
            + "&mode=xml&units=metric";

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);


        weatherImageView = findViewById(R.id.weather_imageView);
        currentTemp = findViewById(R.id.current_textView);
        minTemp = findViewById(R.id.min_textView);
        maxTemp = findViewById(R.id.max_textView);
        progressBar = findViewById(R.id.progressBar);

        Log.i(ACTIVITY_NAME, "In OnCreate");

        ForecastQuery forecastQuery = new ForecastQuery();

        forecastQuery.execute();
    }

    private boolean fileExists(String sIcon) {
        File file = getBaseContext().getFileStreamPath(sIcon);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min, max, current, iconName;
        Bitmap currentIcon;


        @Override
        protected String doInBackground(String... strings) {
            Log.i(ACTIVITY_NAME, "In doInBackground");

            try {
                URL url = new URL(forecastURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                //starts the query
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                XmlPullParser pullParser = Xml.newPullParser();
                pullParser.setInput(inputStream, null);

                while (pullParser.next() != XmlPullParser.END_DOCUMENT) {
                    if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (pullParser.getName().equals("temperature")) {
                        current = pullParser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = pullParser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = pullParser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if (pullParser.getName().equals("weather")) {
                        iconName = pullParser.getAttributeValue(null, "icon");
                        String savedIcon = iconName + ".png";
                        if (fileExists(savedIcon)) {
                            FileInputStream fileInputStream = null;
                            try {
                                fileInputStream = new FileInputStream(getBaseContext().getFileStreamPath(savedIcon));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            currentIcon = BitmapFactory.decodeStream(fileInputStream);
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            currentIcon = getImage(iconUrl);
                            FileOutputStream fileOutputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            currentIcon.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        publishProgress(100);
                    }
                }

                Log.i(ACTIVITY_NAME, current + max + min);

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String degree = Character.toString((char) 0x00B0);
            currentTemp.setText(currentTemp.getText() + current + degree + "C");
            minTemp.setText("Min: " + minTemp.getText() + min + degree + "C");
            maxTemp.setText("Max: " + maxTemp.getText() + max + degree + "C");
            weatherImageView.setImageBitmap(currentIcon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}
