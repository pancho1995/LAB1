package com.example.lab1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lab1.databinding.ActivityMainBinding;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public LocationManager locationManager;
    Spinner spinnerFromCurrency;
    public String baseCurrency;
    public String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private RequestQueue requestQueue;
    private static final String APIKEY = "N1q1IQ4i70DFKCJe1NCLQO6txefww27c";
    public String lastFetchedDate = "";
    public boolean readFromOldResource = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 101);
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            getLocation();

            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if(isNetworkAvailable()) {
            if(checkIfFileExist("lastFetchedDate.txt")) {
                try {
                    String lastDateFetched = readFromFile("lastFetchedDate.txt");
                    lastFetchedDate = lastDateFetched;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    boolean isLatestDataFetched = !sdf.parse(lastDateFetched).before(sdf.parse(dateToday));
                    if(!isLatestDataFetched) {
                        String[] currForBase = getResources().getStringArray(R.array.currencies_for_select);
                        for (String base : currForBase) {
                            fetchDataFromAPI(base);
                        }
                        writeIntoFile(dateToday, "lastFetchedDate");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                String[] currForBase = getResources().getStringArray(R.array.currencies_for_select);
                for (String base : currForBase) {
                    fetchDataFromAPI(base);
                }
                try {
                    writeIntoFile(dateToday, "lastFetchedDate");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(checkIfFileExist("lastFetchedDate.txt")) {
                try {
                    this.lastFetchedDate = readFromFile("lastFetchedDate.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                readFromOldResource = true;
            }
        }

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            enableLocationService();
            getLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_rates) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_HomeFragment_to_RatesFragment);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        invalidateOptionsMenu();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void enableLocationService() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Turn on Location")
                    .setMessage("We need your location for better experience.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String country = addresses.get(0).getCountryName();
            baseCurrency = country;
            spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency);

            setSelectionByUserLocation(country);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    private void setSelectionByUserLocation(String country) {

        switch (country) {
            case "United States":
                spinnerFromCurrency.setSelection(2);
                break;
            case "Sweden":
                spinnerFromCurrency.setSelection(1);
                break;
            case "Great Britain":
                spinnerFromCurrency.setSelection(3);
                break;
            case "China":
                spinnerFromCurrency.setSelection(4);
                break;
            case "Japan":
                spinnerFromCurrency.setSelection(5);
                break;
            case "South Korea":
                spinnerFromCurrency.setSelection(6);
                break;
        }

    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchDataFromAPI(String base) {
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.apilayer.com/exchangerates_data/latest?symbols=EUR%2CGBP%2CSEK%2CJPY%2CCNY%2CSEK%2CUSD%20&base=" + base;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean success = response.getBoolean("success");
                            String date = response.getString("date");
                            JSONObject rates = response.getJSONObject("rates");
                            lastFetchedDate = date;
                            if (success) {
                                writeIntoFile(rates.toString(), base);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apikey", APIKEY);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void writeIntoFile(String jsonData, String base) throws IOException {
        File path = this.getFilesDir();
        File latest_rates = new File(path, base + ".txt");

        FileOutputStream stream = new FileOutputStream(latest_rates);
        try {
            stream.write(jsonData.getBytes());
        } finally {
            stream.close();
        }
    }

    public boolean checkIfFileExist(String filename){
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }

    public String readFromFile(String filename) throws IOException {

        String output = "";
        File fileToRead = getBaseContext().getFileStreamPath(filename);
        BufferedReader br = new BufferedReader(new FileReader(fileToRead));
        String tempLine = br.readLine();
        while(tempLine != null) {
            output += tempLine;
            tempLine = br.readLine();
        }
        return output;
    }

    private void helper() throws IOException {
        File path = this.getFilesDir();
        File latest_rates = new File(path, "lastFetchedDate.txt");

        FileOutputStream stream = new FileOutputStream(latest_rates);
        try {
            stream.write(("2022-11-28").getBytes());
        } finally {
            stream.close();
        }
    }

    public String getLastFetchedDate() {
        return this.lastFetchedDate;
    }
    public boolean getReadFromOldResource() {
        return this.readFromOldResource;
    }
}