package edu.msu.comfortablynumb.project1;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity {

	private BlockView blockView;

	private TextView myScoreTextView;
    private TextView otherPlayerScoreTextView;

    private String myPlayerName;
    private String otherPlayerName;

    private String firstPlayer;
    private String secondPlayer;

    public static final String NAMES = "GameActivity.names";
    public static final String WINNER = "GameActivity.winner";
    public static final String LOSER = "GameActivity.loser";

    @Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_game);
        setTitle("Comfortably Numb");

		blockView = (BlockView)this.findViewById(R.id.blockView);

        if(bundle != null) {
            // We have saved state
            loadInstanceState(bundle);
        }

        // Grab player names and scores
        Intent intent = this.getIntent();
        setMyPlayerName(intent.getStringExtra(MainActivity.USERNAME));
        setOtherPlayerName(intent.getStringExtra(MainActivity.USERNAME_2));
        firstPlayer = intent.getStringExtra(MainActivity.FIRST_PLAYER);
        secondPlayer = intent.getStringExtra(MainActivity.SECOND_PLAYER);

        //Set two player name text views
        setMyScoreTextView((TextView) this.findViewById(R.id.playerOneScore));
        setOtherPlayerScoreTextView((TextView) this.findViewById(R.id.playerTwoScore));

        //send activity to blockView so it can grab player names and scores
        blockView.setGameActivity(this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

                // Parameterize the builder
                builder.setTitle(R.string.howtoplay);
                builder.setMessage(R.string.howtotext);
                builder.setPositiveButton(android.R.string.ok, null);

                // Create the dialog box and show it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.exit_game:
                //Exit the game
                exitApplication();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void exitApplication() {
        //TODO server code:
        /*
         * Server code here to tell database that I'm no longer connected
         */
    }

	public void onWeightSelected(View view){
		int weight;
		CharSequence weightCharSeq = ((Button) view).getText();
        if (weightCharSeq != null) {
            if( weightCharSeq.equals("1 kg") ) {
                weight = 1;
            } else if ( weightCharSeq.equals("2 kg") ) {
                weight = 2;
            } else if ( weightCharSeq.equals("5 kg") ) {
                weight = 5;
            } else {
                weight = 10;
            }
            blockView.forwardOnWeightSelected(weight);
        }
	}

	public void onEndGame(View view, String winner) {
 		Intent intent = new Intent(this, ScoreActivity.class);

        if ( winner == MainActivity.PLAYER_NAME) {
            intent.putExtra(WINNER, myScoreTextView.getText().toString());
            intent.putExtra(LOSER, otherPlayerScoreTextView.getText().toString());
        } else {
            intent.putExtra(WINNER, otherPlayerScoreTextView.getText().toString());
            intent.putExtra(LOSER, myScoreTextView.getText().toString());
        }

		startActivity(intent);
        finish();
	}

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /**
     * Save the instance state into a bundle
     * @param bundle the bundle to save into
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        String [] names = new String[Game.PLAYERS];
        names[Game.PLAYER_ONE] = firstPlayer;
        names[Game.PLAYER_TWO] = secondPlayer;
        bundle.putStringArray(NAMES, names);

        blockView.saveInstanceState(bundle);
    }

    /**
     * Read the Game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        String [] names = bundle.getStringArray(NAMES);
        if ( names != null ) {
            firstPlayer = names[Game.PLAYER_ONE];
            secondPlayer = names[Game.PLAYER_TWO];
            blockView.loadInstanceState(bundle);
        }
    }

    public TextView getMyScoreTextView() { return myScoreTextView; }

    public void setMyScoreTextView(TextView myScoreTextView) {
        this.myScoreTextView = myScoreTextView;
    }

    public TextView getOtherPlayerScoreTextView() {
        return otherPlayerScoreTextView;
    }

    public void setOtherPlayerScoreTextView(TextView otherPlayerScoreTextView) {
        this.otherPlayerScoreTextView = otherPlayerScoreTextView;
    }

    public String getMyPlayerName() {
        return myPlayerName;
    }

    public void setMyPlayerName(String myPlayerName) {
        this.myPlayerName = myPlayerName;
    }

    public String getOtherPlayerName() {
        return otherPlayerName;
    }

    public void setOtherPlayerName(String otherPlayerName) {
        this.otherPlayerName = otherPlayerName;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

}
