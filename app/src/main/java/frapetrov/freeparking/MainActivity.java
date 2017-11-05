package frapetrov.freeparking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

import frapetrov.freeparking.database.tables.ParkingLocationTable;
import frapetrov.freeparking.fragments.AboutFragment;
import frapetrov.freeparking.fragments.AddLocationFragment;
import frapetrov.freeparking.fragments.ListFragment;
import frapetrov.freeparking.fragments.MapsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "freeParkingPref";
    public static final String distance = "distanceValue";
    public static int RANGE = 10;
    public static GPSTracker gps;
    private ArrayList<ParkingLocationTable> parkingLocationListAll = new ArrayList<>();
    private static ArrayList<ParkingLocationTable> parkingLocationListInRange = new ArrayList<>();

    public static ArrayList<ParkingLocationTable> getParkingLocationsInRange(){
        return parkingLocationListInRange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //generira parkirna mjesta samo ako nisu upisana u bazu, znači pri prvom pokretanju
        if(SQLite.select().from(ParkingLocationTable.class).count() == 0) {
            generateParkingPlaces();
        }


        //ako je u mobitelu spremljena neka vrijednost za udaljenost onda uzima tu vrijednost
        //ako nije defaultno je 10km što je gore definirano
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(distance)) {
            RANGE = sharedpreferences.getInt(distance, 0);
        }

        //dohvaća sve zapise iz baze podataka
        parkingLocationListAll = new ArrayList<ParkingLocationTable>(SQLite.select().from(ParkingLocationTable.class).queryList());
        //dohvaća zapise koji su unutar odabrane udaljenosti i sprema ih u parkingLocationListInRange
        getLocationsInRange();

        //na content_main unutar MainActivity stavlja ListFragment s popisom lokaciju u odapranom opsegu
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange)).addToBackStack("fragment").commit();

        //standardno za drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * sprema početne lokacije u bazu podataka, poziva se samo pri prvom pokretanju
     */
    public void generateParkingPlaces(){
        ParkingLocationTable r = new ParkingLocationTable("Parking 1", "Grič 15", "Samobor", 45.83166992, 15.73100567);
        r.save();
        r = new ParkingLocationTable("Besplatno", "Ilica 15", "Samobor", 45.82987577, 15.71791649);
        r.save();
        r = new ParkingLocationTable("Parking 44", "Nova 15", "Samobor", 45.82975616, 15.73954582);
        r.save();
        r = new ParkingLocationTable("ParkingZG", "BLa bla 15", "Zagreb", 45.819633, 16.054896);
        r.save();
        r = new ParkingLocationTable("ParkingVG", "ulica 5", "Zagreb", 45.727043, 16.091196);
        r.save();
        r = new ParkingLocationTable("ParkingZG", "BLa bla 15", "Zagreb", 45.819633, 16.054896);
        r.save();
        r = new ParkingLocationTable("ParkingZaprešić", "Račkog 6", "Zaprešić", 45.864526, 15.797574);
        r.save();
        r = new ParkingLocationTable("Besplatno", "Tina Ujevića 3", "Zaprešić", 45.871135, 15.796389);
        r.save();
        r = new ParkingLocationTable("Parking 22", "Mokrička 57", "Zaprešić", 45.866282, 15.793949);
        r.save();

    }

    //ovo mi nije proradilo
/*
    @Override
    public void onResume(){
        super.onResume();
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if(index != -1) {
            getLocationsInRange();
            String name = getSupportFragmentManager().getBackStackEntryAt(index).getName();
            if (name.equals("map")) {
                getSupportFragmentManager().popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange), "list").addToBackStack("list").commit();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange), "list").addToBackStack("list").commit();

    }
*/

    /**
     * zatvara drawer ako je otvoren,
     * ako nije vraća se po backstacku.
     * problem je ako se vratiš na mapu aplikacija se sruši
     * samo navigiramo na listu, a ako korisnik hoće mapu neka stisne na map u meniu
     */
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            //dobije se index predzadnjeg u backstacku
            int index = getSupportFragmentManager().getBackStackEntryCount() - 2;
            String name;

            if (index == -1) {
                //ako je index -1 ode na home screen
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                //nađe ime fragmenta, ako je mapa otvori listu, ako nije otvori ono što je po redu
                name = getSupportFragmentManager().getBackStackEntryAt(index).getName();
                if (name.equals("map")) {
                    getSupportFragmentManager().popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange), "list").addToBackStack("list").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }

    }
    //stavlja desni meni na ekran
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //otvara settings meni
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.distance_settings) {
            showSettingsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    //odnosi se na drawer, mijenja fragmente na MainActivity
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MapsFragment(), "map").addToBackStack("map").commit();

        } else if (id == R.id.nav_list) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange), "list").addToBackStack("list").commit();

        } else if (id == R.id.nav_settings) {
            showSettingsDialog();

        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment(), "about").addToBackStack("about").commit();

        } else if (id == R.id.nav_add) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddLocationFragment(), "add").addToBackStack("add").commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * otvara settings dialog za podešavanje opsega
     * ako u shared preferences ima nešto spremljeno to postavi na slideru, ako nema onda je 10
     * na OK sprema u shared preferences, poziva metodu getLocationsInRange i otvara listu
     */
    public void showSettingsDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.settings_dialog, (ViewGroup) findViewById(R.id.layout_dialog));
        final TextView item1 = (TextView)Viewlayout.findViewById(R.id.TextViewSeekBarProgress); // txtItem1

        dialog.setTitle(R.string.dialog_title);
        dialog.setView(Viewlayout);

        final SeekBar seek1 = (SeekBar) Viewlayout.findViewById(R.id.seekBar1);
        seek1.setMax(30);
        if(sharedpreferences.contains(distance)){
            seek1.setProgress(sharedpreferences.getInt(distance, 0));}
        else{
            seek1.setProgress(10);}

        item1.setText(seek1.getProgress() + " km");
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                item1.setText(progress + " km");
            }
            public void onStartTrackingTouch(SeekBar arg0) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                RANGE = seek1.getProgress();
                editor.putInt(distance, seek1.getProgress());
                editor.commit();
                getLocationsInRange();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ListFragment().newInstance(parkingLocationListInRange), "list").addToBackStack("list").commit();

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

    /**
     * upisuje lokacije u opsegu u listu parkingLocationListInRange
     */


    public void getLocationsInRange(){
        gps = new GPSTracker(getApplicationContext());
        float[] results = new float[1];
        parkingLocationListInRange = new ArrayList<>();

        for(ParkingLocationTable pl : parkingLocationListAll)
        {
            Location.distanceBetween(gps.getLatitude(), gps.getLongitude(),
                    pl.getLatitude(), pl.getLongitude(), results);

            if(results[0] < (float) (RANGE * 1000))
                parkingLocationListInRange.add(pl);
        }
    }
}
