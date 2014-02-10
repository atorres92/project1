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
import android.widget.TextView;

public class GameActivity extends Activity {

	private BlockView blockView;

	private TextView playerOneScoreView;
    private TextView playerTwoScoreView;

    private String playerOneName;
    private String playerTwoName;

    public static enum playerNames { playerone, playertwo };

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_game);


		blockView = (BlockView)this.findViewById(R.id.blockView);

        // Grab player names and scores
        Intent intent = this.getIntent();
        playerOneName = intent.getStringExtra(MainActivity.PLAYER_ONE_NAME);
        playerTwoName = intent.getStringExtra(MainActivity.PLAYER_TWO_NAME);
        playerOneScoreView = (TextView)this.findViewById(R.id.playerOneScore);
        playerTwoScoreView = (TextView)this.findViewById(R.id.playerTwoScore);

        blockView = (BlockView)this.findViewById(R.id.blockView);

        //send activity to blockView so it can grab player names and scores
        blockView.setGameActivity(this);
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
        finish();
	}

    public void updateScore( playerNames player, int score ) {
        switch (player) {
            case playerone:
                playerOneScoreView.setText( playerOneName + ":" + score );
            case playertwo:
                playerTwoScoreView.setText( playerTwoName + ":" + score );
        }
    }

    public TextView getPlayerOneScoreView() {
        return playerOneScoreView;
    }

    public void setPlayerOneScoreView(TextView playerOneScoreView) {
        this.playerOneScoreView = playerOneScoreView;
    }

    public TextView getPlayerTwoScoreView() {
        return playerTwoScoreView;
    }

    public void setPlayerTwoScoreView(TextView playerTwoScoreView) {
        this.playerTwoScoreView = playerTwoScoreView;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }
}
