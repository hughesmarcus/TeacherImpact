package com.teacherimpact.teacherimpact.ListAdapters;

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

public class SkillsListAdapter extends ArrayAdapter<String>{

    public Activity activity;
    public TeacherSkills teacherSkills;
    public User user;
    public boolean[] isEndorsed;
    public boolean viewingOtherUser;

    public SkillsListAdapter(Activity activity, Boolean otherUser, TeacherSkills teacherSkills, User user, String[] userEndorsedSkills) {
        super(activity, R.layout.activity_profile_skills_row, teacherSkills.getSkills());
        this.activity = activity;
        this.viewingOtherUser = otherUser;
        this.teacherSkills = teacherSkills;
        this.user = user;
        this.isEndorsed = new boolean[teacherSkills.getSkills().length];

        //A null list on endorsed skills either means the profile being viewed is the users or a parcelable issue occured.
        if (userEndorsedSkills != null)
            setEndorsements(userEndorsedSkills);
    }

    private void setEndorsements(String[] endorsements){

        String[] userSpecifiedSkills = teacherSkills.getSkills();

        for(int i = 0; i < userSpecifiedSkills.length; i++){

            for (int j = 0; j < endorsements.length; j++){
                if (userSpecifiedSkills[i].equals(endorsements[j]))
                    isEndorsed[i] = true;
            }
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (view == null){

            //inflate the layout
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.activity_profile_skills_row, null, true);

            //set up ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.endorseButton = (Button) view.findViewById(R.id.endorse_button);
            viewHolder.endorseButton.setVisibility(viewingOtherUser ? View.VISIBLE : View.GONE);
            viewHolder.skillName = (TextView) view.findViewById(R.id.skill_name);
            viewHolder.ensorseCount = (TextView) view.findViewById(R.id.endorse_count);

            //Store the holder with the view
            view.setTag(viewHolder);
        }
        else {
            //Use the viewHolder
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.endorseButton.setText(isEndorsed[position] ? "-" : "+");
        viewHolder.endorseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isEndorsed[position]){

                    //If the skill has been endorsed, then we're un-endorsing it.
                    teacherSkills.getValues()[position]--;
                    Toast.makeText(getContext(), String.format("You've unendorsed %s!", user.getFirstname() + " " + user.getLastname()), Toast.LENGTH_LONG).show();
                }else{

                    //If the skill hasn't been endorsed, then we want to.
                    teacherSkills.getValues()[position]++;
                    Toast.makeText(getContext(), String.format("You've endorsed %s!", user.getFirstname() + " " + user.getLastname()), Toast.LENGTH_LONG).show();
                }
                isEndorsed[position] = !isEndorsed[position];
                viewHolder.endorseButton.setText(isEndorsed[position] ? "-" : "+");
                viewHolder.ensorseCount.setText(String.format("%d", teacherSkills.getValues()[position]));
            }
        });
        viewHolder.ensorseCount.setText(String.format("%d", teacherSkills.getValues()[position]));
        viewHolder.skillName.setText(teacherSkills.getSkills()[position].toUpperCase());

        return view;
    }

    static class ViewHolder{
        Button endorseButton;
        TextView skillName;
        TextView ensorseCount;
    }
}

