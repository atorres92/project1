package edu.msu.comfortablynumb.project1;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
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

}
