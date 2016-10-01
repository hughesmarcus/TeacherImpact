package com.teacherimpact.teacherimpact.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.teacherimpact.teacherimpact.DataTransferObjects.Comment;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.Fragments.FragmentAbout;
import com.teacherimpact.teacherimpact.Fragments.FragmentHome;
import com.teacherimpact.teacherimpact.Fragments.FragmentProfile;
import com.teacherimpact.teacherimpact.Fragments.FragmentSearch;
import com.teacherimpact.teacherimpact.Fragments.NavigationDrawerFragment;
import com.teacherimpact.teacherimpact.Fragments.FragmentFavorites;
import com.teacherimpact.teacherimpact.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class ActivityMain extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    //Navigation tags
    private static final String LOG_TAG = "MD/ActivityMain";
    private static final String KEY_DRAWERPOS = "DrawerPosition";

    //Parcelable tags
    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String USER_ENDORSEMENTS = "Endorsements";
    private static final String TEACHER = "Teacher";
    private static final String USERMAIN = "User_Main";

    //Data Transfer Objects & Constants
    private User user;
    private User usermain;
    private Userid userid;
    private TeacherSkills teacherSkills;
    private String[] endorsedSkills;
    private CommentContain comments;

    //Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // States the last selected position of the drawer
    private int mDrawerPosition = -1;

    // The Actionbar-replacement Toolbar that runs along the top of the screen
    Toolbar mToolbar;
    // Titles of all the drawer items' toolbar titles
    String mTitles[];

    //Variables related to search
    private MenuItem searchAction;
    private MenuItem filterAction;
    private boolean isSearchOpen = false;
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getSearchTerm() {
        ActionBar action = getSupportActionBar();
        assert action != null;
        return ((EditText) action.getCustomView().findViewById(R.id.action_inputSearch)).getText().toString();
    }

    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(getApplicationContext());

        boolean isViewingOther;

        user = getIntent().getParcelableExtra(USER);
        usermain = getIntent().getParcelableExtra(USERMAIN);
        userid = getIntent().getParcelableExtra(USER_ID);
        teacherSkills = getIntent().getParcelableExtra(USER_SKILLS);
        comments = getIntent().getParcelableExtra(USER_COMMENTS);
        isViewingOther = getIntent().getBooleanExtra(VIEWING_FLAG, false);

        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(Color.WHITE);
            mToolbar.setNavigationIcon(R.drawable.search_icon);
            setSupportActionBar(mToolbar);
        }

        // Initialize the drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(user, R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the titles for the Toolbar
        mTitles = getResources().getStringArray(R.array.drawer_items);


        mDrawerPosition = -1;
        if (savedInstanceState == null) {
            // If there was no saved position, then the default, starting position should be used
            forceChangeItemSelected(0);
        } else {
            // Otherwise, get the saved position from the bundle
            mDrawerPosition = savedInstanceState.getInt(KEY_DRAWERPOS);
            mNavigationDrawerFragment.setSelectedItem(mDrawerPosition);
            // Title needs to be re-set
            getSupportActionBar().setTitle(mTitles[mDrawerPosition]);
        }

        if (isViewingOther && user != null & userid != null) {
            if (user.getRole().equals(TEACHER))
                loadEndorsements(user, userid);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment newFrag = new FragmentHome();
        Bundle b = new Bundle();
        b.putParcelable(USER, user);
        newFrag.setArguments(b);
        transaction.replace(R.id.container, newFrag);
        transaction.commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Update the content, title, and everything else as necessary
        changeItemSelected(position);
    }

    // Changes both the drawer position as well as the content frag position
    private void forceChangeItemSelected(int position) {
        mNavigationDrawerFragment.setSelectedItem(position);
        changeItemSelected(position);
    }

    // Changes the item selected to display
    private void changeItemSelected(int newPos) {

        // If newPos is -1, there was an error. Reset newPos to 0.
        if (newPos == -1) {
            Log.e(LOG_TAG, "changeItemSelected(pos) was given -1. Fixing issue for now");
            newPos = 0;
        }

        // First, update the main content by replacing fragments
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment newFrag = new FragmentHome();

        //-> Choosing which fragment to show logic
        switch (newPos) {
            case 1:
                //Home Fragment
                switchTitleSearch();
                newFrag = new FragmentHome();
                Bundle homeBundle = new Bundle();
                homeBundle.putParcelable(USER, user);
                newFrag.setArguments(homeBundle);

                break;
            case 2:
                //My Profile Fragment
                switchTitleSearch();
                loadComments();
                newFrag = new FragmentProfile();
                Bundle profileBundle = new Bundle();
                profileBundle.putParcelable(USER, user);
                profileBundle.putParcelable(USERMAIN, usermain);
                profileBundle.putParcelable(USER_ID, userid);
                profileBundle.putParcelable(USER_SKILLS, teacherSkills);
                profileBundle.putStringArray(USER_ENDORSEMENTS, endorsedSkills);
                profileBundle.putParcelable(USER_COMMENTS, comments);
                newFrag.setArguments(profileBundle);
                break;

            case 3:
                //Favorites Fragment
                switchTitleSearch();
                newFrag = new FragmentFavorites();
                Bundle favoriteBundle = new Bundle();
                favoriteBundle.putParcelable(USER, user);
                favoriteBundle.putParcelable(USERMAIN, usermain);
                favoriteBundle.putParcelable(USER_ID, userid);
                favoriteBundle.putParcelable(USER_SKILLS, teacherSkills);
                favoriteBundle.putStringArray(USER_ENDORSEMENTS, endorsedSkills);
                favoriteBundle.putParcelable(USER_COMMENTS, comments);
                newFrag.setArguments(favoriteBundle);

                break;
            case 4:
                //About Fragment
                switchTitleSearch();
                newFrag = new FragmentAbout();
                break;
            case 5:
                //Logout
                showDialog(this, "LOGOUT", "Return to the login screen?");
                break;
            default:
                // OTHERWISE, there was a big mistake
                switchTitleSearch();
                Log.e(LOG_TAG, "changeItemSelected(pos: " + newPos + "): Invalid position");
                return;
        }

        //-> Choosing which animations to use logic
        int transitionIn, transitionOut;
        if (mDrawerPosition == -1) {
            // If this is the first fragment being added - one way or another - use no transitions
            transitionIn = transitionOut = R.animator.do_nothing;
        } else if (mDrawerPosition < newPos) {
            // The new item is entering from below, and the old is moving out to above
            transitionIn = R.animator.slide_in_frombottom;
            transitionOut = R.animator.slide_out_totop;
        } else {
            // Otherwise, new item is entering from above and old is moving out to below
            transitionIn = R.animator.slide_in_fromtop;
            transitionOut = R.animator.slide_out_tobottom;
        }

        transaction.setCustomAnimations(transitionIn, transitionOut);
        transaction.replace(R.id.container, newFrag);
        transaction.commit();

        // Set the toolbar title to the correct title
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitles[newPos - 1]);

        // Finally, save that this was the latest position set
        mDrawerPosition = newPos;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        LinearLayout filters;

        int id = item.getItemId();

        switch (id) {
            case R.id.save:
                Log.e(LOG_TAG, "onOptionsItemSelected: Save was selected!");
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
            case R.id.action_filter:
                filters = (LinearLayout) findViewById(R.id.searchFilterLayout);
                if (filters != null) {
                    if (filters.getVisibility() == View.VISIBLE)
                        filters.setVisibility(View.GONE);
                    else
                        filters.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // Store the last selected fragment position
        outState.putInt(KEY_DRAWERPOS, mDrawerPosition);

        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void loadEndorsements(User user, Userid id) {

        Firebase endorseRef = new Firebase("https://teacherimpact.firebaseio.com/endorsements").child(user.getUserid()).child(id.getId());
        endorseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                endorsedSkills = dataSnapshot.getValue(String[].class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadComments() {

        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());

        commentref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                String[] sender = new String[(int) dataSnapshot.getChildrenCount()];
                String[] values = new String[(int) dataSnapshot.getChildrenCount()];
                String[] names = new String[(int) dataSnapshot.getChildrenCount()];
                String[] flags = new String[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment teachercomments;
                    teachercomments = postSnapshot.getValue(Comment.class);
                    sender[i] = teachercomments.getSender();
                    names[i] = teachercomments.getName();
                    values[i] = teachercomments.getComment();
                    flags[i] = teachercomments.getViewflag();
                    i++;
                }
                comments = new CommentContain(values, sender, names, flags);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        filterAction = menu.findItem(R.id.action_filter);
        return super.onPrepareOptionsMenu(menu);
    }

    public MenuItem getFilterAction() {
        return filterAction;
    }

    public void switchTitleSearch() {
        ActionBar action = getSupportActionBar();

        if (action != null) {
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);
        }
        if (searchAction == null) {
            return;
        }
        // Add the search icon in the action bar
        searchAction.setIcon(android.R.drawable.ic_menu_search);

        hideKeyboard();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        isSearchOpen = false;
    }

    protected void handleMenuSearch() {
        final EditText searchInput;
        ActionBar action = getSupportActionBar();

        if (isSearchOpen) {
            switchTitleSearch();
        } else {
            if (action != null) {

                action.setDisplayShowCustomEnabled(true);
                action.setCustomView(R.layout.toolbar_search);
                action.setDisplayShowTitleEnabled(false);
                searchInput = (EditText) action.getCustomView().findViewById(R.id.action_inputSearch);

                //this is a listener to do a search when the user clicks on search button
                searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // Add the search icon in the action bar
                            searchAction.setIcon(android.R.drawable.ic_menu_search);

                            hideKeyboard();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                            isSearchOpen = false;

                            if (searchInput.getText().length() == 0) {
                                setTitle(R.string.search);
                                switchTitleSearch();
                            }

                            loadUsers();
                            return true;
                        }

                        // Open the keyboard focused in the edtSearch
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
                        return false;
                    }
                });

                searchInput.requestFocus();

                // Open the keyboard focused in the edtSearch
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);

                // Add the close icon
                searchAction.setIcon(android.R.drawable.ic_menu_close_clear_cancel);

                isSearchOpen = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        //Check if any Fragments are on Stack before calling super.OnBackPressed()
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {

            //Check if Search is open before calling super.onBackPressed()
            if (isSearchOpen) {
                handleMenuSearch();
                return;
            }

            showDialog(this, "LOGOUT", "Return to the login screen?");
        }
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout(dialog);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logout(DialogInterface builder) {
        builder.dismiss();
        Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    private FragmentSearch fragSearch = new FragmentSearch();

    public void setupSearch() {
        if (fragSearch.isOpen()) {
            fragSearch.search();
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putParcelable(USER_ID, userid);
            bundle.putParcelable(USERMAIN, usermain);

            fragSearch.setArguments(bundle);
            transaction.replace(R.id.container, fragSearch);
            transaction.commit();
        }
    }

    public void loadUsers() {

        Firebase userRef = new Firebase("https://teacherimpact.firebaseio.com/users");
        users = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        User u = postSnapshot.getValue(User.class);
                        if (!u.getUserid().equals(usermain.getUserid())) {
                            users.add(u);
                        }
                    } catch (Exception e) {
                        System.out.println("LOG : " + e);
                    }
                }
                setupSearch();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

  public  CommentContain commentdelete(final String comment, final String name) {
        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());

        commentref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Comment teachercomments;

                    teachercomments = postSnapshot.getValue(Comment.class);

                    if (teachercomments.getComment().equals(comment) && teachercomments.getName().equals(name)) {

                        postSnapshot.getRef().removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        loadComments();
        return comments;
    }

    public void setView(final String comment, final String name ,final String flag) {
        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());

        commentref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Comment teachercomments;
                    Map<String, Object> flags = new HashMap<String, Object>();
                    teachercomments = postSnapshot.getValue(Comment.class);

                    if (teachercomments.getComment().equals(comment) && teachercomments.getName().equals(name)) {
                       flags.put("viewflag", flag);
                      postSnapshot.getRef().updateChildren(flags);
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        loadComments();

    }
}