package edu.msu.comfortablynumb.project1;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GameActivity extends Activity {

	private BlockView blockView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_game);


		blockView = (BlockView)this.findViewById(R.id.blockView);

        Intent intent = this.getIntent();
        blockView.setPlayerOneName(intent.getStringExtra(MainActivity.PLAYER_ONE_NAME));
        blockView.setPlayerTwoName(intent.getStringExtra(MainActivity.PLAYER_TWO_NAME));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	/**
	 * Called when options button selected
	 * @param item a menu item to use
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_help:
            // The puzzle is done
            // Instantiate a dialog box builder
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

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
	
	public void onWeightSelected(View view){
		
		CharSequence weight = ((Button) view).getText();
		blockView.forwardOnWeightSelected(weight);
		
	}

	public void onEndGame(View view) {

		Intent intent = new Intent(this, ScoreActivity.class);

		startActivity(intent);
	}

}
