package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Game {

	private final float TURN_TIME = 3500;

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
	 * Holds the startTime of the fall
	 */
	private long fallingStartTime;

	/**
	 * Brick
	 */
	private int turningBlock;

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
	private touchStates touchState;

	/**
	 * the possible states of the stack
	 */
	private enum stackStates {standing, fallingLeft, fallingRight, fallen};
	/**
	 * the starting stack state
	 */
	private stackStates stackState;


	/**
	 * last of last relY
	 */
	private float lastLastRelY;

	/**
	 * saves the offset of the canvas used for vertical scrolling
	 */
	private float offset;

    /**
     * The name of the bundle keys to save the Game
     */
    private final static String LOCATIONS = "Game.locations";
    private final static String IDS = "Game.ids";
    private final static String WEIGHTS = "Game.weights";
    private final static String SCORES = "Game.scores";
    private final static String TOUCHSTATE = "Game.touchstate";
    private final static int PLAYERS = 2;
    private final static int PLAYER_ONE = 0;
    private final static int PLAYER_TWO = 1;

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    //Our block pieces are stored in this array
    public ArrayList<BlockPiece> blocks;

    public void updateScore() {
        if (gameActivity != null) {
            gameActivity.getPlayerOneScoreView().setText(gameActivity.getPlayerOneName() + ":" + playerOneScore);
            gameActivity.getPlayerTwoScoreView().setText(gameActivity.getPlayerTwoName() + ":" + playerTwoScore);
        }
    }

	public Game(Context context, View view) {
        blocks = new ArrayList<BlockPiece>();
        touchState = touchStates.none;
        playerOneScore = 0;
        playerTwoScore = 0;
		blockView = (BlockView) view;
		lastLastRelY = 0;
		gameContext = context;
		stackState = stackStates.standing;
        gameActivity = blockView.getGameActivity();
        turningBlock = -1;
        fallingStartTime = 0;

	}

	public void addBlock( View view, int weight, int player){
		//sets the touch state to horizontal so the block piece can be moved horizontally
		touchState = touchStates.horizontal;

		//draws the block a certain color depending on the player
		if(player ==1){
		blocks.add(new BlockPiece(gameContext, R.drawable.brick_red1, numBlocks, centerCanvas, weight));
		}
		else{
		blocks.add(new BlockPiece(gameContext, R.drawable.brick_blue, numBlocks, centerCanvas, weight));
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

		Log.i("draw", " " + wid + "center " + centerCanvas);
		canvas.save();

		//allows for vertical scrolling.
		if(touchState == touchStates.vertical){
			if(lastLastRelY <lastRelY )
				offset +=5;
			else if (lastLastRelY > lastRelY &&  offset >=0)
				offset -=5;
		}
		canvas.translate(0, offset);

		if((stackState == stackStates.fallingLeft || stackState == stackStates.fallingRight) && fallingStartTime == 0 )
			fallingStartTime = System.currentTimeMillis();


		long currentTime = System.currentTimeMillis();

		if(stackState == stackStates.fallingLeft || stackState == stackStates.fallingRight){
			if((currentTime-fallingStartTime) < TURN_TIME){
				for(int i = turningBlock +1 ; i < blocks.size(); i++){
					if(stackState == stackStates.fallingRight)
						blocks.get(i).setRotation((((float)(currentTime-fallingStartTime))/TURN_TIME) * 90);
					else
						blocks.get(i).setRotation((((float)(currentTime-fallingStartTime))/TURN_TIME) * -90);
				}

			}
			else{
				stackState = stackStates.fallen;

			}


			blockView.invalidate();
		}

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
		if(touchState == touchStates.horizontal){
			touchState = touchStates.vertical;
			turningBlock = calculateCenterOfMass();
			if(turningBlock != -1){
				for (int j = turningBlock +1; j < blocks.size(); j++){
					if(stackState == stackStates.fallingRight){
						blocks.get(j).setTurnBlockX(blocks.get(turningBlock).getX() + blocks.get(j).getWidth());
						blocks.get(j).setTurnBlockY(blocks.get(turningBlock).getY());
					}
					else{
						blocks.get(j).setTurnBlockX(blocks.get(turningBlock).getX()) ;
						blocks.get(j).setTurnBlockY(blocks.get(turningBlock).getY());
					}

			     }
			}

		}
		else if (touchState == touchStates.vertical)
			;
		else
			;
		return true;
	}


	/* Returns the position of the block that the stack is falling over
	 * A return of -1 means that nothing is falling
	 */
	private int calculateCenterOfMass(){

		if(blocks.size() <= 1)
			return -1;

		for(int i = 0; i < blocks.size()-1; i++)
		{
			//Sum up the blocks to find center of mass
			float[] centerOfMass = new float[2];
			int sumMass = 0;
			float sumX = 0;
			float sumY = 0;

			//Remember to remove the first one from calculation later
			for (int j = i+1; j < blocks.size(); j++){
				BlockPiece b = blocks.get(j);
		        sumMass = sumMass + b.getWeight();
		        sumX = sumX + ((float)b.getWeight()) * b.getX();
		        sumY = sumY + ((float)b.getWeight()) * b.getY();

		     }
			centerOfMass[0] = (float) (1.0 / ((float)sumMass) *sumX);
			centerOfMass[1] = (float) (1.0 / ((float)sumMass) *sumY);

			BlockPiece bottom = blocks.get(i);
			if(bottom.getX() - (bottom.getWidth()/2) < centerOfMass[0] && (bottom.getX() + (bottom.getWidth()/2))  >  centerOfMass[0] ){
			}

			else if (bottom.getX() - (bottom.getWidth()/2) >= centerOfMass[0]){
				Log.i("falling", "Fallling at block: " + i);
				stackState = stackStates.fallingLeft;
				return i;
			}
			else{
				Log.i("falling", "Fallling at block: " + i);
				stackState = stackStates.fallingRight;
				return i;
			}

		}



		Log.i("standing", "Still Standing");
		return -1;
	}

    /**
     * Save the Game to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[blocks.size() * 2];
        int [] ids = new int[blocks.size()];
        int [] scores = new int[PLAYERS];
        int [] weights = new int[blocks.size()];

        for(int i=0;  i<blocks.size(); i++) {
            BlockPiece piece = blocks.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
            weights[i] = piece.getWeight();

            bundle.putFloatArray(LOCATIONS, locations);
            bundle.putIntArray(IDS,  ids);
            bundle.putIntArray(WEIGHTS, weights);
        }

        scores[PLAYER_ONE] = playerOneScore;
        scores[PLAYER_TWO] = playerTwoScore;
        bundle.putIntArray(SCORES, scores);
        bundle.putSerializable( TOUCHSTATE, touchState );
    }

    /**
     * Read the Game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);
        int [] scores = bundle.getIntArray(SCORES);
        int [] weights = bundle.getIntArray(WEIGHTS);

        playerOneScore = scores[PLAYER_ONE];
        playerTwoScore = scores[PLAYER_TWO];

        if ( ids != null ) {
            for(int i=0; i<ids.length; i++) {
                // Since we're starting with empty array of blocks, loop through ids and create corresponding block
                if( ids[i] == R.drawable.brick_red1 ) {
                    addBlock(blockView, weights[i], 1);
                } else if ( ids[i] == R.drawable.brick_blue ) {
                    addBlock(blockView, weights[i], 2);
                }
            }
        } else {
            return;
        }

        for(int i=0;  i<blocks.size(); i++) {
            BlockPiece piece = blocks.get(i);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
        }

        if ( topBlock.getId() == R.drawable.brick_red1 ) {
            blockView.updateTurn(0);
        } else {
            blockView.updateTurn(1);
        }

        touchState = (touchStates) bundle.getSerializable(TOUCHSTATE);
        updateScore();

    }
}
