package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Activity;

import java.util.List;

/**
 * This class provides access to the Activity items. It is responsible for making a View for each
 * activity from the list.
 */
public class ActivityListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Activity> activities;

    public ActivityListAdapter(Context mContext, List<Activity> activities) {
        this.mContext = mContext;
        this.activities = activities;
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method sets the activity data that will be displayed in GUI
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_activity_list, null);
        TextView activityName = (TextView)v.findViewById(R.id.activity_name);
        TextView activityCategory = (TextView)v.findViewById(R.id.activity_category);
        TextView activityDescription = (TextView)v.findViewById(R.id.activity_description);

        activityName.setText(activities.get(position).getName());
        activityCategory.setText(activities.get(position).getCategory().getName());
        activityDescription.setText(activities.get(position).getDescription());

        v.setTag(activities.get(position).getId());
        return v;
    }
}
