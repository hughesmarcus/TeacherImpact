package com.teacherimpact.teacherimpact.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.teacherimpact.teacherimpact.DataTransferObjects.GradeLevel;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.R;

import java.util.HashMap;
import java.util.Map;


public class FragmentUpdateProfile extends Fragment {

    private static final String USER = "User";
    private static final String USER_ID = "User_ID";
    private static final String USER_SKILLS = "User_Skills";
    private static final String USER_COMMENTS = "User_Comments";
    private static final String IS_REGISTER = "Is_Register";
    private static final String TEACHER = "Teacher";
    private static final String STUDENT = "Student";
    private static final String STUDENTPARENT = "StudentParent";

    private User user;
    private Userid userid;
    private TeacherSkills teacherSkills;
    boolean isRegisterActivity = false;

    public FragmentUpdateProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = getArguments().getParcelable(USER);
            this.userid = getArguments().getParcelable(USER_ID);
            this.teacherSkills = getArguments().getParcelable(USER_SKILLS);
            this.isRegisterActivity = getArguments().getBoolean(IS_REGISTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_profile, container, false);
        Button update , picture;

        if (teacherSkills == null){
            teacherSkills = new TeacherSkills();
            teacherSkills.setSkills(new String[]{""});
            teacherSkills.setValues(new int[]{});
        }

        if (user != null) {

            switch (user.getRole()){
                case TEACHER:
                    view = setTeacherFieldsAndContent(view, user);
                    break;
                case STUDENT:
                    view = setStudentFieldsAndContent(view, user);
                    break;
                case STUDENTPARENT:
                    view = setStudentParentFieldsAndContent(view, user);
                    break;
                default:
                    break;
            }
        }

