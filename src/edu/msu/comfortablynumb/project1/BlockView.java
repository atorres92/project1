package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BlockView extends View {

	private Game game;
    private String playerOneName;
    private String playerTwoName;

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
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		game.draw(canvas);
		//Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//linePaint.setColor(0xff008000);
		//linePaint.setStrokeWidth(3);
		//canvas.drawLine(0,  0, 400, 100, linePaint);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return game.onTouchEvent(this, event);
	}
	
}
