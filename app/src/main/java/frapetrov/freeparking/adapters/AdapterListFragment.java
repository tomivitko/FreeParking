package frapetrov.freeparking.adapters;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import frapetrov.freeparking.ParkingLocationDetailsActivity;
import frapetrov.freeparking.R;
import frapetrov.freeparking.database.tables.ParkingLocationTable;

/**
 * Created by tvitko on 7.5.2017..
 */

public class AdapterListFragment extends RecyclerView.Adapter<AdapterListFragment.ViewHolder> {

    private List<ParkingLocationTable> parkingLocationList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nazivText, adresaText;
        public ViewHolder(View view){
            super(view);

            nazivText = (TextView) view.findViewById(R.id.locationName);
            adresaText = (TextView) view.findViewById(R.id.locationAddress);
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int poz = getAdapterPosition();
            ParkingLocationTable parkingLocation = parkingLocationList.get(poz);

            Intent intent = new Intent(v.getContext(), ParkingLocationDetailsActivity.class);
            intent.putExtra("parkingLocation", parkingLocation);
            v.getContext().startActivity(intent);
        }


    }

    public AdapterListFragment(List<ParkingLocationTable> parkingLocationList){
        this.parkingLocationList = parkingLocationList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParkingLocationTable parkingLocation = parkingLocationList.get(position);
        holder.nazivText.setText(parkingLocation.getName());
        holder.adresaText.setText(String.valueOf(parkingLocation.getAddress()) +", "+ parkingLocation.getCity());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_row,parent, false);
        return new ViewHolder(v);
    }


    @Override
    public int getItemCount() {
        return parkingLocationList.size();
    }
}
