package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BlockPiece {


	private int id;



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}

	//the image of the block
	private Bitmap block;

	/**
	 * x location
	 */
	private float x;
	/**
	 * y location
	 */
	private float y;
	/**
	 * weight
	 */
	private int weight;


	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
	}



	public float getY() {
		return y;
	}



	public void setY(float y) {
		this.y = y;
	}



	public int getWeight() {
		return weight;
	}



	public void setWeight(int weight) {
		this.weight = weight;
	}



	public BlockPiece(Context context, int id) {
		this.id = id;

		block = BitmapFactory.decodeResource(context.getResources(), id);

	}

	public void draw(Canvas canvas ){
		canvas.drawBitmap(block, 0, 0,null);
	}

}
