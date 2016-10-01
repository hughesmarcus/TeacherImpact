package com.teacherimpact.teacherimpact.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.teacherimpact.teacherimpact.R;

public class ActivityForgotPassword extends AppCompatActivity {

    private EditText email;
    private FrameLayout messageLayout;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.forgotpassword_inputEmail);
        messageLayout = (FrameLayout) findViewById(R.id.messageLayout);
        send = (Button) findViewById(R.id.forgotpassword_buttonSend);

        //Overrides keyboard enter
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendEmail(null);
                    return true;
                }
                return false;
            }
        });

        //Stop keyboard from opening on load
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void sendEmail(View view) {
        messageLayout.setVisibility(view.VISIBLE);
        send.setEnabled(false);
        hideKeyboard();
        Firebase ref = new Firebase("https://teacherimpact.firebaseio.com");
        ref.resetPassword(email.getText().toString().trim(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(FirebaseError firebaseError) {
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
}
