package com.teacherimpact.teacherimpact.ListAdapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.R;
import com.teacherimpact.teacherimpact.R;


public class CommentListAdapter extends ArrayAdapter<String> {
    String[] comment;
    Context context;
    public Activity activity;
    String[] sender;
    String[] flags;
    private static LayoutInflater inflater = null;

    public CommentListAdapter(Activity activity, String[] comment, String[] sender ,String [] flags) {
        super(activity, R.layout.row_layout, comment);
        this.comment = comment;
        this.sender = sender;
        this.activity = activity;
        this.flags= flags;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return comment.length;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();

            view = inflater.inflate(R.layout.row_layout, null, true);
            viewHolder = new ViewHolder();
            viewHolder.comment1 = (TextView) view.findViewById(R.id.comment1);
            viewHolder.sender1 = (TextView) view.findViewById(R.id.sender1);

            view.setTag(viewHolder);
        } else {
            //Use the viewHolder
            viewHolder = (ViewHolder) view.getTag();
        }
        System.out.println(sender[position]);
        System.out.println(flags[position]);
        if(flags[position].equals("true")) {
            viewHolder.comment1.setText(comment[position]);

            viewHolder.sender1.setText(sender[position]);
        }
        viewHolder.sender1.setTypeface(null, Typeface.ITALIC);
        return view;
    }

    static class ViewHolder{

        TextView comment1, sender1;


    }
}
