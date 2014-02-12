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

	public boolean placed;
	
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
	 * vertical scrolling
	 */
	private float scrollingOffset;
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

	public int getWidth(){
		return block.getWidth();
	}

	public BlockPiece(Context context, int id, float dy, float center, int weight) {
		this.placed=false;
		this.id = id;
		this.weight = weight;
		this.x = center;
		block = BitmapFactory.decodeResource(context.getResources(), id);
		this.y = dy+1;
	}

	public void draw(Canvas canvas ){
		canvas.save();
		//canvas.translate(0, scrollingOffset);
		canvas.restore();
		
		canvas.save();
		int height = canvas.getHeight();
		canvas.translate(x, height - (block.getHeight()*y));
		canvas.drawBitmap(block, 0, 0,null);
		canvas.restore();
	}
	
	public void move(float dx, float dy){
		x=dx;
		//scrollingOffset= dy;
	}

}
