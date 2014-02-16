package edu.msu.comfortablynumb.project1;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Game {

    private GameActivity gameActivity;

	private BlockView blockView;
	/**
	 * Pointer to the top most block
	 */
	private BlockPiece topBlock;
	
	/**
	 * lost relative location of X
	 */
	private float lastRelX;
	
	/**
	 * last relative location of Y
	 */
	private float lastRelY;
	
    private int playerOneScore;
    private int playerTwoScore;

    /**
     * number of blocks in play
     */
	private int numBlocks;
	/**
	 * returns center position of canvas for block positioning 
	 */
	private float centerCanvas;

	private Context gameContext;
	
	/**
	 * the possible touch states the game can be in.
	 * @author keyurpatel
	 */
	private enum touchStates {horizontal, vertical, none};
	/**
	 * the starting game state
	 */
	private touchStates touchState = touchStates.none;
	
	/**
	 * last of last relY
	 */
	private float lastLastRelY;
	
	/**
	 * saves the offset of the canvas used for vertical scrolling
	 */
	private float offset;
	

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    //Our block pieces are stored in this array
    public ArrayList<BlockPiece> blocks = new ArrayList<BlockPiece>();

    public void updateScore() {
        if (gameActivity != null) {
            gameActivity.getPlayerOneScoreView().setText(gameActivity.getPlayerOneName() + ":" + playerOneScore);
            gameActivity.getPlayerTwoScoreView().setText(gameActivity.getPlayerTwoName() + ":" + playerTwoScore);
        }
    }

	public Game(Context context, View view) {
        playerOneScore = 0;
        playerTwoScore = 0;
		blockView = (BlockView) view;
		lastLastRelY = 0;
		gameContext = context;
        gameActivity = blockView.getGameActivity();

	}
	
	public void addBlock( View view, CharSequence weight, int player){
		//sets the touch state to horizontal so the block piece can be moved horizontally 
		touchState = touchStates.horizontal;
		int trueWeight = 0;
		String stringWeight = weight.toString();
		if(stringWeight.matches("1 kg"))
			trueWeight = 1;
		else if (stringWeight.matches("2 kg"))
			trueWeight = 2;
		else if(stringWeight.matches("5 kg"))
			trueWeight = 5;
		else 
			trueWeight = 10;
		
		//draws the block a certain color depending on the player
		if(player ==1){			
		blocks.add(new BlockPiece(gameContext, R.drawable.brick_red1, numBlocks, centerCanvas, trueWeight));
		}
		else{		
		blocks.add(new BlockPiece(gameContext, R.drawable.brick_blue, numBlocks, centerCanvas, trueWeight));
		}
		numBlocks+=1;
		topBlock = blocks.get(numBlocks-1);
		lastRelX = topBlock.getX();
		view.invalidate();
		
	}

	
	
	
	public void draw(Canvas canvas){
				
		int wid = canvas.getWidth();
		int hit = canvas.getHeight();
		
		int minDim = wid < hit ? wid : hit;
		
		if( wid == minDim)
			centerCanvas =  (float) ((float) wid/2.0); 
		else
			centerCanvas = (float) ((float) wid/2.0) ;
		
		Log.i("draw", " " +wid + "center " + centerCanvas);
		canvas.save();
		
		//allows for vertical scrolling.
		if(touchState == touchStates.vertical){				
			if(lastLastRelY <lastRelY )
				offset +=5;
			else if (lastLastRelY > lastRelY &&  offset >=0)
				offset -=5;		
		}
		canvas.translate(0, offset);
		
		//draws the block pieces
		for(BlockPiece blockPiece : blocks){
			blockPiece.draw(canvas);
		}
		
		canvas.restore();
        updateScore();
    }

	public boolean onTouchEvent(View view, MotionEvent event){

		float relX = event.getX();
		float relY = event.getY();
		
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_UP:
			return onReleased(view, relX, relY);	
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_MOVE:
			//stores relevant information depending on the touch state
			//Log.i("onTouchEvent",  "ACTION_MOVE: " + event.getX() + "," + event.getY());
			if(touchState == touchStates.horizontal){
				if(topBlock!=null){
				topBlock.move(relX - topBlock.getWidth()/2, relY); 
				lastRelX = relX;
				view.invalidate();
				}
				return true;
			}
			else if (touchState == touchStates.vertical){
				lastLastRelY = lastRelY;			
				lastRelY = relY;
				view.invalidate();
				return true;
			}
			else
				return true;
		
		}
		return false;
	}

	private boolean onReleased(View view, float x, float y){

        /* if player one's block falls:
        playerTwoScore++;
        updateScore();
        ...
         */
		if(topBlock!=null){
			topBlock.placed=true;
		}
		view.invalidate();
		
		//Change the touch state so as soon as the player lets go the block cannot be shifted 
		if(touchState == touchStates.horizontal)
			touchState = touchStates.vertical;
		else if (touchState == touchStates.vertical)
			;
		else
			;	
		return true;
	}
	

	/*returns float array of center of mass:
	 * value[0] = X
	 * value[1] = Y
	 */
	private float[] calculateCenterOfMass(){

		//Sum up the blocks to find center of mass
		float[] centerOfMass = new float[2];
		int sumMass = 0;
		float sumX = 0;
		float sumY = 0;

		//Remember to remove the first one from calculation later
		for (BlockPiece b : blocks){
	        sumMass = sumMass + b.getWeight();
	        sumX = ((float)b.getWeight()) * b.getX();
	        sumY = ((float)b.getWeight()) * b.getY();

	     }
		centerOfMass[0] = (float) (1.0 / ((float)sumMass) *sumX);
		centerOfMass[1] = (float) (1.0 / ((float)sumMass) *sumY);

		return centerOfMass;
	}
	

}
