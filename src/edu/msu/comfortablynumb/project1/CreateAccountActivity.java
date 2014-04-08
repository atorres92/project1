package edu.msu.comfortablynumb.project1;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

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
    private String ACCOUNT_ALREADY_EXISTS = "Username already exists";

    private Handler toastHandler;
    private Runnable toastRunnable;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = (EditText)findViewById(R.id.usernameText);
        password = (EditText)findViewById(R.id.passwordText);
        confirmPassword = (EditText)findViewById(R.id.confirmPasswordText);

        toastHandler = new Handler();
        toastRunnable = new Runnable() {
            public void run() {
                Toast.makeText(CreateAccountActivity.this, ACCOUNT_ALREADY_EXISTS, Toast.LENGTH_LONG).show();
            }
        };

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

            // Create a thread to create a new login
            new Thread(new Runnable() {

                /**
                 * Save the hatting in the background
                 */
                @Override
                public void run() {
                    Cloud cloud = new Cloud();
                    InputStream stream = cloud.createOnCloud(username.getText().toString(), password.getText().toString(), true);

                    // Test for an error
                    boolean fail = stream == null;
                    if(!fail) {
                        try {
                            XmlPullParser xml = Xml.newPullParser();
                            xml.setInput(stream, "UTF-8");


                            xml.nextTag();      // Advance to first tag
                            xml.require(XmlPullParser.START_TAG, null, "brick");
                            String status = xml.getAttributeValue(null, "status");
                            Log.i("status", status);
                            if (status.equalsIgnoreCase("no")) {
                                toastHandler.post(toastRunnable);
                            }


                        } catch(IOException ex) {
                            fail = true;
                        } catch(XmlPullParserException ex) {
                            fail = true;
                        } finally {
                            try {
                                stream.close();
                            } catch(IOException ex) {
                            }
                        }
                    }
                }



            }).start();

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