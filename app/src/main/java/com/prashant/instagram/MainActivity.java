package com.prashant.instagram;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener
 {

    EditText usernameText;
    EditText passwordText;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            login(v);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram");

        usernameText=findViewById(R.id.usernameTextView);
        passwordText=findViewById(R.id.passwordTextView);
        Button loginButton=findViewById(R.id.loginButton);
        Button signupButton=findViewById(R.id.signupButton);

        ImageView instagramImageView=findViewById(R.id.instagramImageView);
        ConstraintLayout constraintLayout=findViewById(R.id.constraintLayout);

        constraintLayout.setOnClickListener(this);

        passwordText.setOnKeyListener(this);

        if(ParseUser.getCurrentUser() != null){
            showUsersList();
        }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signup(View v){

        String uName=usernameText.getText().toString();
        String pass=passwordText.getText().toString();

        ParseUser user=new ParseUser();
        user.setUsername(uName);
        user.setPassword(pass);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null){

                    showUsersList();

                    Toast.makeText(getApplicationContext(),"Signup Successful",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Signup Unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public  void login(View v){

        String uName=usernameText.getText().toString();
        String pass=passwordText.getText().toString();

        ParseUser.logInInBackground(uName, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(e==null){

                    showUsersList();

                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void showUsersList(){
        Intent intent=new Intent(getApplicationContext(),UsersListActivity.class);
        startActivity(intent);
        finish();
    }


}
