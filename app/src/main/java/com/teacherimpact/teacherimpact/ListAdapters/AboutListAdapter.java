package com.teacherimpact.teacherimpact.ListAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teacherimpact.teacherimpact.R;

public class AboutListAdapter extends ArrayAdapter<String>{

    Activity activity;
    String[] aboutUsContentHeaders;
    String[] aboutUsContent;

    public AboutListAdapter(Activity activity, String[] aboutUsContentHeaders, String[] aboutUsContent) {
        super(activity, R.layout.activity_about_row, aboutUsContentHeaders);
        this.activity = activity;
        this.aboutUsContentHeaders = aboutUsContentHeaders;
        this.aboutUsContent = aboutUsContent;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_about_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_title);
        TextView txtInfo = (TextView) rowView.findViewById(R.id.tv_information);

        txtTitle.setText(aboutUsContentHeaders[position]);
        txtInfo.setText(aboutUsContent[position]);

        return rowView;
    }
}