package frapetrov.freeparking.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import frapetrov.freeparking.GPSTracker;
import frapetrov.freeparking.MainActivity;
import frapetrov.freeparking.R;
import frapetrov.freeparking.database.tables.ParkingLocationTable;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mMap;
    GPSTracker gps;
    GoogleMap googleMap;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_maps_fragment, container, false);

            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

            mMap.getMapAsync(this);

            gps = new GPSTracker(getActivity().getApplicationContext());

            if(gps.canGetLocation()){

                double longitude = gps.getLongitude();
                double latitude = gps.getLatitude();

            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "no gps", Toast.LENGTH_LONG).show();
                gps.showSettingsAlert();
            }

            return rootView;
        }


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng currentPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
        this.googleMap.addMarker(new MarkerOptions().position(currentPosition));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 12));

        for(ParkingLocationTable pl : MainActivity.getParkingLocationsInRange())
        {
            LatLng parkingLocation = new LatLng(pl.getLatitude(), pl.getLongitude());
            this.googleMap.addMarker(new MarkerOptions().position(parkingLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .title(pl.getName())
                    .snippet(pl.getAddress() + ", " + pl.getCity()));

        }
    }
}
