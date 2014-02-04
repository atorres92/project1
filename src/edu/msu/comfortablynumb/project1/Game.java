package edu.msu.comfortablynumb.project1;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Game {

	public ArrayList<BlockPiece> blocks = new ArrayList<BlockPiece>();

	private BlockView blockView;

	public Game(Context context, View view) {
		blockView = (BlockView) view;

		blocks.add(new BlockPiece(context, R.drawable.brick_barney));
		blocks.add(new BlockPiece(context, R.drawable.brick_green1));
	}

	public void draw(Canvas canvas){
		for(BlockPiece blockPiece : blocks){
			blockPiece.draw(canvas);
		}

	}


	public boolean onTouchEvent(View view, MotionEvent event){

		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			//onTouched(event.getX(), event.getY());
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_MOVE:
			Log.i("onTouchEvent",  "ACTION_MOVE: " + event.getX() + "," + event.getY());
			break;
		case MotionEvent.ACTION_UP:
			break;

		}
		return false;
	}

	private boolean onTouched(float x, float y){
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
