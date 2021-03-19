package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;

import java.util.List;

/**
 * This class provides access to the User items. It is responsible for making a View for each
 * user from the list.
 */
public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;

    public UserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method sets the user data that will be displayed in GUI
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_user_list, null);
        TextView userUsername = (TextView)v.findViewById(R.id.user_username);
        TextView userEmail = (TextView)v.findViewById(R.id.user_email);

        userUsername.setText(users.get(position).getUsername());
        userEmail.setText(users.get(position).getEmail());

        v.setTag(users.get(position).getId());
        return v;
    }
}
