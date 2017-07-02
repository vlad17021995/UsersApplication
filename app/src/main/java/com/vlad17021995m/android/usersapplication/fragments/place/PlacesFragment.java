package com.vlad17021995m.android.usersapplication.fragments.place;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.fragments.place.dummy.DummyContent;

import java.util.List;

public class PlacesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private PlacesRecyclerViewAdapter adapter;

    public PlacesFragment() {
    }

    @SuppressWarnings("unused")
    public static PlacesFragment newInstance(int columnCount) {
        PlacesFragment fragment = new PlacesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_place_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new PlacesRecyclerViewAdapter(DummyContent.getPlaces(getActivity()), mListener, getActivity());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Place item) {
                EditPlaceFragment placeFragment = EditPlaceFragment.newInstance(item.getId());
                placeFragment.show(getActivity().getSupportFragmentManager(), "place_fragment");
            }

            @Override
            public void onRemovePlace(final Place item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("удалить место? " + item.getHeader())
                        .setPositiveButton("да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocalStorage storage = LocalStorage.getInstance(getActivity());
                                storage.deletePlace(item);
                                getAdapter().setmValues(DummyContent.getPlaces(getActivity()));
                                getAdapter().notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public PlacesRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(PlacesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Place item);

        void onRemovePlace(Place item);
    }
}
