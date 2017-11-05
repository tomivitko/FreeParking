package frapetrov.freeparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import frapetrov.freeparking.database.tables.ParkingLocationTable;

public class ParkingLocationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ParkingLocationTable pl = new ParkingLocationTable();
    Button buttonGoogleMaps;
    private SupportMapFragment mMap;
    GPSTracker gps;
    GoogleMap googleMap;
    TextView tvName, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_location_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.textViewName);
        tvAddress = (TextView) findViewById(R.id.textViewAddress);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetails));

        mMap.getMapAsync(this);

        gps = new GPSTracker(getApplicationContext());

        pl = (ParkingLocationTable) getIntent().getExtras().get("parkingLocation");

        tvName.setText(pl.getName());
        tvAddress.setText(pl.getAddress() + ", " + pl.getCity());
        buttonGoogleMaps = (Button) findViewById(R.id.buttonGoogleMaps);

        buttonGoogleMaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://maps.google.com/maps?saddr="
                                + gps.getLatitude() + ","
                                + gps.getLongitude() + "&daddr="
                                + pl.getLatitude() + "," + pl.getLongitude()));
                navigation.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(navigation);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng currentPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
        this.googleMap.addMarker(new MarkerOptions().position(currentPosition));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 12));

        LatLng parkingLocation = new LatLng(pl.getLatitude(), pl.getLongitude());
        this.googleMap.addMarker(new MarkerOptions().position(parkingLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

    }

}
