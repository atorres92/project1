package edu.msu.comfortablynumb.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BlockPiece {

	//The rate at which the blocks will fall down
	private float FALL_RATE = 1f;

	//How far the blocks have fallen
	private float amountFallen;

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

	//the X position of the block the stack is turning around
	private float turnBlockX;

	public float getTurnBlockX() {
		return turnBlockX;
	}



	public void setTurnBlockX(float turnBlockX) {
		this.turnBlockX = turnBlockX;
	}



	public float getTurnBlockY() {
		return turnBlockY;
	}



	public void setTurnBlockY(float turnBlockY) {
		this.turnBlockY = turnBlockY;
	}

	//the Y position of the block the stack is turning around
	private float turnBlockY;

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

	public int getHeight(){
		return block.getWidth();
	}


	public BlockPiece(Context context, int id, float dy, float center, int weight) {
		this.placed=false;
		this.id = id;
		this.weight = weight;
		block = BitmapFactory.decodeResource(context.getResources(), id);
		this.x = center - (float) block.getWidth()/2;
		this.y = dy+1;
		this.turnBlockX = 0.0f;
		this.turnBlockY = 0.0f;
		this.rotation = 0f;
	}

	public void draw(Canvas canvas ){
		canvas.save();
		canvas.restore();

		canvas.save();
		int height = canvas.getHeight();
		if(this.turnBlockX != 0.0f || this.turnBlockY != 0.0f){
			canvas.translate(turnBlockX,  height - (block.getHeight()*turnBlockY));
			canvas.rotate(rotation);
			if(rotation > 70 || rotation <-70){
				amountFallen += FALL_RATE;
				canvas.translate(-turnBlockX, -(height - (block.getHeight()*turnBlockY)));
			}
			else
				canvas.translate(-turnBlockX, -(height - (block.getHeight()*turnBlockY)));
		}
		if(rotation > 0)
			canvas.translate(amountFallen+ x, height - (block.getHeight()*y));
		else
			canvas.translate(-amountFallen+ x, height - (block.getHeight()*y));

		canvas.drawBitmap(block, 0, 0,null);
		canvas.restore();
	}

	public void move(float dx, float dy){
		x=dx;
	}

    public void centerX( float center ) {
        this.x = center - ( (float) block.getWidth() / 2f );
    }

}
