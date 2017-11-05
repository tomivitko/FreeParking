package frapetrov.freeparking.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import frapetrov.freeparking.GPSTracker;
import frapetrov.freeparking.MainActivity;
import frapetrov.freeparking.R;
import frapetrov.freeparking.database.tables.ParkingLocationTable;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by tvitko on 19.5.2017..
 */

public class AddLocationFragment extends Fragment {

    ParkingLocationTable r;
    GPSTracker gps;
    Button buttonAdd;
    EditText editTextName, editTextAddress, editTextCity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_location, container, false);

        editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
        editTextCity = (EditText) rootView.findViewById(R.id.editTextCity);
        buttonAdd = (Button) rootView.findViewById(R.id.buttonAddLocation);

        //dohvaća trenutnu lokaciju
        gps = new GPSTracker(rootView.getContext());

        /**
         * provjerava da li su upisana sva polja, ako nisu prikaže toast s obavijesti
         * ako su upisana sva polja upisuje u bazu podataka i pokreće MainActivity
         */
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editTextName.getText().toString()) || TextUtils.isEmpty(editTextAddress.getText().toString()) || TextUtils.isEmpty(editTextCity.getText().toString()))
                {
                    Toast.makeText(getContext(), getResources().getString(R.string.add_location_message), Toast.LENGTH_LONG).show();
                }
                else {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(rootView.getContext());
                    final LayoutInflater inflater = (LayoutInflater) rootView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View Viewlayout = inflater.inflate(R.layout.add_location_dialog, (ViewGroup) rootView.findViewById(R.id.layout_dialog));

                    dialog.setTitle(R.string.dialog_title);
                    dialog.setView(Viewlayout);
                    dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                                r = new ParkingLocationTable(editTextName.getText().toString(), editTextAddress.getText().toString(),
                                        editTextCity.getText().toString(), gps.getLatitude(), gps.getLongitude());
                                r.save();
                                Intent i = new Intent(rootView.getContext(), MainActivity.class);
                                startActivity(i);
                        }
                    });
                    dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // show it
                    dialog.show();
                }

            }
        });

        return rootView;
    }
}
