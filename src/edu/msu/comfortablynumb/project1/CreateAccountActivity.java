package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Aaron on 3/31/14.
 */
public class CreateAccountActivity extends Activity {

    EditText username;
    EditText password;
    EditText confirmPassword;

    static final String PASSWORDS_DONT_MATCH = "Passwords don't match!";
    static final String FILL_OUT_INFO = "Please fill out all forms";
    static final String USERNAME = "USERNAME";
    static final String PASSWORD = "PASSWORD";
    static final String CONFIRM_PASSWORD = "CONFIRM_PASSWORD";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = (EditText)findViewById(R.id.usernameText);
        password = (EditText)findViewById(R.id.passwordText);
        confirmPassword = (EditText)findViewById(R.id.confirmPasswordText);

        if ( savedInstanceState != null ) {
            loadInstanceState(savedInstanceState);
        }
    }

    public void onCreateAccount(View view) {

        Log.i("CreateAccountActivity", "CreateAccount Clicked");
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String passwordConfirmText = confirmPassword.getText().toString();

        if( usernameText.equals("") || passwordText.equals("") || passwordConfirmText.equals("")) {
            Toast.makeText(this, FILL_OUT_INFO, Toast.LENGTH_SHORT).show();
        }
        else if (passwordText.compareTo(passwordConfirmText) != 0) {
            Toast.makeText(this, PASSWORDS_DONT_MATCH, Toast.LENGTH_SHORT).show();
        } else {
            //
            //
            //
            //
            //Create account server code here
            //
            //
            finish(); //Return back to login screen
        }
    }

    /**
     * Save the names to a bundle
     * @param bundle The bundle we save to
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putString(USERNAME, username.getText().toString());
        bundle.putString(PASSWORD, password.getText().toString());
        bundle.putString(CONFIRM_PASSWORD, confirmPassword.getText().toString());
    }

    /**
     * Read the names from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        username.setText(bundle.getString(USERNAME));
        password.setText(bundle.getString(PASSWORD));
        confirmPassword.setText(bundle.getString(CONFIRM_PASSWORD));
    }


}