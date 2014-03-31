package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    /*
     * DELETE THIS!!!!!!
     */
	static final String PLAYER_ONE_NAME = "PLAYER_ONE_NAME";
	static final String PLAYER_TWO_NAME = "PLAYER_TWO_NAME";
    static final String ENTER_PLAYER_NAME = "Please Enter Names";
    static final String NAMES = "NAMES";
    //////////////////////////////////////

	static final String USERNAME = "USERNAME";
	static final String PASSWORD = "PASSWORD";
    static final String REMEMBER = "REMEMBER";
    static final String LOGIN_FAILED = "Login Failed";
    EditText usernameText;
    EditText passwordText;
    CheckBox checkbox;
    boolean remember = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        setTitle("Comfortably Numb");

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        checkbox = (CheckBox) findViewById(R.id.checkBox);

        if ( savedInstanceState != null ) {
            loadInstanceState(savedInstanceState);
        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Called when options button selected
	 * @param item a menu item to use
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.game_help:
            // The puzzle is done
            // Instantiate a dialog box builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            // Parameterize the builder
            builder.setTitle(R.string.howtoplay);
            builder.setMessage(R.string.howtotext);
            builder.setPositiveButton(android.R.string.ok, null);

            // Create the dialog box and show it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
	}

    public void onLogin(View view) {
        Log.i("Clicked login button", "A1234");

        if ( !usernameText.getText().toString().equals("") && !passwordText.getText().toString().equals("") ) {
            //
            //
            //Server code here
            //
            //
        } else {
            Toast.makeText(this, LOGIN_FAILED, Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreateAccount(View view) {
        Log.i("Clicked create account button", "A1234");

        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /*
	public void onStartGame(View view) {
        if ( playerOneText.getText() != null && playerTwoText.getText() != null && getApplicationContext() != null ) {
            if (playerOneText.getText().toString().matches("") || playerTwoText.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), ENTER_PLAYER_NAME,
                        Toast.LENGTH_SHORT).show();
            } else {
                String playerOneName = playerOneText.getText().toString();
                String playerTwoName = playerTwoText.getText().toString();

                Intent intent = new Intent(this, GameActivity.class);

                intent.putExtra(PLAYER_ONE_NAME, playerOneName);
                intent.putExtra(PLAYER_TWO_NAME, playerTwoName);

                startActivity(intent);
                finish();
            }
        }
	}
	*/

    /**
     * Save the names to a bundle
     * @param bundle The bundle we save to
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        if (usernameText.getText() != null && passwordText.getText() != null ) {
            String username = usernameText.getText().toString();
            String password =  passwordText.getText().toString();

            bundle.putString(USERNAME, username);
            bundle.putString(PASSWORD, password);
            bundle.putBoolean(REMEMBER, remember);
        }

    }

    /**
     * Read the names from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        String username = bundle.getString(USERNAME);
        String password = bundle.getString(PASSWORD);
        remember = bundle.getBoolean(REMEMBER);
        checkbox.setChecked(remember);

        if ( username != null && password != null ) {
            usernameText.setText(username);
            passwordText.setText(password);
        }
    }

    public void onRemember(View view) {
        if (checkbox.isChecked()) {
            Log.i("Checked","checked");
            remember = true;
        } else {
            Log.i("Unchecked","unchecked");
            remember = false;
        }
    }

}
