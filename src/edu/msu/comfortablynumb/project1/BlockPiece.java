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
	 * saves how much to offset the 7 given the # of blocks
	 */
	private float y;
	/**
	 * weight
	 */
	private int weight;
	/**
	 * rotation
	 */
	private float rotation;


	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

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
		block = BitmapFactory.decodeResource(context.getResources(), id);
		this.x = center - (float) block.getWidth()/2;
		this.y = dy+1;
		this.rotation = 0f;
	}

	public void draw(Canvas canvas ){
		canvas.save();
		canvas.restore();

		canvas.save();
		int height = canvas.getHeight();
		canvas.translate(x, height - (block.getHeight()*y));
		canvas.rotate(rotation);
		canvas.drawBitmap(block, 0, 0,null);
		canvas.restore();
	}

	public void move(float dx, float dy){
		x=dx;
	}

}
