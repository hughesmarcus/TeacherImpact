package com.teacherimpact.teacherimpact.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teacherimpact.teacherimpact.DataTransferObjects.Comment;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.ListAdapters.AboutListAdapter;
import com.teacherimpact.teacherimpact.ListAdapters.SkillsListAdapter;
import com.teacherimpact.teacherimpact.R;

public class FragmentHome extends Fragment {

    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String TEACHER = "Teacher";
    private static final String STUDENT = "Student";
    private static final String STUDENTPARENT = "StudentParent";

    User user;
    AboutListAdapter aboutAdapter;
    ListView list;

    //These are just static strings until the web app starts doing announcements.
    String entry1 = "Want to meet the development team behind the TeacherIMPACT mobile application? They'll be showcasing the application during the PCEC Project Day event " +
            "in downtown Grand Rapids on the Grand Valley State University Pew Campus April 21st at 10:00am to 12:00pm. \n\n\nMore details can be found at http://www.cis.gvsu.edu/events/pcec-student-project-day/";

    String entry2 = "Welcome to the TeacherIMPACT mobile application! While the application is in beta, we would love to hear more from you on how we can improve it. " +
            "Whether you want some more information about the application, or the team itself, feel free to reach out. We promise to get back to you with lightning speeds. \n\n\n Contact us at info.teacherimpact.com";
    String[] dateHeaders = new String[]{"4/13/16", "4/10/16"};
    String[] content = new String[] {entry1, entry2};


    private OnFragmentInteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            this.user = getArguments().getParcelable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tv;

        aboutAdapter = new AboutListAdapter(getActivity(), dateHeaders, content);
        list = (ListView) view.findViewById(R.id.home_listview);
        list.setAdapter(aboutAdapter);

        if (user != null){
            tv = (TextView)view.findViewById(R.id.home_wb_label);
            String wb = "Welcome Back, " + user.getFirstname() + "!";
            tv.setText(wb);
        }
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}