package frapetrov.freeparking.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import frapetrov.freeparking.R;
import frapetrov.freeparking.classes.ParkingLocation;
import frapetrov.freeparking.adapters.AdapterListFragment;
import frapetrov.freeparking.database.tables.ParkingLocationTable;

/**
 * Created by tvitko on 7.5.2017..
 */

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ParkingLocationTable> parkingLocationList = new ArrayList<>();

    public ListFragment(){}

    public static ListFragment newInstance(final ArrayList<ParkingLocationTable> lista ){
        final ListFragment fragment = new ListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList("lista", lista);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // Layout manager:
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        parkingLocationList = (this.getArguments().getParcelableArrayList("lista"));

        mAdapter = new AdapterListFragment(parkingLocationList);
        mRecyclerView.setAdapter(mAdapter);



        return rootView;
    }

}
