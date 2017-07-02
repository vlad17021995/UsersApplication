package com.vlad17021995m.android.usersapplication.fragments.place;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.fragments.place.PlacesFragment.OnListFragmentInteractionListener;

import java.util.List;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {



    private List<Place> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Activity activity;

    public PlacesRecyclerViewAdapter(List<Place> items, OnListFragmentInteractionListener listener, Activity activity) {
        mValues = items;
        mListener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_places_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.header.setText(mValues.get(position).getHeader());
        holder.avatarView.setImageURI(Uri.parse(getPath(Uri.parse(mValues.get(position).getAvatar_path()))));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener){
                    mListener.onRemovePlace(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public List<Place> getmValues() {
        return mValues;
    }

    public void setmValues(List<Place> mValues) {
        this.mValues = mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView header;
        public final ImageView avatarView;
        public final ImageView remover;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            header = (TextView) view.findViewById(R.id.header);
            avatarView = (ImageView) view.findViewById(R.id.avatar_view);
            remover = (ImageView) view.findViewById(R.id.remover);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + header.getText() + "'";
        }
    }
}
