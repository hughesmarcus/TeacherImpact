package com.teacherimpact.teacherimpact.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.teacherimpact.teacherimpact.Activities.ActivityMain;
import com.teacherimpact.teacherimpact.DataTransferObjects.Comment;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.Favorite;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.ListAdapters.FavoritesListAdapter;
import com.teacherimpact.teacherimpact.R;
import java.util.ArrayList;

public class FragmentFavorites extends Fragment{

    private static final String USER = "User";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String USER_ID = "User_ID";
    private static final String ENDORSEMENTS = "Endorsements";
    private static final String TEACHER = "Teacher";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USERMAIN = "User_Main";

    private View view;

    private ArrayList<User> users;
    private ArrayList<User> favoriteUsers;
    private ArrayList<Favorite> favoriteIds;

    private TeacherSkills teacherSkills;
    private String[] endorsedSkills;
    private CommentContain comments;

    private User usermain;
    private Userid userid;

    public FragmentFavorites() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = getArguments().getParcelable(USER_ID);
        usermain = getArguments().getParcelable(USERMAIN);
        users = new ArrayList<>();
        favoriteUsers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_favorites, container, false);
        users = new ArrayList<>();
        favoriteUsers = new ArrayList<>();

        if(this.isAdded())
            loadFavoriteIds();

        return view;
    }

    private void loadFavoriteIds(){
        Firebase favoriteRef = new Firebase("https://teacherimpact.firebaseio.com/Favorite").child(usermain.getUserid());
        favoriteIds = new ArrayList<>();

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        Favorite favorite = postSnapshot.getValue(Favorite.class);
                        favoriteIds.add(favorite);
                    } catch (Exception e) {
                        System.out.println("ERROR : " + e);
                    }
                }
                loadUsers();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void loadUsers() {
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
                        System.out.println("ERROR : " + e);
                    }
                }
                filterUsers();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());}
        });
    }

    private void filterUsers(){
        for(User user : users){
            for(Favorite id : favoriteIds){
                if(user.getUserid().equals(id.getId())){
                    favoriteUsers.add(user);
                }
            }
        }
        listSetup();
    }

    private void listSetup(){
        FavoritesListAdapter adapter = new FavoritesListAdapter(getActivity(), favoriteUsers);

        ListView list = (ListView) view.findViewById(R.id.listViewFavorites);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = favoriteUsers.get(+position);

                ((ActivityMain) getActivity()).setTitle(getString(R.string.profileOther));

                if (u.getRole().equals(TEACHER)) {
                    loadSkills(u, usermain.getUserid());
                } else {
                    loadFragment(u);
                }
            }
        });
    }

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
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());}
        });
    }

    private void loadEndorsements(final User user, final String id){
        Firebase endorseRef = new Firebase("https://teacherimpact.firebaseio.com/endorsements").child(user.getUserid()).child(id);
        endorseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                endorsedSkills = dataSnapshot.getValue(String[].class);
                loadComments(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());}
        });

    }

    private void loadComments(final User user){
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
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void loadFragment(User user){
        Fragment newFrag = new FragmentProfile();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        bundle.putParcelable(USERMAIN, usermain);
        bundle.putParcelable(USER_ID, userid);
        bundle.putParcelable(USER_SKILLS, teacherSkills);
        bundle.putStringArray(ENDORSEMENTS, endorsedSkills);
        bundle.putParcelable(USER_COMMENTS, comments);
        bundle.putBoolean(VIEWING_FLAG, true);

        newFrag.setArguments(bundle);

        transaction.replace(R.id.container, newFrag);
        transaction.commit();
    }
}
