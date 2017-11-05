package frapetrov.freeparking.database.tables;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import frapetrov.freeparking.database.ParkingLocationDB;

/**
 * Created by tvitko on 17.5.2017..
 */
@Table(database = ParkingLocationDB.class)
public class ParkingLocationTable extends BaseModel implements Parcelable{
    @Column
    @PrimaryKey(autoincrement = true)
    int id;
    @Column
    String name;
    @Column
    String address;
    @Column
    String city;
    @Column
    double latitude;
    @Column
    double longitude;

    public ParkingLocationTable() {
    }

    public ParkingLocationTable(String name, String address, String city, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private ParkingLocationTable(Parcel in){
        this.name = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<ParkingLocationTable> CREATOR = new Parcelable.Creator<ParkingLocationTable>() {

        @Override
        public ParkingLocationTable createFromParcel(Parcel source) {
            return new ParkingLocationTable(source);
        }

        @Override
        public ParkingLocationTable[] newArray(int size) {
            return new ParkingLocationTable[size];
        }
    };


}
