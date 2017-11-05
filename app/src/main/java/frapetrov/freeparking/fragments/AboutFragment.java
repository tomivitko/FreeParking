package frapetrov.freeparking.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frapetrov.freeparking.R;
import frapetrov.freeparking.adapters.AdapterListFragment;

/**
 * Created by tvitko on 17.5.2017..
 */

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);


        return rootView;
    }
}
