package com.teacherimpact.teacherimpact.ListAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesListAdapter extends ArrayAdapter<User>{
    private final Activity activity;
    private final List<User> users;

    public FavoritesListAdapter(Activity activity, ArrayList<User> users) {
        super(activity, R.layout.activity_search_row, users);
        this.activity = activity;
        this.users = users;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_search_row, null, true);

        TextView textName = (TextView) rowView.findViewById(R.id.search_textName);
        TextView textInfo1 = (TextView) rowView.findViewById(R.id.search_textInfo1);
        TextView textInfo2 = (TextView) rowView.findViewById(R.id.search_textInfo2);
        TextView textInfo3 = (TextView) rowView.findViewById(R.id.search_textInfo3);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.search_imageProfile);

        imageView.setImageResource(R.drawable.profile_default);
        textName.setText(users.get(position).getFirstname() + " " + users.get(position).getLastname());
        textInfo1.setText(users.get(position).getRole());
        textInfo2.setText(users.get(position).getEmail());
        textInfo3.setText(users.get(position).getState() + " : " + users.get(position).getCity() + " : " + users.get(position).getZip());

        return rowView;
    }
}
