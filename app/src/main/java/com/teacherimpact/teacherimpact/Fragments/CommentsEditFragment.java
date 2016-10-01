package com.teacherimpact.teacherimpact.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.app.Activity;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.teacherimpact.teacherimpact.Activities.ActivityMain;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.R;


public class CommentsEditFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    User user;
    TeacherSkills teacherSkills;
    public Activity activity;
    CommentContain comments;
    String[] endorsedSkills;
    Userid userid;
    public CommentsEditFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CommentsEditFragment newInstance(int columnCount) {
        CommentsEditFragment fragment = new CommentsEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            if (getArguments() != null) {
                this.user = getArguments().getParcelable(USER);
                this.userid = getArguments().getParcelable(USER_ID);
                this.teacherSkills = getArguments().getParcelable(USER_SKILLS);
                this.endorsedSkills = getArguments().getStringArray("Endorsements");
                this.comments = getArguments().getParcelable(USER_COMMENTS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments_list, container, false);
        ListView lv;
        lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(new CommentEditAdapter(getActivity(), comments.getComment(), comments.getName(), comments.getViewFlag()));

        return view;


    }


    public class CommentEditAdapter extends ArrayAdapter {

        String[] flag;
        String[] comment;
        Context context;
        public Activity activity;
        String[] sender;
        private  LayoutInflater inflater = null;

        public CommentEditAdapter(Activity activity, String[] comment, String[] sender, String [] flag) {
            super(activity, R.layout.row_layout, comment);
            this.comment = comment;
            this.sender = sender;
            this.flag = flag;
            this.activity = activity;

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

                view = inflater.inflate(R.layout.fragment_comments, null, true);
                viewHolder = new ViewHolder();
                viewHolder.comment1 = (TextView) view.findViewById(R.id.comment1);
                viewHolder.sender1 = (TextView) view.findViewById(R.id.sender1);
                if(flag.equals("true")) {
                    viewHolder.viewButton.setChecked(true);
                }
                if(flag.equals("false")) {
                    viewHolder.viewButton.setChecked(false);
                }
                view.setTag(viewHolder);
            } else {
                //Use the viewHolder
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.comment1.setText(comment[position]);
            viewHolder.sender1.setText(sender[position]);


            viewHolder.deleteButton = (Button) view.findViewById(R.id.comeditbutton);
            viewHolder.viewButton = (Switch) view.findViewById(R.id.CommentView);
            //  deleteButton.setTag(position);

            viewHolder.deleteButton.setOnClickListener(
                    new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String com = viewHolder.comment1.getText().toString();

                            String sen =   viewHolder.sender1.getText().toString();

                            comments=  ((ActivityMain) getActivity()).commentdelete(com , sen);
                            notifyDataSetChanged();
                        }
                    }
            );
            viewHolder.viewButton.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            String flag;
                            String com = viewHolder.comment1.getText().toString();
                            System.out.println(com);
                            String sen =   viewHolder.sender1.getText().toString();

                            if(isChecked){

                                ((ActivityMain) getActivity()).setView(com , sen , "true");
                                notifyDataSetChanged();
                            }else{
                                ((ActivityMain) getActivity()).setView(com , sen , "false");
                                notifyDataSetChanged();
                            }

                        }
                    });

            return view;
        }

         class ViewHolder{
            Button deleteButton;
               Switch viewButton;
            TextView comment1, sender1;


        }


    }


}