        final View viewHolder = view;
        picture = (Button) view.findViewById(R.id.Update_image);
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // System.out.println("kjdfjkjsdfgjdfg");
                 Fragment newFrag = new UpdateImage();
                Bundle bundle = new Bundle();
                 bundle.putParcelable(USER, user);
                 bundle.putParcelable(USER_ID, userid);
                   bundle.putParcelable(USER_SKILLS, teacherSkills);
                  newFrag.setArguments(bundle);
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.container, newFrag);
                 transaction.commit();
            }
        });

        update = (Button) view.findViewById(R.id.profile_update_button);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updateInfo(viewHolder);

                if (validateSkills(viewHolder, user)) {
                    //onValidateSuccess(user);
                } else {
                    onValidateFailed();
                    return;
                }

                if (!isRegisterActivity)
                    updateDatabase(user);

                Fragment newFrag = new FragmentProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelable(USER, user);
                bundle.putParcelable(USER_ID, userid);
                bundle.putParcelable(USER_SKILLS, teacherSkills);
                newFrag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFrag);
                transaction.commit();
            }
        });


        return view;
    }

    private int setGradeLevel(User u){

        String role = u.getGrade();

        switch (role){
            case "Kintergarden": return 0;
            case "First": return 1;
            case "Second": return 2;
            case "Third": return 3;
            case "Fourth": return 4;
            case "Fifth": return 5;
            case "Sixth": return 6;
            case "Seventh": return 7;
            case "Eighth": return 8;
            case "Ninth": return 9;
            case "Tenth": return 10;
            case "Eleventh": return 11;
            case "Twelfth": return 12;
            case "Other": return 13;
            default: return 0;
        }
    }

    private String getGradeLevel(int position){

        switch (position){
            case 0: return "Kintergarden";
            case 1: return "First";
            case 2: return "Second";
            case 3: return "Third";
            case 4: return "Fourth";
            case 5: return "Fifth";
            case 6: return "Sixth;";
            case 7: return "Seventh";
            case 8: return "Eighth";
            case 9: return "Ninth";
            case 10: return "Tenth";
            case 11: return "Eleventh";
            case 12: return "Twelfth";
            case 13: return "Other";
            default: return "";
        }
    }

    private View setTeacherFieldsAndContent(View view, User user){

        EditText et;

        view.findViewById(R.id.update_tp_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_teacher_philosophy);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getTeachingphilosophy());

        view.findViewById(R.id.update_cb_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_career_background);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getBackground());

        view.findViewById(R.id.update_ex_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_experience);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getExperience());

        //Not allowing users to update skills at the moment.
        view.findViewById(R.id.update_skills_label).setVisibility(View.GONE);
        et = (EditText)view.findViewById(R.id.update_skills);
        et.setVisibility(View.GONE);
        et.setText(getUserSkills(teacherSkills.getSkills()));

        view.findViewById(R.id.update_cert_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_certifications);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getCertifications());

        view.findViewById(R.id.update_awd_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_awards);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getAwards());

        view.findViewById(R.id.update_interests_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_interests);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getInterests());

        return view;
    }

    private View setStudentFieldsAndContent(View view, User user){

        EditText et;
        Spinner gradeSpinner;

        view.findViewById(R.id.update_grade_label).setVisibility(View.VISIBLE);
        gradeSpinner = (Spinner)view.findViewById(R.id.update_grade_level);
        gradeSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GradeLevel.values()));
        gradeSpinner.setVisibility(View.VISIBLE);
        gradeSpinner.setSelection(setGradeLevel(user));

        view.findViewById(R.id.update_learning_strengths_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_learning_strengths);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getLstrengths());

        view.findViewById(R.id.update_learning_weaknesses_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_learning_weaknesses);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getLweaknesses());

        view.findViewById(R.id.update_interests_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_interests);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getInterests());

        return view;
    }

    private View setStudentParentFieldsAndContent(View view, User user){

        EditText et;

        view.findViewById(R.id.update_bio_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_biography);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getBiography());

        view.findViewById(R.id.update_interests_label).setVisibility(View.VISIBLE);
        et = (EditText)view.findViewById(R.id.update_interests);
        et.setVisibility(View.VISIBLE);
        et.setText(user.getInterests());

        return view;
    }

    private View updateInfo(View view){

        EditText et;
        Spinner gradeSpinner;

        //General Information
        et = (EditText)view.findViewById(R.id.update_interests);
        user.setInterests(et.getText().toString());

        //Teacher Information
        et = (EditText)view.findViewById(R.id.update_teacher_philosophy);
        user.setTeachingphilosophy(et.getText().toString());

        et = (EditText)view.findViewById(R.id.update_career_background);
        user.setBackground(et.getText().toString());

        et = (EditText)view.findViewById(R.id.update_experience);
        user.setExperience(et.getText().toString());

        et = (EditText)view.findViewById(R.id.update_skills);
        teacherSkills.setSkills(setUserSkills(et.getText().toString()));

        et = (EditText)view.findViewById(R.id.update_certifications);
        user.setCertifications(et.getText().toString());

        et = (EditText)view.findViewById(R.id.update_awards);
        user.setAwards(et.getText().toString());

        //Student Information
        gradeSpinner = (Spinner)view.findViewById(R.id.update_grade_level);
        user.setGrade(getGradeLevel(gradeSpinner.getSelectedItemPosition()));

        et = (EditText)view.findViewById(R.id.update_learning_strengths);
        user.setLstrengths(et.getText().toString());

        et = (EditText)view.findViewById(R.id.update_learning_weaknesses);
        user.setLweaknesses(et.getText().toString());

        //Parent Information
        et = (EditText)view.findViewById(R.id.update_biography);
        user.setBiography(et.getText().toString());

        return view;
    }

    private String getUserSkills(String[] userSkills){
        String result = "";
        int length = userSkills.length;
        int cursor = 0;

        if (length == 1 && userSkills[0].equals(""))
            return result;

        for (String skill : userSkills){

            result += skill;
            cursor++;
            if (cursor != length)
                result += ", ";
        }
        return result;
    }

    private String[] setUserSkills(String userSkills){
        return userSkills.trim().split(",");
    }

    private boolean validateSkills(View view, User user){

        EditText et;

        if (user.getRole().equals(TEACHER)){

            et = (EditText)view.findViewById(R.id.update_skills);
            if (et.getText().toString().equals("")){
                et.setError("Must specify at least one skill. Separate skills with a comma.");
                return false;
            }
        }

        return true;
    }

    private void onValidateSuccess(User user){

       /* String[] userSkills = user.getInstructionalskills();
        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
        ref = ref.child("skills").child(user.getUserid());
        String[] skills = user.getInstructionalskills();

        ref.setValue(userSkills);*/
    }

    private void onValidateFailed(){
        Toast.makeText(getActivity(), "Error updating Skills", Toast.LENGTH_LONG).show();
    }

    private void updateDatabase(User user){

        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com/users").child(userid.getId());

        Map<String, Object> map = new HashMap<>();

        if (user.getRole().equals(TEACHER)) {

            map.put("teachingphilosophy", user.getTeachingphilosophy());
            map.put("certifications", user.getCertifications());
            map.put("awards", user.getAwards());
            map.put("background", user.getBackground());
            map.put("experience", user.getExperience());
            map.put("interests", user.getInterests());
        }

        if (user.getRole().equals(STUDENT)) {

            map.put("grade", user.getGrade());
            map.put("lstrengths", user.getLstrengths());
            map.put("lweaknesses", user.getLweaknesses());
            map.put("interests", user.getInterests());
        }

        if (user.getRole().equals(STUDENTPARENT)) {

            map.put("biography", user.getBiography());
            map.put("interests", user.getInterests());
        }
        ref.updateChildren(map);
    }
}
