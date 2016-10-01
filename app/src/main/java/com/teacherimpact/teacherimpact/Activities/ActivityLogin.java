package com.teacherimpact.teacherimpact.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.teacherimpact.teacherimpact.DataTransferObjects.CommentContain;
import com.teacherimpact.teacherimpact.DataTransferObjects.Comment;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.R;

public class ActivityLogin extends AppCompatActivity {

    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String USERMAIN = "User_Main";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";

    String[] values;

    //User input fields
    private TextView email;
    private TextView password;

    //Can focus on an element
    private View focusView;

    //Firebase references
    private Firebase ref;

    //Data Transfer Objects
    private User user, usermain;
    private TeacherSkills teacherSkills;
    public CommentContain commentA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        //Clickable text fields
        TextView forgotPassword;
        TextView aboutUs;

        //Signs user in
        Button signIn;
        Button register;

        //Temporary strings for a quick Sign In
        String Email = "willajos@mail.gvsu.edu";
        String Password = "password";

        signIn = (Button) findViewById(R.id.login_buttonSignIn);
        focusView = null;
        ref = new Firebase("https://teacherimpact.firebaseio.com");

        //Sets the email to temp email
        email = (TextView) findViewById(R.id.login_inputEmail);
        if (email != null)
            email.setText(Email);

        //Sets the password to temp password
        password = (TextView) findViewById(R.id.login_inputPassword);
        if (password != null)
            password.setText(Password);

        aboutUs = (TextView) findViewById(R.id.login_aboutUs);
        if (aboutUs != null) {
            aboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityLogin.this, ActivityAbout.class);
                    startActivity(intent);
                }
            });
        }

        //Makes register textView clickable
        register = (Button) findViewById(R.id.login_buttonSignUp);
        if (register != null) {
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    startActivity(intent); //Do not close this activity just yet.
                }
            });
        }

        //Makes forgot password textView clickable
        forgotPassword = (TextView) findViewById(R.id.login_textForgotPassword);
        if (forgotPassword != null) {
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                    startActivity(intent);
                }
            });
        }

        //Overrides keyboard enter
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    checkValid(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void checkValid(View view) {
        if (checkValidEmail()) {
            if (checkValidPassword()) {
                Login(email.getText().toString(), password.getText().toString());
            } else {
                focusView = password;
                focusView.requestFocus();
                Toast.makeText(ActivityLogin.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            focusView = email;
            focusView.requestFocus();
            Toast.makeText(ActivityLogin.this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkValidEmail() {
        return !email.getText().toString().isEmpty() && email.getText().toString().contains("@");
    }

    public boolean checkValidPassword() {
        return !password.getText().toString().isEmpty() && password.getText().toString().length() > 5;
    }

    public void Login(String Username, String Password) {
        ref.authWithPassword(Username, Password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Userid userid = new Userid(authData.getUid());
                loadUser(userid);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(ActivityLogin.this, "Sign In Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUser(final Userid id) {

        Firebase userRef = new Firebase("https://teacherimpact.firebaseio.com/users");

        userRef = userRef.child(id.getId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                usermain = dataSnapshot.getValue(User.class);

                if (user != null && user.getRole().equals("Teacher")) {
                    loadSkills(id);
                } else {
                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                    intent.putExtra(USER, user);
                    intent.putExtra(USERMAIN, usermain);
                    intent.putExtra(USER_ID, id);
                    intent.putExtra(VIEWING_FLAG, true);
                    intent.putExtra("comments", commentA);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void loadSkills(final Userid id) {

        Firebase skillRef = new Firebase("https://teacherimpact.firebaseio.com/skills").child(id.getId());

        skillRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Load Teacher Skills
                teacherSkills = dataSnapshot.getValue(TeacherSkills.class);
                loadComments(id);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void loadComments(final Userid id) {

        Firebase commentref = new Firebase("https://teacherimpact.firebaseio.com/Comment");
        commentref = commentref.child(user.getUserid());

        commentref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;
                String[] sender = new String[(int) dataSnapshot.getChildrenCount()];
                values = new String[(int) dataSnapshot.getChildrenCount()];
                String[] names = new String[(int) dataSnapshot.getChildrenCount()];
                String[] flags = new String[(int) dataSnapshot.getChildrenCount()];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment teachercomments;
                    teachercomments = postSnapshot.getValue(Comment.class);

                    sender[i] = teachercomments.getSender();
                    values[i] = teachercomments.getComment();
                    names[i] = teachercomments.getName();
                    flags[i] = teachercomments.getViewflag();
                    i++;
                }
                commentA = new CommentContain(values, sender, names, flags);
                loadActivity(id);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void loadActivity(Userid id) {
        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
        intent.putExtra(USER, user);
        intent.putExtra(USERMAIN, usermain);
        intent.putExtra(USER_ID, id);
        intent.putExtra(VIEWING_FLAG, true);
        intent.putExtra(USER_SKILLS, teacherSkills);
        intent.putExtra(USER_COMMENTS, commentA);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showDialog(this, "EXIT APPLICATION", "Exit Teacher IMPACT?");
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
        finish();
    }
}