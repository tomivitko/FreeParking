package frapetrov.freeparking.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import frapetrov.freeparking.GPSTracker;
import frapetrov.freeparking.MainActivity;
import frapetrov.freeparking.R;

/**
 * Created by tvitko on 3.5.2017..
 */

public class TestFragment extends Fragment {

    private Button b_get;
    private GPSTracker gps;
    double longitude;
    double latitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_test_fragment, container, false);

        b_get = (Button)rootView.findViewById(R.id.buttonGetGPS);



        b_get.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                gps = new GPSTracker(getActivity().getApplicationContext());


                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    Toast.makeText(getActivity().getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
                }
                else
                {

                    gps.showSettingsAlert();
                }

            }
        });
        return rootView;
    }

}

