package r.sardorbek.mytaxiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import r.sardorbek.mytaxiapp.adapter.TripAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 1;

    ListView mListView;     //Trip history listView
    ArrayList<Data> tripHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.appBar_Title); //AppBar title set to "Trip history"

        tripHistory = new ArrayList<>();
        mListView = findViewById(R.id.ridesList); //Trip history listView initialized

        //Some random data for listView
        sampleData();

        TripAdapter tripAdapter = new TripAdapter(this, R.layout.activity_main, tripHistory);
        mListView.setAdapter(tripAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isGPSEnabled()) {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }

            }
        });

    }

    //Sample data of addresses and prices for List View
    public void sampleData() {
        tripHistory.add(new Data("Sagbon 156 a block, Olmazor district", "Amir Temur Avenue 2a block 23 A", "38.500sum"));
        tripHistory.add(new Data("Istiqbol 20 h, Mirabad dis.", "Sharaf Rashidov, prospect", "14.200sum"));
        tripHistory.add(new Data("Muqimi 2a h, Chilanzar dis.", "Qumbuloq 13 h, Uchtepa dis.", "9.500sum"));
        tripHistory.add(new Data("Shodlik 60 h, Uchtepa dis.", "Amir Temur Avenue 2a block 23 A", "12.500sum"));
        tripHistory.add(new Data("Sharaf Raswhidov, prospect", "Muqimi 2a h, Chilanzar dis.", "29.00sum"));
        tripHistory.add(new Data("Sagbon 156 a block, Olmazor district", "Amir Temur Avenue 2a block 23 A", "38.500sum"));
        tripHistory.add(new Data("Istiqbol 20 h, Mirabad dis.", "Sharaf Rashidov, prospect", "14.200sum"));
        tripHistory.add(new Data("Muqimi 2a h, Chilanzar dis.", "Qumbuloq 13 h, Uchtepa dis.", "9.500sum"));
        tripHistory.add(new Data("Shodlik 60 h, Uchtepa dis.", "Amir Temur Avenue 2a block 23 A", "12.500sum"));
        tripHistory.add(new Data("Sharaf Raswhidov, prospect", "Muqimi 2a h, Chilanzar dis.", "29.00sum"));

    }

    //Checks whether location is enabled or not
    public boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageLocationOff();
            return false;
        }
        return true;
    }

    //Alert message tells you to turn on your location
    private void buildAlertMessageLocationOff() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The application requires GPS to work, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

