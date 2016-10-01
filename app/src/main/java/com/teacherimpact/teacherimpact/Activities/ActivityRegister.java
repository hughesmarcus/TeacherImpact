package com.teacherimpact.teacherimpact.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.teacherimpact.teacherimpact.DataTransferObjects.Gender;
import com.teacherimpact.teacherimpact.DataTransferObjects.GradeLevel;
import com.teacherimpact.teacherimpact.DataTransferObjects.State;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.R;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegister extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    //Base User data strings
    String name, email;
    String gender, birthday, password;
    String city, county, state;
    String zip;
    String role;

    private static final String USER = "User";
    private static final String USERMAIN = "User_Main";
    private static final String USER_ID = "User_ID";
    private static final String VIEWING_FLAG = "Viewing_Flag";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String TEACHER = "Teacher";
    private static final String STUDENT = "Student";
    private static final String STUDENTPARENT = "StudentParent";

    private Button register;
    private EditText et;
    User user, usermain;
    Userid userid;
    TeacherSkills teacherSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);
        Spinner sp;

        //Stop keyboard from opening on load
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sp = (Spinner)findViewById(R.id.register_inputGender);
        if (sp != null)
            sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Gender.values()));

        sp = (Spinner)findViewById(R.id.register_inputState);
        State stateList = new State();
        if (sp != null)
            sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stateList.getArrayList()));

        sp = (Spinner)findViewById(R.id.register_grade);
        if (sp != null)
            sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, GradeLevel.values()));

        register = (Button) findViewById(R.id.register_buttonRegister);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.teacher:
                if (checked)
                    role = TEACHER;
                showTeacherFields();
                break;
            case R.id.student:
                if (checked)
                    role = STUDENT;
                showStudentFields();
                break;
            case R.id.parent:
                if (checked)
                    role = STUDENTPARENT;
                showStudentParentFields();
                break;
        }
        findViewById(R.id.register_role_error_message).setVisibility(View.GONE);
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    private void showTeacherFields(){
        EditText et;

        findViewById(R.id.register_profile_info_label).setVisibility(View.VISIBLE);
        findViewById(R.id.register_teachingphilosophy).setVisibility(View.VISIBLE);
        findViewById(R.id.register_background).setVisibility(View.VISIBLE);
        findViewById(R.id.register_experience).setVisibility(View.VISIBLE);
        findViewById(R.id.register_skills).setVisibility(View.VISIBLE);
        findViewById(R.id.register_certifications).setVisibility(View.VISIBLE);
        findViewById(R.id.register_awards).setVisibility(View.VISIBLE);
        findViewById(R.id.register_grade_label).setVisibility(View.GONE);
        findViewById(R.id.register_grade).setVisibility(View.GONE);
        findViewById(R.id.register_strengths).setVisibility(View.GONE);
        findViewById(R.id.register_weaknesses).setVisibility(View.GONE);
        findViewById(R.id.register_biography).setVisibility(View.GONE);
        findViewById(R.id.register_interests).setVisibility(View.VISIBLE);

    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    private void showStudentFields(){

        findViewById(R.id.register_profile_info_label).setVisibility(View.VISIBLE);
        findViewById(R.id.register_teachingphilosophy).setVisibility(View.GONE);
        findViewById(R.id.register_background).setVisibility(View.GONE);
        findViewById(R.id.register_experience).setVisibility(View.GONE);
        findViewById(R.id.register_skills).setVisibility(View.GONE);
        findViewById(R.id.register_certifications).setVisibility(View.GONE);
        findViewById(R.id.register_awards).setVisibility(View.GONE);
        findViewById(R.id.register_grade_label).setVisibility(View.VISIBLE);
        findViewById(R.id.register_grade).setVisibility(View.VISIBLE);
        findViewById(R.id.register_strengths).setVisibility(View.VISIBLE);
        findViewById(R.id.register_weaknesses).setVisibility(View.VISIBLE);
        findViewById(R.id.register_biography).setVisibility(View.GONE);
        findViewById(R.id.register_interests).setVisibility(View.VISIBLE);
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    private void showStudentParentFields(){

        findViewById(R.id.register_profile_info_label).setVisibility(View.VISIBLE);
        findViewById(R.id.register_teachingphilosophy).setVisibility(View.GONE);
        findViewById(R.id.register_background).setVisibility(View.GONE);
        findViewById(R.id.register_experience).setVisibility(View.GONE);
        findViewById(R.id.register_skills).setVisibility(View.GONE);
        findViewById(R.id.register_certifications).setVisibility(View.GONE);
        findViewById(R.id.register_awards).setVisibility(View.GONE);
        findViewById(R.id.register_grade_label).setVisibility(View.GONE);
        findViewById(R.id.register_grade).setVisibility(View.GONE);
        findViewById(R.id.register_strengths).setVisibility(View.GONE);
        findViewById(R.id.register_weaknesses).setVisibility(View.GONE);
        findViewById(R.id.register_biography).setVisibility(View.VISIBLE);
        findViewById(R.id.register_interests).setVisibility(View.VISIBLE);
    }

    public void CreateAccount(final String Username, final String Password, final String name) {

        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
        ref.createUser(Username, Password, new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public void onSuccess(Map<String, Object> result) {
                add(Username, Password, name);
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                System.out.print("An error with firebase occurred.");
            }
        });
    }

    public void add(String Username, String Password, final String name) {

        final Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
        ref.authWithPassword(Username, Password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());

                Map<String, String> map = new HashMap<>();
                map.put("email", authData.getProviderData().get("email").toString());
                map.put("password", authData.getProvider());
                if (authData.getProviderData().containsKey("displayName")) {
                    map.put("displayName", authData.getProviderData().get("displayName").toString());
                }
                userid = new Userid(authData.getUid());
                user.setUserid(userid.getId());

                //Basic User Info Setup
                map.put("userid", userid.getId());
                map.put("firstname", user.getFirstname());
                map.put("lastname", user.getLastname());
                map.put("birthdate", user.getBirthdate());
                map.put("gender", user.getGender());
                map.put("zip", user.getZip());
                map.put("county", user.getCounty());
                map.put("city", user.getCity());
                map.put("state", user.getState());
                map.put("role", user.getRole());
                map.put("interests", user.getInterests());

                if (user.getRole().equals(TEACHER)) {
                    //comment(authData.getUid());
                    map.put("teachingphilosophy", user.getTeachingphilosophy());
                    map.put("certifications", user.getCertifications());
                    map.put("awards", user.getAwards());
                    map.put("background", user.getBackground());
                    map.put("experience", user.getExperience());

                    if (teacherSkills != null) {
                        Map<String, Object> skillmap = new HashMap<>();
                        Map<String, Object> commap = new HashMap<>();
                        skillmap.put("skills", teacherSkills.getSkills());
                        skillmap.put("values", teacherSkills.getValues());
                        commap.put("comment", " ");
                        commap.put("name", " ");
                        commap.put("sender", " ");
                        commap.put("viewflag", "false");
                        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
                        Firebase refcom = ref;
                        refcom = ref.child("Comment").child(user.getUserid());
                        ref = ref.child("skills").child(user.getUserid());

                        ref.setValue(skillmap);
                        refcom.push().setValue(commap);
                    }
                }

                if (user.getRole().equals(STUDENT)) {

                    map.put("grade", user.getGrade());
                    map.put("lstrengths", user.getLstrengths());
                    map.put("lweaknesses", user.getLweaknesses());
                }

                if (user.getRole().equals(STUDENTPARENT)) {

                    map.put("biography", user.getBiography());
                }
                ref.child("users").child(authData.getUid()).setValue(map);
                onSignupSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }
        });
    }
    private void comment(String id){
        final Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
        ref.child("Comment").setValue(id);
    }
    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    public void signup() {

        Log.d(TAG, "Signup");

        et = (EditText)findViewById(R.id.register_inputName);
        name = et.getText().toString();

        et = (EditText)findViewById(R.id.register_inputEmail);
        email = et.getText().toString();

        et = (EditText)findViewById(R.id.register_inputPassword);
        password = et.getText().toString();

        if (!validate(name, email, password)) {
            onSignupFailed();
            return;
        }
        createUser();
        if (!validateSkills(user))
            return;

        CreateAccount(email, password, name);
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    private void createUser(){

        EditText et;
        Spinner sp;

        //Load up a User with empty values on all values not set.
        //Name, Email and Password already set.
        et = (EditText)findViewById(R.id.register_inputBirthday);
        birthday = et.getText().toString();

        sp = (Spinner)findViewById(R.id.register_inputGender);
        gender = sp.getSelectedItem().toString();

        et = (EditText)findViewById(R.id.register_inputCity);
        city = et.getText().toString();

        et = (EditText)findViewById(R.id.register_inputCounty);
        county = et.getText().toString();

        sp = (Spinner)findViewById(R.id.register_inputState);
        state = sp.getSelectedItem().toString();
        if (state == null)
            state = "";
        if (!state.isEmpty())
            state = state.substring(state.length() - 2, state.length());

        et = (EditText)findViewById(R.id.register_inputZipCode);
        zip = et.getText().toString();

        String[] parts = name.split(" ");
        String part1 = parts[0];
        String part2 = parts[1];

        user = new User();
        teacherSkills = new TeacherSkills();

        //Base fields
        user.setFirstname(part1);
        user.setLastname(part2);
        user.setEmail(email);
        user.setPassword(password);
        user.setBirthdate(birthday);
        user.setGender(gender);
        user.setRole(role);
        user.setZip(zip);
        user.setCounty(county);
        user.setCity(city);
        user.setState(state);
        user.setFavorited(false);

        et = (EditText)findViewById(R.id.register_interests);
        user.setInterests(et.getText().toString());


        //Teacher fields
        et = (EditText)findViewById(R.id.register_teachingphilosophy);
        user.setTeachingphilosophy(et.getText().toString());

        et = (EditText)findViewById(R.id.register_certifications);
        user.setCertifications(et.getText().toString());

        et = (EditText)findViewById(R.id.register_awards);
        user.setAwards(et.getText().toString());

        et = (EditText)findViewById(R.id.register_background);
        user.setBackground(et.getText().toString());

        et = (EditText)findViewById(R.id.register_experience);
        user.setExperience(et.getText().toString());

        et = (EditText)findViewById(R.id.register_skills);
        teacherSkills.setSkills(setUserSkills(et.getText().toString()));
        teacherSkills.setValues(new int[teacherSkills.getSkills().length]);

        //Student fields
        sp = (Spinner)findViewById(R.id.register_grade);
        user.setGrade(sp.getSelectedItem().toString());

        et = (EditText)findViewById(R.id.register_strengths);
        user.setLstrengths(et.getText().toString());

        et = (EditText)findViewById(R.id.register_weaknesses);
        user.setLweaknesses(et.getText().toString());

        //Parent fields
        et = (EditText)findViewById(R.id.register_biography);
        user.setBiography(et.getText().toString());

        usermain = user;
    }

    private String[] setUserSkills(String userSkills){
        String[] result = userSkills.trim().split(",");
        return result;
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    private boolean validateSkills(User user){

        EditText et;

        if (user.getRole().equals(TEACHER)){

            et = (EditText)findViewById(R.id.register_skills);
            if (et.getText().toString().equals("")){
                et.setError("Must specify at least one skill. Separate skills with a comma.");
                return false;
            }
        }

        return true;
    }

    public void onSignupSuccess() {

        register.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(ActivityRegister.this, ActivityMain.class);
        intent.putExtra(USER, user);
        intent.putExtra(USERMAIN, usermain);
        intent.putExtra(USER_ID, userid);
        intent.putExtra(VIEWING_FLAG, false);
        intent.putExtra(USER_SKILLS, teacherSkills);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {

        Toast.makeText(getBaseContext(), "Registration Failed - Missing Required Fields", Toast.LENGTH_LONG).show();
        register.setEnabled(true);
    }

    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    public boolean validate(String name, String email, String password) {

        boolean valid = true;
        RadioButton tb = (RadioButton) findViewById(R.id.teacher);
        RadioButton sb = (RadioButton) findViewById(R.id.student);
        RadioButton pb = (RadioButton) findViewById(R.id.parent);

        if (name.isEmpty() || name.length() < 3) {
            et = (EditText)findViewById(R.id.register_inputName);
            et.setError("Name must be at least 3 characters long.");
            valid = false;
        } else {
            et = (EditText)findViewById(R.id.register_inputName);
            et.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et = (EditText)findViewById(R.id.register_inputEmail);
            et.setError("Enter a valid email address.");
            valid = false;
        } else {
            et = (EditText)findViewById(R.id.register_inputEmail);
            et.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            et = (EditText)findViewById(R.id.register_inputPassword);
            et.setError("Password must be between 4 and 10 alphanumeric characters.");
            valid = false;
        } else {
            et = (EditText)findViewById(R.id.register_inputPassword);
            et.setError(null);
        }

        if (role == null || role.isEmpty() || (!tb.isChecked() && !sb.isChecked() && !pb.isChecked())) {
            findViewById(R.id.register_role_error_message).setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }
}
