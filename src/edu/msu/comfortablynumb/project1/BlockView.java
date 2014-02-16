package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BlockView extends View {

	private Game game;

	private int turn;
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
		turn = 1;
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

	public void forwardOnWeightSelected(CharSequence weight){

		game.addBlock(this, weight, turn);
		if(turn==1)
			turn = 0;
		else
			turn =1;
	}

}
