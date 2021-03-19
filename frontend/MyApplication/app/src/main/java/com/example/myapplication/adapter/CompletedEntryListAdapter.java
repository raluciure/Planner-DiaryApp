package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.CompletedEntry;

import java.util.List;

/**
 * This class provides access to the CompletedEntry items. It is responsible for making a View for each
 * completed calendar entry from the list.
 */
public class CompletedEntryListAdapter extends BaseAdapter {

    private Context context;
    private List<CompletedEntry> completedEntries;

    public CompletedEntryListAdapter(Context context, List<CompletedEntry> completedEntries) {
        this.context = context;
        this.completedEntries = completedEntries;
    }

    @Override
    public int getCount() {
        return completedEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return completedEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method sets the completed calendar entry data that will be displayed in GUI. In order for
     * the date to be displayed in HH:MM format I use a regex to split the String dateFrom and
     * String dateTo.
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_calendar_entry_list, null);

        TextView calendarEntryActivity = (TextView)v.findViewById(R.id.calendar_entry_activity);
        TextView calendarEntryDate = (TextView)v.findViewById(R.id.calendar_entry_date);

        calendarEntryActivity.setText(completedEntries.get(position).getActivity().getName());
        String[] dateFromParts = completedEntries.get(position).getDateFrom().split(" ")[1].split(":");
        String[] dateToParts = completedEntries.get(position).getDateTo().split(" ")[1].split(":");
        calendarEntryDate.setText(dateFromParts[0] + ":" + dateFromParts[1] + " - " + dateToParts[0] + ":" + dateToParts[1]);
        v.setTag(completedEntries.get(position).getId());
        return v;
    }
}
