package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String PLAYER_ONE_NAME = "PLAYER_ONE_NAME";
	static final String PLAYER_TWO_NAME = "PLAYER_TWO_NAME";
    static final String ENTER_PLAYER_NAME = "Please Enter Names";
    static final String NAMES = "NAMES";
    EditText playerOneText;
    EditText playerTwoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        setTitle("Comfortably Numb");

        playerOneText = (EditText) findViewById(R.id.player1Input);
        playerTwoText = (EditText) findViewById(R.id.player2Input);

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

    /**
     * Save the names to a bundle
     * @param bundle The bundle we save to
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState( bundle );

        String [] names = new String[Game.PLAYERS];

        if (playerOneText.getText() != null && playerTwoText.getText() != null ) {
            names[Game.PLAYER_ONE] = playerOneText.getText().toString();
            names[Game.PLAYER_TWO] = playerTwoText.getText().toString();
            bundle.putStringArray(NAMES, names);
        }
    }

    /**
     * Read the names from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        String [] names = bundle.getStringArray(NAMES);
        if ( names != null ) {
            playerOneText.setText(names[Game.PLAYER_ONE]);
            playerTwoText.setText(names[Game.PLAYER_TWO]);
        }
    }

}
