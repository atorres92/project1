package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Game {

	private final float TURN_TIME = 2000;

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

	public float getLastRelX() {
		return lastRelX;
	}

	public void setLastRelX(float lastRelX) {
		this.lastRelX = lastRelX;
	}

	/**
	 * last relative location of Y
	 */
	private float lastRelY;

    /**
     * Score for player one
     */
    private int playerOneScore;

    /**
     * score for player two
     */
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

    /**
     * Context of game
     */
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

    //If a brick falls, stop polling server to see if second player placed a brick
    private volatile boolean gameOver = false;

	/**
	 * last of last relY
	 */
	private float lastLastRelY;

	/**
	 * saves the offset of the canvas used for vertical scrolling
	 */
	private float offset;

    private volatile boolean doneSavingBrick = false;

    /**
     * The name of the bundle keys to save the Game
     */
    private final static String LOCATIONS = "Game.locations";
    private final static String IDS = "Game.ids";
    private final static String WEIGHTS = "Game.weights";
    private final static String SCORES = "Game.scores";
    private final static String TOUCHSTATE = "Game.touchstate";

    /**
     * Constants for index values
     */
    public final static int PLAYERS = 2;
    public final static int PLAYER_ONE = 0;
    public final static int PLAYER_TWO = 1;

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
            gameActivity.getPlayerScoreView().setText(gameActivity.getPlayerName() + ":" + playerOneScore);
            gameActivity.getSecondPlayerScoreView().setText(gameActivity.getSecondPlayerName() + ":" + playerTwoScore);
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

    public void waitForOtherPlayer() {
        //Wait for other player to place their brick
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!gameOver) {
                        if( doneSavingBrick ) {
                            boolean secondPlayerDone = false; //true when second player is done taking their turn.

                            //SERVER CODE HERE:
                            //


                            Cloud cloud = new Cloud();
                            InputStream stream = cloud.pollGame(gameActivity.getPlayerName());
                            // Test for an error
                            boolean fail = stream == null;

                            if(!fail) {
                                try {
                                    XmlPullParser xml = Xml.newPullParser();
                                    xml.setInput(stream, "UTF-8");

                                    xml.nextTag();      // Advance to first tag
                                    xml.require(XmlPullParser.START_TAG, null, "brick");
                                    String status = xml.getAttributeValue(null, "status");
                                    Log.i("Polling Server...", "Polling Server with status: " + status);
                                    if(status.equals("yes")) {
                                        while(xml.nextTag() == XmlPullParser.START_TAG) {
                                            if(xml.getName().equals("last_brick")) {
                                                int weight = Integer.parseInt(xml.getAttributeValue(null, "weight"));
                                                float x = Float.parseFloat(xml.getAttributeValue(null, "x"));
                                                float y = Float.parseFloat(xml.getAttributeValue(null, "y"));
                                                int height = Integer.parseInt(xml.getAttributeValue(null, "height"));
                                                callBlock(weight, height, x, y);


                                            }
                                        }
                                        secondPlayerDone = true;
                                    }

                                } catch(IOException ex) {
                                    fail = true;
                                } catch(XmlPullParserException ex) {
                                    Log.i("ex:", ex.getMessage());
                                    fail = true;
                                } finally {
                                    try {
                                        stream.close();
                                    } catch(IOException ex) {
                                        Log.i("Message",ex.getMessage());
                                    }
                                }
                            }


                            if (secondPlayerDone) {
                                doneSavingBrick = false;
                                Log.i("Second player done with turn", "Second Player done...");
                            } else {
                                Thread.sleep(3000);
                            }
                        }
                    }

                } catch( Exception e ) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void addBlock( View view, int weight, int player){
        view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);

        if(stackState == stackStates.standing ){
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

	}

	public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        centerCanvas = ( (float) wid / 2f );

		canvas.save();

		//allows for vertical scrolling.
		if(touchState == touchStates.vertical && stackState == stackStates.standing){
			if(lastLastRelY <lastRelY )
				offset +=5;
			else if (lastLastRelY > lastRelY &&  offset >=0 && stackState == stackStates.standing)
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
				restartGame();

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

			saveBrickToCloud(BlockView.getGameActivity().getPlayerName());
            waitForOtherPlayer();

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
				stackState = stackStates.fallingLeft;
				return i;
			}
			else{
				stackState = stackStates.fallingRight;
				return i;
			}

		}
		return -1;
	}

	public void restartGame() {
		if(blockView.getTurn() == 1)
			playerTwoScore += 1;
		else
			playerOneScore += 1;

        updateScore();
		//Someone wins when they get 3 points
		if(playerOneScore >= 5 ) {
            gameActivity.onEndGame(blockView, MainActivity.PLAYER_NAME);
        } else if ( playerTwoScore >= 5) {
            gameActivity.onEndGame(blockView, MainActivity.SECOND_PLAYER_NAME);
        }

		blocks.clear();
        offset=0;
        blockView.setTurn(0);

		numBlocks = 0;
		topBlock = null;
		fallingStartTime =0;
		stackState = stackStates.standing;
	}

	public void saveBrickToCloud(String username){

		final String  user = username;
        // Create a thread to create a new login
        new Thread(new Runnable() {

            /**
             * Save the hatting in the background
             */
            @Override
            public void run() {
                Cloud cloud = new Cloud();
                boolean ok = cloud.saveToCloud(user, blockView);

                if(!ok) {
                	Log.i("Saving", "Saving Error");

                } else {
                    doneSavingBrick = true;
                }

            }

        }).start();
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

    public void callBlock(int weight, int height, float x, float y){
        addBlock(blockView, weight, 2);
        BlockPiece piece = blocks.get(blocks.size()-1);
        piece.setX(x);
        piece.setY(y);

    }


    public void saveXml(String id, XmlSerializer xml) throws IOException{
        xml.startTag(null, "lastbrick");

        xml.attribute(null, "weight", Float.toString(topBlock.getWeight())  );
        xml.attribute(null, "x", Float.toString(topBlock.getX()));
        xml.attribute(null, "y", Float.toString(topBlock.getY()));
        xml.attribute(null, "height", Float.toString(topBlock.getHeight()));

        xml.endTag(null,  "lastbrick");

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
