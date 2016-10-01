package com.teacherimpact.teacherimpact.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.ListAdapters.SkillsListAdapter;
import com.teacherimpact.teacherimpact.R;

import java.util.HashMap;
import java.util.Map;


public class FragmentAddComment extends Fragment {
    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String TEACHER = "Teacher";
    private static final String STUDENT = "Student";
    private static final String STUDENTPARENT = "StudentParent";
    private static final String USERMAIN = "User_Main";


    ImageView favorite;
    SkillsListAdapter skillsAdapter;
    User user, usermain;
    TeacherSkills teacherSkills;

    CommentContain comments;
    String[] endorsedSkills;
    Userid userid;
    boolean favorited;
    boolean viewingOtherUser = true;
    public FragmentAddComment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = getArguments().getParcelable(USER);
            this.userid = getArguments().getParcelable(USER_ID);
            this.teacherSkills = getArguments().getParcelable(USER_SKILLS);
            this.endorsedSkills = getArguments().getStringArray("Endorsements");
            this.comments = getArguments().getParcelable(USER_COMMENTS);
            this.usermain = getArguments().getParcelable(USERMAIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_fragment_add_comment, container, false);
        Button sumbit;
        sumbit = (Button) view.findViewById(R.id.comment_update_button);
        sumbit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et;
                et = (EditText) view.findViewById(R.id.editTextDialogUserInput);
                String comment;
                comment = et.getText().toString();
              pushComment(comment , userid.getId());
                Toast.makeText(getActivity(), "Comment Submitted", Toast.LENGTH_SHORT).show();
                android.app.Fragment newFrag = new FragmentProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelable(USER, user);
                bundle.putParcelable(USER_ID, userid);
                bundle.putParcelable(USER_SKILLS, teacherSkills);
                bundle.putStringArray("Endorsements", endorsedSkills);
                bundle.putParcelable(USER_COMMENTS, comments);
                bundle.putParcelable(USERMAIN, usermain);
                newFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFrag);
                transaction.commit();
            }
        });


        return view;
    }

    private void pushComment(String comment, String sender) {

        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());
        // Add some data to the new location
        Map<String, String> post1 = new HashMap<String, String>();
        post1.put("comment", comment);
        post1.put("sender", sender);
        post1.put("name", usermain.getFirstname());
        post1.put("viewflag", "false");

        commentref.push().setValue(post1);
    }

}
