package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BlockView extends View {

	private Game game;

	private int turn;

    public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	private GameActivity gameActivity;

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity activity) {
        gameActivity = activity;
        game.setGameActivity(gameActivity);
    }

	public BlockView(Context context) {
		super(context);
		init(context);
	}

	public BlockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);

	}

	private void init(Context context){
		game = new Game(context, this.findViewById(R.id.blockView)) ;
		setTurn(0);
	}


	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		game.draw(canvas);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return game.onTouchEvent(this, event);
	}

	public void forwardOnWeightSelected(int weight){

		game.addBlock(this, weight, turn);
		if(turn==1)
			setTurn(0);
		else
			setTurn(1);
	}

    public void updateTurn( int currentTurn ) {
        turn = currentTurn;
    }

    /**
     * Save the Game to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    /**
     * Load the Game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        game.loadInstanceState(bundle);
    }

}
