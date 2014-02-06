package edu.msu.comfortablynumb.project1;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Game {
	
	private int gameSize;

	public ArrayList<BlockPiece> blocks = new ArrayList<BlockPiece>();

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
	
	private int numBlocks;
	
	private float centerCanvas;

	private Context gameContext;
	
	public Game(Context context, View view) {
		blockView = (BlockView) view;
		
		gameContext = context;
		addBlock(view);
		
		//blocks.add(new BlockPiece(context, R.drawable.brick_green1));
		
	}
	
	public void addBlock( View view){
		blocks.add(new BlockPiece(gameContext, R.drawable.brick_barney, numBlocks, centerCanvas));
		numBlocks+=1;
	}

	public void draw(Canvas canvas){
		
		
		int wid = canvas.getWidth();
		int hit = canvas.getHeight();
		
		centerCanvas = (float) (wid/4.0);
		int minDim = wid <hit ? wid : hit;
		
		gameSize = minDim;
		
		
		for(BlockPiece blockPiece : blocks){
			blockPiece.draw(canvas);
		}

	}


	public boolean onTouchEvent(View view, MotionEvent event){

		float relX = event.getX();///gameSize;
		float relY = event.getY();///gameSize;
		
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			return onTouched(event.getX(), event.getY());
			//return true;
		case MotionEvent.ACTION_UP:
			return onReleased(view, relX, relY);
			
			
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("onTouchEvent",  "ACTION_MOVE: " + event.getX() + "," + event.getY());
			topBlock.move(relX - lastRelX);
			lastRelX = relX;
			view.invalidate();
			return true;
			
		

		}
		return false;
	}

	
	private boolean onReleased(View view, float x, float y){
		addBlock(view);
		view.invalidate();

		return true;
	}
	
	private boolean onTouched(float x, float y){
		topBlock = blocks.get(numBlocks-1);
		lastRelX = topBlock.getX();
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
