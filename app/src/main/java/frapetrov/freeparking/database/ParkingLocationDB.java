package frapetrov.freeparking.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by tvitko on 17.5.2017..
 */
@Database(name = ParkingLocationDB.NAME, version = ParkingLocationDB.VERSION)
public class ParkingLocationDB {
    public static final String NAME = "FreeparkingDb";
    public static final int VERSION = 1;
}
