package com.teacherimpact.teacherimpact.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.teacherimpact.teacherimpact.Activities.ActivityMain;
import com.teacherimpact.teacherimpact.DataTransferObjects.Comment;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.ListAdapters.SearchListAdapter;
import com.teacherimpact.teacherimpact.R;
import java.util.ArrayList;

public class FragmentSearch extends Fragment{

    //Parcelable tags
    private static final String USER = "User";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String USER_ENDORSEMENTS = "Endorsements";
    private static final String USER_ID = "User_ID";
    private static final String TEACHER = "Teacher";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USERMAIN = "User_Main";

    //Fragment items
    private View view;
    private MenuItem filterAction;
    private LinearLayout filters;

    //Layout items
    private ListView list;
    private ArrayList<User> users;
    private ArrayList<User> searchedUsers;

    //User items
    private User usermain;
    private Userid userid;
    private TeacherSkills teacherSkills;
    private String[] endorsedSkills;
    private CommentContain comments;

    //Filter items
    private CheckBox checkAll;
    private CheckBox checkTeachers;
    private CheckBox checkParents;
    private CheckBox checkStudents;
    private Spinner spinnerState;
    private TextView textCity;
    private TextView textZip;

    public FragmentSearch() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userid = getArguments().getParcelable(USER_ID);
        this.usermain = getArguments().getParcelable(USERMAIN);
    }

    @Override
    public void onResume(){
        checkAll.setChecked(true);
        checkTeachers.setChecked(false);
        checkParents.setChecked(false);
        checkStudents.setChecked(false);
        spinnerState.setSelection(0);
        textCity.setText(null);
        textZip.setText(null);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set fragment title
        ((ActivityMain) getActivity()).setTitle(getString(R.string.search));

        //Instantiate view
        view = inflater.inflate(R.layout.activity_search_filter, container, false);

        //Instantiate filter items
        checkAll = (CheckBox) view.findViewById(R.id.checkBox1);
        checkTeachers = (CheckBox) view.findViewById(R.id.checkBox2);
        checkParents = (CheckBox) view.findViewById(R.id.checkBox3);
        checkStudents = (CheckBox) view.findViewById(R.id.checkBox4);
        spinnerState = (Spinner) view.findViewById(R.id.spinnerState);
        textCity = (TextView) view.findViewById(R.id.textCity);
        textZip = (TextView) view.findViewById(R.id.textZip);
        Button buttonUpdate = (Button) view.findViewById(R.id.updateButton);

        //Overrides keyboard search on city filter
        textCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    ((ActivityMain) getActivity()).hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //Overrides keyboard search on zip code filter
        textZip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    ((ActivityMain) getActivity()).hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAll();
            }
        });

        checkTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        checkParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        checkStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        //Instantiate state spinner
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(R.layout.activity_search_spinner);
        spinnerState.setAdapter(adapterSpinner);

        //Instantiate array lists containing users
        users = new ArrayList<>();
        searchedUsers = new ArrayList<>();

        //Instantiates the filter layout
        filters = (LinearLayout) view.findViewById(R.id.searchFilterLayout);

        //Displays the filter icon on toolbar
        filterAction = ((ActivityMain) getActivity()).getFilterAction();
        filterAction.setVisible(true);

        //Instantiates adapter to hold searched users
        SearchListAdapter adapter = new SearchListAdapter(getActivity(), searchedUsers);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        //Sets listener for when a profile is clicked
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Gets clicked user
                User user = searchedUsers.get(+position);

                //Hides the filter button
                filterAction.setVisible(false);

                //Hides keyboard and sets the title appropriately
                ((ActivityMain) getActivity()).switchTitleSearch();
                ((ActivityMain) getActivity()).setTitle(getString(R.string.profileOther));

                if (user.getRole().equals(TEACHER)){
                    //Load other information for teachers; skills, endorsements, comments
                    loadSkills(user, userid.getId());
                }else{
                    loadFragment(user);
                }
            }
        });

        //Start search
        search();

        return view;
    }

    //Returns whether the search is open
    public boolean isOpen(){
        return FragmentSearch.this.isVisible();
    }

    public void check() {
        if (checkAll != null)
            checkAll.setChecked(false);
    }

    public void checkAll() {
        if (checkAll != null && checkTeachers != null && checkParents != null && checkStudents != null){
            if (checkAll.isChecked()) {
                checkTeachers.setChecked(false);
                checkParents.setChecked(false);
                checkStudents.setChecked(false);
            }
        }
    }

    //Searches users
    public void search(){
        //Resets searched users array
        searchedUsers = new ArrayList<>();

        String searchTerm = ((ActivityMain) getActivity()).getSearchTerm();
        users = ((ActivityMain) getActivity()).getUsers();

        if(filters.getVisibility() == View.VISIBLE){
            String filterState = spinnerState.getSelectedItem().toString().toLowerCase().trim();
            String filterCity = textCity.getText().toString().toLowerCase().trim();
            String filterZip = textZip.getText().toString().toLowerCase().trim();

            boolean filterAll = checkAll.isChecked();
            boolean filterTeacher = checkTeachers.isChecked();
            boolean filterParent = checkParents.isChecked();
            boolean filterStudent = checkStudents.isChecked();

            for(User u : users){
                if(u.searchMin().toLowerCase().contains(searchTerm.toLowerCase().trim())) {
                    if (filterState.equals("all") && filterCity.isEmpty() && filterZip.isEmpty()) {
                        if (filterTeacher && u.getRole().equals("Teacher")) {
                            searchedUsers.add(u);
                        } else if (filterParent && u.getRole().equals("StudentParent")) {
                            searchedUsers.add(u);
                        } else if (filterStudent && u.getRole().equals("Student")) {
                            searchedUsers.add(u);
                        } else if (filterAll) {
                            searchedUsers.add(u);
                        }
                    } else if (!filterState.equals("all") && filterCity.isEmpty() && filterZip.isEmpty()) {
                        if (u.getState().toLowerCase().trim().contains(filterState)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (!filterState.equals("all") && !filterCity.isEmpty() && filterZip.isEmpty()) {
                        if (u.getState().toLowerCase().trim().contains(filterState) && u.getCity().toLowerCase().trim().contains(filterCity)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (!filterState.equals("all") && filterCity.isEmpty() && !filterZip.isEmpty()) {
                        if (u.getState().toLowerCase().trim().contains(filterState) && u.getZip().toLowerCase().trim().contains(filterZip)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (!filterState.equals("all") && !filterCity.isEmpty() && !filterZip.isEmpty()) {
                        if (u.getState().toLowerCase().trim().contains(filterState) && u.getZip().toLowerCase().trim().contains(filterZip) && u.getCity().toLowerCase().trim().contains(filterCity)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (filterState.equals("all") && !filterCity.isEmpty() && filterZip.isEmpty()) {
                        if (u.getCity().toLowerCase().trim().contains(filterCity)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (filterState.equals("all") && filterCity.isEmpty() && !filterZip.isEmpty()) {
                        if (u.getZip().toLowerCase().trim().contains(filterZip)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else if (filterState.equals("all") && !filterCity.isEmpty() && !filterZip.isEmpty()) {
                        if (u.getCity().toLowerCase().trim().contains(filterCity) && u.getZip().toLowerCase().trim().contains(filterZip)) {
                            if (filterTeacher && u.getRole().equals("Teacher")) {
                                searchedUsers.add(u);
                            } else if (filterParent && u.getRole().equals("StudentParent")) {
                                searchedUsers.add(u);
                            } else if (filterStudent && u.getRole().equals("Student")) {
                                searchedUsers.add(u);
                            } else if (filterAll) {
                                searchedUsers.add(u);
                            }
                        }
                    } else {
                        searchedUsers.add(u);
                    }
                }
            }
        }else {
            for( User u : users){
                if(u.searchAll().toLowerCase().contains(searchTerm.toLowerCase().trim())){
                    searchedUsers.add(u);
                }
            }
        }

        //Loads the searched users into the list
        SearchListAdapter adapter = new SearchListAdapter(getActivity(), searchedUsers);
        list = (ListView)view.findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    //If a teacher profile is clicked this loads the skills
    private void loadSkills(final User user, final String id){
        Firebase skillRef = new Firebase("https://teacherimpact.firebaseio.com/skills").child(user.getUserid());

        skillRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Load Teacher Skills
                teacherSkills = dataSnapshot.getValue(TeacherSkills.class);
                loadEndorsements(user, id);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //If a teacher profile is clicked this loads the endorsements
    private void loadEndorsements(final User user, final String id){
        Firebase endorseRef = new Firebase("https://teacherimpact.firebaseio.com/endorsements").child(user.getUserid()).child(id);

        endorseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                endorsedSkills = dataSnapshot.getValue(String[].class);
                loadComments(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //If a teacher profile is clicked this loads the comments
    public void loadComments(final User user){
        Firebase commentRef = new Firebase("https://teacherimpact.firebaseio.com/Comment").child(user.getUserid());
        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] sender = new String[(int) dataSnapshot.getChildrenCount()];
                String[] values = new String[(int) dataSnapshot.getChildrenCount()];
                String[] names = new String[(int) dataSnapshot.getChildrenCount()];
                String[] flags = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment teacherComments;
                    teacherComments = postSnapshot.getValue(Comment.class);

                    sender[i] = teacherComments.getSender();
                    values[i] = teacherComments.getComment();
                    names[i] = teacherComments.getName();

                    flags[i] = teacherComments.getViewflag();
                    i++;
                }

                comments = new CommentContain(values, sender, names , flags);
                loadFragment(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //Loads the profile fragment
    private void loadFragment(User user){
        Fragment newFrag = new FragmentProfile();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        bundle.putParcelable(USERMAIN, usermain);
        bundle.putParcelable(USER_ID, userid);
        bundle.putParcelable(USER_SKILLS, teacherSkills);
        bundle.putStringArray(USER_ENDORSEMENTS, endorsedSkills);
        bundle.putParcelable(USER_COMMENTS, comments);
        bundle.putBoolean(VIEWING_FLAG, true);

        newFrag.setArguments(bundle);

        transaction.replace(R.id.container, newFrag);
        transaction.commit();
    }
}
