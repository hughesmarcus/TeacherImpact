package com.teacherimpact.teacherimpact.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.teacherimpact.teacherimpact.Activities.ActivityMain;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.Favorite;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.ListAdapters.CommentListAdapter;
import com.teacherimpact.teacherimpact.ListAdapters.SkillsListAdapter;
import com.teacherimpact.teacherimpact.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {

    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String USER_ENDORSEMENTS = "Endorsements";
    private static final String TEACHER = "Teacher";
    private static final String STUDENT = "Student";
    private static final String STUDENTPARENT = "StudentParent";
    private static final String USERMAIN = "User_Main";

    private SkillsListAdapter skillsAdapter;
    private User user, usermain;
    private TeacherSkills teacherSkills;
    private CommentContain comments;
    private String[] endorsedSkills;
    private Userid userid;
    private boolean viewingOtherUser = false;

    //Favorites
    private ImageView favorite;
    private boolean favorited;

    //Comments
    private LinearLayout commentLayout;
    private LinearLayout commentLayoutSubmitted;
    private EditText commentText;
    private TextView commentSubmittedText;
    private boolean commentSubmitted;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = getArguments().getParcelable(USER);
            this.userid = getArguments().getParcelable(USER_ID);
            this.teacherSkills = getArguments().getParcelable(USER_SKILLS);
            this.endorsedSkills = getArguments().getStringArray(USER_ENDORSEMENTS);
            this.comments = getArguments().getParcelable(USER_COMMENTS);
            this.viewingOtherUser = getArguments().getBoolean(VIEWING_FLAG);
            this.usermain = getArguments().getParcelable(USERMAIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        Firebase.setAndroidContext(getActivity().getBaseContext());
        Button commentButton;
        ImageView edit, message;


        commentSubmittedText = (TextView) view.findViewById(R.id.commentSubmittedText);
        commentLayout = (LinearLayout) view.findViewById(R.id.profile_layoutComments);
        commentLayoutSubmitted = (LinearLayout) view.findViewById(R.id.profile_layoutCommentsSubmitted);
        commentButton = (Button) view.findViewById(R.id.buttonSubmit);
        commentText = (EditText) view.findViewById(R.id.commentText);
        commentText.setHint("Leave a comment for " + user.getFirstname());
        commentSubmitted = false;

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClick();
            }
        });

        //Overrides keyboard enter
        commentText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    submitClick();
                    return true;
                }
                return false;
            }
        });

        if (user != null) {
            view = initializeTabs(view, user);
            view = populateBasicInfo(view, user);
            view = initializeComments(view);
            view = populateUserData(view, user, viewingOtherUser);
        }

        edit = (ImageView) view.findViewById(R.id.profile_edit_profile);
        edit.setVisibility(viewingOtherUser ? View.GONE : View.VISIBLE);

        message = (ImageView) view.findViewById(R.id.profile_comment_button);
     //   message.setVisibility(viewingOtherUser ? View.VISIBLE : View.GONE);

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment newFrag = new FragmentUpdateProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelable(USER, user);
                bundle.putParcelable(USER_ID, userid);
                bundle.putParcelable(USER_SKILLS, teacherSkills);
                newFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Will need to pull value from database when setup
        favorited = false;

        favorite = (ImageView) view.findViewById(R.id.profile_favorite_button);
        favorite.setVisibility(viewingOtherUser ? View.VISIBLE : View.GONE);

        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                favoritesButtonClicked();
            }
        });

        if (viewingOtherUser)
            checkFavorite();

        return view;
    }

    private void setTabTextColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {

            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorPrimary));
        }
    }

    private void setSelectedTabColor(TabHost tabhost, String tab) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorAccentGray)); //unselected

        if (tabhost.getCurrentTabTag().equals(tab))
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorAccentBlue)); //1st tab selected
    }

    private View populateBasicInfo(View view, User user) {

        TextView tv;
        String name;

        tv = (TextView) view.findViewById(R.id.profile_textName);
        name = user.getFirstname() + " " + user.getLastname();
        tv.setText(name);
        tv.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.colorAccentBlue));

        tv = (TextView) view.findViewById(R.id.profile_role);

        if (user.getRole().equals(STUDENT))
            tv.setText(new StringBuilder().append(user.getRole()).append(" | ").append(user.getGrade()));
        else
            tv.setText(user.getRole());

        tv = (TextView) view.findViewById(R.id.profile_gender);
        tv.setText(user.getGender());
        tv = (TextView) view.findViewById(R.id.profile_age);
        tv.setText(new StringBuilder().append(user.getBirthdate()).append(" Years Old"));

        return view;
    }

    private View initializeTabs(View view, User user) {

        final TabHost host;
        host = (TabHost) view.findViewById(R.id.profile_tabHost);
        host.setup();

        switch (user.getRole()) {
            case TEACHER:
                onCreateTeacherProfile(host);
                break;
            case STUDENT:
                onCreateStudentProfile(host);
                break;
            case STUDENTPARENT:
                onCreateStudentParentProfile(host);
                break;
        }

        setSelectedTabColor(host, "Tab One");
        setTabTextColor(host);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab) {
                setSelectedTabColor(host, tab);
            }
        });

        return view;
    }

    private View initializeComments(View view) {

        ImageView commentbutton;

        commentbutton = (ImageView) view.findViewById(R.id.profile_comment_button);
        commentbutton.setVisibility(!user.getRole().equals("Teacher") ? View.GONE : View.VISIBLE);
        // add button listener
        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getUserid().equals(userid.getId())) {
                    if(!commentSubmitted) {
                        if (commentLayout.getVisibility() == View.VISIBLE) {
                            commentLayout.setVisibility(View.GONE);
                            ((ActivityMain) getActivity()).hideKeyboard();
                        } else {
                            commentLayout.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(commentLayoutSubmitted.getVisibility() == View.VISIBLE){
                            commentLayoutSubmitted.setVisibility(View.GONE);
                        }else{
                            commentLayoutSubmitted.setVisibility(View.VISIBLE);
                            commentLayoutSubmitted.postDelayed(new Runnable() {
                                public void run() {
                                    commentLayoutSubmitted.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }
                    }
                } else {

                    Fragment newFrag = new CommentsEditFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(USER, user);
                    bundle.putParcelable(USER_ID, userid);
                    bundle.putParcelable(USER_SKILLS, teacherSkills);
                    bundle.putStringArray("Endorsements", endorsedSkills);
                    bundle.putParcelable(USER_COMMENTS, comments);
                    newFrag.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, newFrag);
                    transaction.commit();
                }
            }
        });
        return view;
    }

    public void submitClick() {
        ((ActivityMain) getActivity()).hideKeyboard();
        pushComment(commentText.getText().toString(), userid.getId());
        commentText.setText(null);
        commentLayout.setVisibility(View.GONE);
        commentSubmitted = true;
        commentLayoutSubmitted.setVisibility(View.VISIBLE);
        commentSubmittedText.setText("Comment submitted, awaiting approval from " + user.getFirstname());

        commentLayoutSubmitted.postDelayed(new Runnable() {
            public void run() {
                commentLayoutSubmitted.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void pushComment(String comment, String sender) {
        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());
        // Add some data to the new location
        Map<String, String> post1 = new HashMap<>();
        post1.put("comment", comment);
        post1.put("sender", sender);
        post1.put("name", usermain.getFirstname());
        post1.put("viewflag", "false");

        commentref.push().setValue(post1);
    }

    private View populateUserData(View view, User user, boolean viewingOtherUser) {

        TextView tv;
        ListView skillsList;

        //Populate information for Teacher User
        if (user.getRole().equals(TEACHER)) {

            //Set tab scroll indicators
            view.findViewById(R.id.left_tab_indicator).setVisibility(View.VISIBLE);
            view.findViewById(R.id.right_tab_indicator).setVisibility(View.VISIBLE);

            //Overview data
            tv = (TextView) view.findViewById(R.id.profile_textOverview);
            tv.setText(Html.fromHtml(setOverviewContent(user.getTeachingphilosophy(), user.getAwards() + "<p>" + user.getCertifications())));

            //Experience data
            tv = (TextView) view.findViewById(R.id.profile_textExperience);
            tv.setText(Html.fromHtml(setExperienceContent(user.getExperience())));

            //Background data
            tv = (TextView) view.findViewById(R.id.profile_textBackground);
            tv.setText(Html.fromHtml(setBackgroundContent(user.getBackground())));

            //Skills data
            if (teacherSkills != null) {
                skillsAdapter = new SkillsListAdapter(getActivity(), viewingOtherUser, teacherSkills, user, endorsedSkills);
                skillsList = (ListView) view.findViewById(R.id.profile_listView);
                skillsList.setAdapter(skillsAdapter);
            }

            if(comments != null) {
                String com[] = comments.getComment();
                String sen[] = comments.getName();
                String flag[] = comments.getViewFlag();

                ArrayList<String> com1 = new ArrayList<>();
                ArrayList<String> flag1 = new ArrayList<>();
                ArrayList<String> sen1 = new ArrayList<>();
                for (int i = 0; i < comments.getViewFlag().length; i++) {

                    if (flag[i].equals("true")) {
                        com1.add(com[i]);
                        flag1.add(flag[i]);
                        sen1.add(sen[i]);
                    }

                }
                String[] comments = com1.toArray(new String[com1.size()]);
                String[] views = flag1.toArray(new String[flag1.size()]);
                String[] names = sen1.toArray(new String[sen1.size()]);

                ListView lv;
                lv = (ListView) view.findViewById(R.id.profile_textComments);
                CommentListAdapter adapter = (new CommentListAdapter(getActivity(), comments, names, views));
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            //Interests data
            tv = (TextView) view.findViewById(R.id.profile_textInterests);
            tv.setText(Html.fromHtml(setInterestsContent(user.getInterests())));
        }

        //Populate information for Student User
        if (user.getRole().equals(STUDENT)) {
            //Strengths data
            tv = (TextView) view.findViewById(R.id.profile_textStrengths);
            tv.setText(Html.fromHtml(setStrengthsContent(user.getLstrengths())));

            //Weaknesses data
            tv = (TextView) view.findViewById(R.id.profile_textWeaknesses);
            tv.setText(Html.fromHtml(setWeaknessContent(user.getLweaknesses())));

            //Interests data
            tv = (TextView) view.findViewById(R.id.profile_textInterests);
            tv.setText(Html.fromHtml(setInterestsContent(user.getInterests())));
        }

        //Populate information for Student Parent User
        if (user.getRole().equals(STUDENTPARENT)) {
            //Overview data
            tv = (TextView) view.findViewById(R.id.profile_textOverview);
            tv.setText(Html.fromHtml(setBioContent(user.getBiography())));

            //Interests data
            tv = (TextView) view.findViewById(R.id.profile_textInterests);
            tv.setText(Html.fromHtml(setInterestsContent(user.getInterests())));
        }

        return view;
    }
    static String[] removeAt(int k, String[] arr) {
        final int L = arr.length;
        String[] ret = new String[L - 1];
        System.arraycopy(arr, 0, ret, 0, k);
        System.arraycopy(arr, k + 1, ret, k, L - k - 1);
        return ret;
    }
    public String setOverviewContent(String teaching_philosophy, String awards_recognitions) {

        String content = "";
        String tp_header = "<b>Teaching Philosophy</b><p>";
        String ar_header = "<b>Awards & Recognitions</b><p>";
        String spacing = "<p>";

        if (teaching_philosophy != null && !teaching_philosophy.isEmpty()) {
            if (teaching_philosophy.contains("\n"))
                teaching_philosophy = teaching_philosophy.replaceAll("\n", "<p>");
            content += tp_header;
            content += teaching_philosophy;
            content += spacing;
        }

        if (awards_recognitions != null && !awards_recognitions.isEmpty()) {
            if (awards_recognitions.contains("\n"))
                awards_recognitions = awards_recognitions.replaceAll("\n", "<p>");
            content += ar_header;
            content += awards_recognitions;
            content += spacing;
        }

        return content;
    }

    public String setExperienceContent(String employment_history) {

        String content = "";
        String eh_header = "<b>Employment History</b><p>";
        String spacing = "<p>";

        if (employment_history != null && !employment_history.isEmpty()) {
            if (employment_history.contains("\n"))
                employment_history = employment_history.replaceAll("\n", "<p>");
            content += eh_header;
            content += employment_history;
            content += spacing;
        }

        return content;
    }

    public String setBackgroundContent(String academic_background) {

        String content = "";
        String eh_header = "<b>Academic Background</b><p>";
        String spacing = "<p>";

        if (academic_background != null && !academic_background.isEmpty()) {
            if (academic_background.contains("\n"))
                academic_background = academic_background.replaceAll("\n", "<p>");
            content += eh_header;
            content += academic_background;
            content += spacing;
        }

        return content;
    }

    public String setInterestsContent(String interests) {

        String content = "";
        String eh_header = "<b>Interests</b><p>";
        String spacing = "<p>";

        if (interests != null && !interests.isEmpty()) {
            if (interests.contains("\n"))
                interests = interests.replaceAll("\n", "<p>");
            content += eh_header;
            content += interests;
            content += spacing;
        }

        return content;
    }

    public String setStrengthsContent(String content) {

        String result = "";
        String eh_header = "<b>Strengths</b><p>";
        String spacing = "<p>";

        if (content != null && !content.isEmpty()) {
            if (content.contains("\n"))
                content = content.replaceAll("\n", "<p>");
            result += eh_header;
            result += content;
            result += spacing;
        }

        return result;
    }

    public String setWeaknessContent(String content) {

        String result = "";
        String eh_header = "<b>Weaknesses</b><p>";
        String spacing = "<p>";

        if (content != null && !content.isEmpty()) {
            if (content.contains("\n"))
                content = content.replaceAll("\n", "<p>");
            result += eh_header;
            result += content;
            result += spacing;
        }

        return result;
    }

    public String setBioContent(String content) {

        String result = "";
        String spacing = "<p>";

        if (content != null && !content.isEmpty()) {
            if (content.contains("\n"))
                content = content.replaceAll("\n", "<p>");
            result += content;
            result += spacing;
        }

        return result;
    }

    private void favoritesButtonClicked() {
        if (favorited) {
            favorite.setBackgroundResource(R.drawable.favorite_disabled);
            favorited = false;
            removeFavorite();
        } else {
            favorite.setBackgroundResource(R.drawable.favorite_enabled);
            favorited = true;
            pushFavorite();
        }
    }

    private void pushFavorite() {
        Firebase favoriteRef = new Firebase("https://teacherimpact.firebaseio.com/Favorite");
        favoriteRef = favoriteRef.child(usermain.getUserid());

        // Add some data to the new location
        Map<String, String> favorite = new HashMap<>();
        favorite.put("favorite", user.getUserid());

        favoriteRef.push().setValue(favorite);
    }

    private void removeFavorite(){
        Firebase favoriteRef = new Firebase("https://teacherimpact.firebaseio.com/Favorite").child(usermain.getUserid());

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        Favorite favorite = postSnapshot.getValue(Favorite.class);

                        if(favorite.getId().equals(user.getUserid())){
                            postSnapshot.getRef().removeValue();
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR : " + e);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void checkFavorite(){
        Firebase favoriteRef = new Firebase("https://teacherimpact.firebaseio.com/Favorite").child(usermain.getUserid());

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        Favorite id = postSnapshot.getValue(Favorite.class);

                        if(user.getUserid().equals(id.getId())){
                            favorite.setBackgroundResource(R.drawable.favorite_enabled);
                            favorited = true;
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR : " + e);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void onCreateTeacherProfile(TabHost host) {

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.profile_tabOverview);
        spec.setIndicator("Overview");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.profile_tabExperience);
        spec.setIndicator("Experience");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.profile_tabBackground);
        spec.setIndicator("Background");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.profile_tabSkills);
        spec.setIndicator("Skills");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Tab Five");
        spec.setContent(R.id.profile_tabInterests);
        spec.setIndicator("Interests");
        host.addTab(spec);

        //Tab 6
        spec = host.newTabSpec("Tab Six");
        spec.setContent(R.id.profile_tabComments);
        spec.setIndicator("Comments");
        host.addTab(spec);
    }

    private void onCreateStudentProfile(TabHost host) {

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.profile_tabStrengths);
        spec.setIndicator("Strengths");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.profile_tabWeaknesses);
        spec.setIndicator("Weaknesses");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.profile_tabInterests);
        spec.setIndicator("Interests");
        host.addTab(spec);
    }

    private void onCreateStudentParentProfile(TabHost host) {

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.profile_tabOverview);
        spec.setIndicator("Overview");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.profile_tabInterests);
        spec.setIndicator("Interests");
        host.addTab(spec);
    }

    private void updateEndorsements(TeacherSkills ts, boolean[] endorsed) {

        if (ts == null || endorsed == null)
            return;

        Firebase updateEndorsements = new Firebase("https://teacherimpact.firebaseio.com/endorsements");
        updateEndorsements = updateEndorsements.child(user.getUserid()).child(userid.getId());

        ArrayList<String> endorsedSkills = new ArrayList<>();

        int i = 0;
        for (boolean value : endorsed) {
            if (value)
                endorsedSkills.add(ts.getSkills()[i]);
            i++;
        }

        try {
            if (endorsedSkills.isEmpty())
                updateEndorsements.removeValue();
            else
                updateEndorsements.setValue(endorsedSkills);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateSkillsToDataBase(TeacherSkills ts) {

        if (ts == null)
            return;

        Firebase updateSkills = new Firebase("https://teacherimpact.firebaseio.com/skills");
        updateSkills = updateSkills.child(user.getUserid());

        Map<String, Object> map = new HashMap<>();
        map.put("skills", ts.getSkills());
        map.put("values", ts.getValues());

        try {
            updateSkills.setValue(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDetach() {

        if (viewingOtherUser) {
            if (user.getRole().equals(TEACHER) && skillsAdapter != null) {
                updateEndorsements(skillsAdapter.teacherSkills, skillsAdapter.isEndorsed);
                updateSkillsToDataBase(skillsAdapter.teacherSkills);

            }
        }
        super.onDetach();
    }
}