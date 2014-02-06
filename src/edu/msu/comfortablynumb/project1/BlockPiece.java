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



	public BlockPiece(Context context, int id, float dy, float center) {
		this.id = id;
		
		this.x = center;
		block = BitmapFactory.decodeResource(context.getResources(), id);
		this.y = dy;
	}

	public void draw(Canvas canvas ){
		
		canvas.save();
		int height = canvas.getHeight();
		canvas.translate(x, height - (block.getHeight()*y));
		canvas.drawBitmap(block, 0, 0,null);
		canvas.restore();
	}
	
	public void move(float dx){
		x+=dx;
	}

}
