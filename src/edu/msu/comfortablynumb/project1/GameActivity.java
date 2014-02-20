package edu.msu.comfortablynumb.project1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity {

	private BlockView blockView;

	private TextView playerOneScoreView;
    private TextView playerTwoScoreView;

    private String playerOneName;
    private String playerTwoName;

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
        setPlayerOneName(intent.getStringExtra(MainActivity.PLAYER_ONE_NAME));
        setPlayerTwoName(intent.getStringExtra(MainActivity.PLAYER_TWO_NAME));
        setPlayerOneScoreView( (TextView)this.findViewById(R.id.playerOneScore) );
        setPlayerTwoScoreView((TextView) this.findViewById(R.id.playerTwoScore));

        //send activity to blockView so it can grab player names and scores
        blockView.setGameActivity(this);
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

        if ( winner == MainActivity.PLAYER_ONE_NAME ) {
            intent.putExtra(WINNER, playerOneScoreView.getText().toString());
            intent.putExtra(LOSER, playerTwoScoreView.getText().toString());
        } else {
            intent.putExtra(WINNER, playerTwoScoreView.getText().toString());
            intent.putExtra(LOSER, playerOneScoreView.getText().toString());
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
        names[Game.PLAYER_ONE] = playerOneName;
        names[Game.PLAYER_TWO] = playerTwoName;
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
            playerOneName = names[Game.PLAYER_ONE];
            playerTwoName = names[Game.PLAYER_TWO];
            blockView.loadInstanceState(bundle);
        }
    }

    public TextView getPlayerOneScoreView() { return playerOneScoreView; }

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
