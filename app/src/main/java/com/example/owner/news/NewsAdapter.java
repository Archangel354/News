package com.example.owner.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Owner on 7/23/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsList> {

    public NewsAdapter(Activity context, ArrayList<NewsList> newsRecords) {
        super(context, 0, newsRecords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }
        // Get the EarthquakeRecord object located at this "position" in the list
        NewsList currentNews = getItem(position);

        TextView txtTitleView = (TextView) listItemView.findViewById(R.id.txtTitle);

        // Find the TextView in the list_item.xml layout with the ID txtTitle
        // Get the title from the current newsRecord object and
        // set this text on the title TextView
        txtTitleView.setText(String.valueOf(currentNews.getmTitle()));

        // Find the TextView in the list_item.xml layout with the ID txtSection
        TextView textAuthor = (TextView) listItemView.findViewById(R.id.txtSection);
        // Get the section from the current newsRecord object and
        // set this text on the section TextView
        textAuthor.setText(currentNews.getmSection());

        // Find the TextView in the list_item.xml layout with the ID txtDate
        TextView textPublishedDate = (TextView) listItemView.findViewById(R.id.txtDate);
        // Get the date from the current newsRecord object and
        // set this text on the date TextView
        textPublishedDate.setText(currentNews.getmDate());

        return listItemView;
    }
}
