package com.teacherimpact.teacherimpact.Fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


import com.firebase.client.Firebase;
import com.teacherimpact.teacherimpact.DataTransferObjects.TeacherSkills;
import com.teacherimpact.teacherimpact.DataTransferObjects.User;
import com.teacherimpact.teacherimpact.DataTransferObjects.Userid;
import com.teacherimpact.teacherimpact.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class UpdateImage extends Fragment {
    TextView textTargetUri;
    ImageView targetImage;
    String imageFile;
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

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
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
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.activity_activity_select_image, container, false);
        Button update;
        Button buttonLoadImage = (Button)view.findViewById(R.id.loadimage);
        textTargetUri = (TextView)view.findViewById(R.id.targeturi);
        targetImage = (ImageView)view.findViewById(R.id.targetimage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
        update = (Button) view.findViewById(R.id.profile_update_button);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               setpic();


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

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (resultCode == Activity.RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);

                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
               // if (bitmap != null && !bitmap.isRecycled()) {
                  //  bitmap.recycle();
               //     bitmap = null;
               // }
                byte[] byteArray = bYtE.toByteArray();
                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setpic() {
        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com/users").child(userid.getId());
        ref = ref.child("imageFile");

        ref.setValue(imageFile);
    }
}
