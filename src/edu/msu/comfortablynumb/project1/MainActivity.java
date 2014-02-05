package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	final String PLAYER_ONE_NAME = "PLAYER_ONE_NAME";
	final String PLAYER_TWO_NAME = "PLAYER_TWO_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
