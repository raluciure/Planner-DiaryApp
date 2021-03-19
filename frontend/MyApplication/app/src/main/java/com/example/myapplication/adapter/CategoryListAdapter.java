package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Category;

import java.util.List;

/**
 * This class provides access to the Category items. It is responsible for making a View for each
 * category from the list.
 */
public class CategoryListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Category> categories;

    public CategoryListAdapter(Context mContext, List<Category> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method sets the Category data that will be displayed in GUI
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_category_list, null);

        TextView categoryName = (TextView)v.findViewById(R.id.category_name);
        categoryName.setText(categories.get(position).getName());

        v.setTag(categories.get(position).getId());
        return v;
    }
}
