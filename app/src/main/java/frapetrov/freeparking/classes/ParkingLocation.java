package frapetrov.freeparking.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tvitko on 27.4.2017..
 */

public class ParkingLocation implements Parcelable{

    private String naziv;
    private String adresa;
    private String grad;
    private double latitude;
    private double longitude;

    public ParkingLocation(){

    }

    public ParkingLocation(String naziv, String adresa, String grad, double latitude, double longitude) {
        this.naziv = naziv;
        this.adresa = adresa;
        this.grad = grad;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
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

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(adresa);
        dest.writeString(grad);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private ParkingLocation(Parcel in){
        this.naziv = in.readString();
        this.adresa = in.readString();
        this.grad = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<ParkingLocation> CREATOR = new Parcelable.Creator<ParkingLocation>() {

        @Override
        public ParkingLocation createFromParcel(Parcel source) {
            return new ParkingLocation(source);
        }

        @Override
        public ParkingLocation[] newArray(int size) {
            return new ParkingLocation[size];
        }
    };
}
