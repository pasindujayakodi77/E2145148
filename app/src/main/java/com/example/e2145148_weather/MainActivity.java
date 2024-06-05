package com.example.e2145148_weather;


import android.annotation.SuppressLint;

import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView latitudeLongitudeTextView, addressTextView, systemTimeTextView, weatherInfoTextView, temperatureTextView, humidityTextView, weatherDescriptionTextView;
    private RelativeLayout mainLayout;
    private RequestQueue requestQueue;
    private static final String API_KEY = "db291665816d306519a6e02468ad9d63";

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        latitudeLongitudeTextView = findViewById(R.id.latitudeLongitude);
        addressTextView = findViewById(R.id.address);
        systemTimeTextView = findViewById(R.id.systemTime);
        weatherInfoTextView = findViewById(R.id.weatherInfo);
        temperatureTextView = findViewById(R.id.temperature);
        humidityTextView = findViewById(R.id.humidity);
        weatherDescriptionTextView = findViewById(R.id.weatherDescription);
        mainLayout = findViewById(R.id.mainLayout);

        // Initialize request queue
        requestQueue = Volley.newRequestQueue(this);

        // Use your real coordinates
        double latitude = 7.088466850302816;
        double longitude = 80.03861842537052;

        // Set latitude and longitude text
        latitudeLongitudeTextView.setText(getString(R.string.latitude_longitude_label, latitude, longitude));

        // Fetch address and weather data
        fetchAddress(latitude, longitude);
        fetchWeather(latitude, longitude);
        updateSystemTime();
    }

    private void fetchAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                addressTextView.setText(address.getAddressLine(0));
            } else {
                addressTextView.setText(R.string.address_not_found);
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressTextView.setText(R.string.geocoder_error);
        }
    }

    private void fetchWeather(double latitude, double longitude) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + API_KEY;

        Log.d(TAG, "Weather API URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("StringFormatMatches")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "Weather API Response: " + response.toString());
                            JSONObject main = response.getJSONObject("main");
                            double temperature = main.getDouble("temp");
                            int humidity = main.getInt("humidity");

                            String weatherDescription = response.getJSONArray("weather").getJSONObject(0).getString("description");

                            temperatureTextView.setText(getString(R.string.temperature_label, temperature));
                            humidityTextView.setText(getString(R.string.humidity_label, humidity));
                            weatherDescriptionTextView.setText(getString(R.string.weather_description_label, weatherDescription));

                            // Update background based on weather description
                            updateBackground(weatherDescription);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            weatherInfoTextView.setText(R.string.weather_data_parsing_error);
                            Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        weatherInfoTextView.setText(R.string.weather_data_fetching_error);
                        Log.e(TAG, "Volley error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateBackground(String weatherDescription) {
        if (weatherDescription.contains("sunny")) {
            mainLayout.setBackgroundResource(R.drawable.sunny_background);
        } else if (weatherDescription.contains("rain")) {
            mainLayout.setBackgroundResource(R.drawable.rainy_background);
        } else if (weatherDescription.contains("cloud")) {
            mainLayout.setBackgroundResource(R.drawable.cloudy_background);
        } else {
            mainLayout.setBackgroundResource(R.drawable.default_background);
        }
    }

    private void updateSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(System.currentTimeMillis());
        systemTimeTextView.setText(getString(R.string.time_label, currentTime));

        // Update the time every second
        systemTimeTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSystemTime();
            }
        }, 1000);
    }
}







