package usbong.android.anglexploresuite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ImageView;

public class AngleTouchableObject extends TouchableObject {
	private float angleArm1 = 45.0f;
	private float angleArm2 = 45.0f;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private int angleArmLength; 
	private int vertexRadius;
	
	public AngleTouchableObject(Context c) {
		super(c);

		myImageView = new ImageView(c);		
/*		
		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
*/		

		((Activity)c).addContentView(myImageView, lp); 		
/*		
		offsetX=48;
		offsetY=48;
		offsetWidth=48;
		offsetHeight=48;	
*/
		offsetX=0;
		offsetY=0;
		offsetWidth=0;
		offsetHeight=0;	
		
		angleArm1 = 0.0f;
		angleArm2 = -45.0f;
		vertexRadius=8;
		
//		init();
	}
/*	
	public void init() {
	    if (myBitmap != null) {
		  myBitmap.recycle();
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_4444);//ARGB_8888
		  myCanvas = new Canvas(myBitmap);
		}
		catch(Exception e) {
			e.printStackTrace();
		}	  
	}
*/	
	@Override
	public void draw(Canvas c) {
		super.draw(c);
		
		paint.setColor(Color.RED);
    	paint.setStrokeWidth((float) 3.0);

    	angleArmLength = myMaxCanvasHeight/4;
    	
		if (!hasDoneInit) { //this means that draw hasn't been called yet
			//put these here because Android only knows the size of the imageview when it reaches draw(...)
			//Reference: http://stackoverflow.com/questions/17479391/get-imageview-width-and-height;
			//last accessed: 11 Dec. 2013; answer by Kaifei
			myWidth = angleArmLength*2;
			myHeight = angleArmLength*2;

//			this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);			
			hasDoneInit=true;
			
//		  myBitmap = Bitmap.createBitmap(myImageView.getWidth(),myImageView.getHeight(),Bitmap.Config.ARGB_8888);//4444);//ARGB_8888
//		  myBitmap = Bitmap.createBitmap(angleArmLength+offsetWidth,angleArmLength+offsetHeight,Bitmap.Config.ARGB_8888);//4444);//ARGB_8888
//	      myBitmap = Bitmap.createBitmap(c.getWidth(),c.getHeight(),Bitmap.Config.ARGB_8888);//4444);//ARGB_8888
		  myBitmap = Bitmap.createBitmap(myWidth,myHeight,Bitmap.Config.ARGB_8888);//4444);//ARGB_8888
		  myCanvas = new Canvas(myBitmap);
		}
  	
//		myCanvas.drawBitmap(myBitmap, new Matrix(), paint);
/*
		startX = (int) getX();
		startY = (int) getY();
		endX   = (int) (getX() + angleArmLength * Math.cos(Math.toRadians(angleArm1)));
		endY   = (int) (getY() + angleArmLength * Math.sin(Math.toRadians(angleArm1)));		
*/
		startX = 0+angleArmLength;
		startY = 0+angleArmLength;		
		endX   = (int) (startX + angleArmLength * Math.cos(Math.toRadians(angleArm1)));
		endY   = (int) (startY + angleArmLength * Math.sin(Math.toRadians(angleArm1)));		
		
//		myCanvas.drawLine(startX, startY, endX, endY, paint);
//		myImageView.draw(myCanvas);
		myCanvas.drawLine(startX, startY, endX, endY, paint);
		myCanvas.drawCircle(startX, startY, vertexRadius, paint);
/*		
		endX   = (int) (getX() + angleArmLength * Math.cos(Math.toRadians(angleArm2)));
		endY   = (int) (getY() + angleArmLength * Math.sin(Math.toRadians(angleArm2)));		
*/		
		endX   = (int) (startX + angleArmLength * Math.cos(Math.toRadians(angleArm2)));
		endY   = (int) (startY + angleArmLength * Math.sin(Math.toRadians(angleArm2)));		
		myCanvas.drawLine(startX, startY, endX, endY, paint);		
		
//		myImageView.draw(myCanvas);
//		myImageView.setImageDrawable(new BitmapDrawable(myContext.getResources(),myBitmap));

		myImageView.setImageBitmap(myBitmap);
//		myImageView.invalidate();
/*
		try{
			c.drawBitmap(myBitmap,
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					paint);//0,0,null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
*/		
	}
}