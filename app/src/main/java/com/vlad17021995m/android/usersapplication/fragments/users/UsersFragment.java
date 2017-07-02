package com.vlad17021995m.android.usersapplication.fragments.users;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.vlad17021995m.android.usersapplication.activities.ViewActivity;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.fragments.users.dummy.DummyContent;


public class UsersFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private UsersRecyclerViewAdapter adapter;

    public UsersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UsersFragment newInstance(int columnCount) {
        UsersFragment fragment = new UsersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new UsersRecyclerViewAdapter(DummyContent.getUsersWithoutCurrent(getActivity()), mListener, getActivity());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = new OnListFragmentInteractionListener() {
            @Override
            public void onViewUser(User item) {
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                intent.putExtra(ViewActivity.KEY_USER_LOGIN, item.getMail());
                startActivity(intent);
            }

            @Override
            public void onDeleteUser(final User item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("удалить пользователя " + item.getName() + " " + item.getSurname() + "?")
                        .setPositiveButton("да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocalStorage storage = LocalStorage.getInstance(getActivity());
                                storage.deleteUser(item);
                                adapter.setmValues(DummyContent.getUsersWithoutCurrent(getActivity()));
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("нет", null).create().show();
            }

            @Override
            public void onBanUser(User item) {
                BanUserFragment fragment = BanUserFragment.newInstance(item.getMail());
                fragment.show(getActivity().getSupportFragmentManager(), "ban_user");
            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onViewUser(User item);

        void onDeleteUser(User item);

        void onBanUser(User item);
    }
}
