package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity {

    TextView winnerView;
    TextView loserView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
        setTitle("Comfortably Numb");

        winnerView = (TextView)this.findViewById(R.id.winnerText);
        loserView = (TextView)this.findViewById(R.id.loserText);


        if ( savedInstanceState != null ) {
            loadInstanceState(savedInstanceState);
        } else {
            Intent intent = this.getIntent();

            String winnerName = intent.getStringExtra(GameActivity.WINNER);
            String loserName = intent.getStringExtra(GameActivity.LOSER);
            winnerView.setText("Winner! " + winnerName);
            loserView.setText("Loser! " + loserName);

        }
    }

	public void onRestartGame(View view) {
        view.invalidate();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
        finish();
	}

    /**
     * Save the names to a bundle
     * @param bundle The bundle we save to
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState( bundle );

        String [] names = new String[Game.PLAYERS];

        if (winnerView.getText() != null && loserView.getText() != null ) {
            names[0] = winnerView.getText().toString();
            names[1] = loserView.getText().toString();
            bundle.putStringArray(MainActivity.NAMES, names);
        }
    }

    /**
     * Read the names from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        String [] names = bundle.getStringArray(MainActivity.NAMES);
        if ( names != null ) {
            winnerView.setText(names[0]);
            loserView.setText(names[1]);
        }
    }

}
