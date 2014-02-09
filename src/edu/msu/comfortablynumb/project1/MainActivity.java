package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	static final String PLAYER_ONE_NAME = "PLAYER_ONE_NAME";
	static final String PLAYER_TWO_NAME = "PLAYER_TWO_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		EditText playerOneText = (EditText) findViewById(R.id.player1Input);
		EditText playerTwoText = (EditText) findViewById(R.id.player2Input);

		String playerOneName = playerOneText.getText().toString();
		String playerTwoName = playerTwoText.getText().toString();

		Intent intent = new Intent(this, GameActivity.class);

		intent.putExtra(PLAYER_ONE_NAME, playerOneName);
		intent.putExtra(PLAYER_TWO_NAME, playerTwoName);

		startActivity(intent);
	}

}
