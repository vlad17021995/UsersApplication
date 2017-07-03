package com.vlad17021995m.android.usersapplication.fragments.users;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.fragments.users.UsersFragment.OnListFragmentInteractionListener;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Activity activity;

    public UsersRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, Activity activity) {
        mValues = items;
        mListener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.user_name_view.setText(mValues.get(position).getName() + " " + mValues.get(position).getSurname());
        holder.avatar_view.setImageURI(Uri.parse(getPath(Uri.parse(mValues.get(position).getAvatar_path()))));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onViewUser(holder.mItem);
                }
            }
        });

        holder.del_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener){
                    mListener.onDeleteUser(holder.mItem);
                }
            }
        });

        holder.block_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener){
                    mListener.onBanUser(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public List<User> getmValues() {
        return mValues;
    }

    public void setmValues(List<User> mValues) {
        this.mValues = mValues;
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(activity, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView avatar_view;
        public final TextView user_name_view;
        public final ImageView block_butt;
        public final ImageView del_butt;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatar_view = (ImageView) view.findViewById(R.id.avatar_view);
            user_name_view = (TextView) view.findViewById(R.id.user_name_view);
            block_butt = (ImageView) view.findViewById(R.id.block_butt);
            del_butt = (ImageView) view.findViewById(R.id.del_butt);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + user_name_view.getText() + "'";
        }
    }
}
