package usbong.android.anglexploresuite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

public class TouchableObject {
	protected Paint paint = new Paint();
	protected Bitmap myBitmap;
	protected Canvas myCanvas;

	protected float myPosX=-50;
	protected float myPosY=-50;
	protected int myWidth;
	protected int myHeight;	
	
	protected int myMaxCanvasWidth;
	protected int myMaxCanvasHeight;	
	
	protected ImageView myImageView;
	protected FrameLayout.LayoutParams lp;
	
//	protected boolean hasBeenInitiated;
	protected boolean isBeingMoved;

	protected int offsetX;
	protected int offsetY;	
	protected int offsetWidth;
	protected int offsetHeight;	

	private Matrix matrix;
	private float rotationDegrees=0.0f;
	private float prevAngle=0.0f;
	
	protected boolean hasDoneInit;
	protected Context myContext;
	
	public TouchableObject(Context c) {
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		myImageView = new ImageView(c);
		//do this in subclass
//		myImageView.setImageDrawable(c.getResources().getDrawable(R.drawable.bottle));
/*
		myWidth = myImageView.getWidth();
		myHeight = myImageView.getHeight();
		((Activity)c).addContentView(myImageView, lp); 
*/		
		hasDoneInit=false;
		myContext = c;
	}
	
	public void draw(Canvas c) {	
/*
		//put these here because Android only knows the size of the imageview when it reaches draw(...)
		//Reference: http://stackoverflow.com/questions/17479391/get-imageview-width-and-height;
		//last accessed: 11 Dec. 2013; answer by Kaifei
		myWidth = myImageView.getWidth();
		myHeight = myImageView.getHeight();
*/		
    	lp.setMargins((int)myPosX, (int)myPosY, 0, 0);
        myImageView.setLayoutParams(lp);
        
/*
        matrix = new Matrix();
		matrix.postRotate(rotationDegrees, myImageView.getWidth()/2, myImageView.getHeight()/2);
		myImageView.setImageMatrix(matrix);
*/
//        myImageView.setVisibility(View.VISIBLE);        
	}
	
	public void setXYPos(float x, float y) {
		myPosX = x;
		myPosY = y;
	}
/*	
	public boolean getHasBeenInitiated() {
		return hasBeenInitiated;
	}
*/	
	public boolean hasIntersectedWithPoint(float x, float y) {
/*//original
		//if x and y positions are outside the bounding box of this object
		if (((x < myPosX) || (x > myPosX + myWidth)) ||
			((y < myPosY) || (y > myPosY + myHeight))) {
			return false;
		}
		return true;
*/	
		//if x and y positions are inside the bounding box of this object
		if (((x >= myPosX-offsetX) && (x <= myPosX + myWidth+offsetWidth)) &&
			((y >= myPosY-offsetY) && (y <= myPosY + myHeight+offsetHeight))) {
			return true;
		}
		return false;		
	}

	public boolean hasIntersectedWithRectangle(float x, float y, int w, int h) {
/*
		Log.d("x and y",x+" and "+y);
		Log.d("w and h",w+" and "+h);
		Log.d("myPosX and myPosY",myPosX+" and "+myPosY);
		Log.d("myWidth and myHeight",myWidth+" and "+myHeight);		
*/
		//if the rectangle is outside the bounding box of this object
		if (((myPosX+myWidth < x) || (myPosX > x + w)) ||
			((myPosY+myHeight < y) || (myPosY > y + h))) {
				return false;
			}
		return true;
	}
	
	public boolean getIsVisible() {
		if (myImageView.getVisibility()==View.VISIBLE) {
			return true;
		}
		return false;
	}
	
	public void setVisible(int v) {
        myImageView.setVisibility(v);		
	}
	
	public int getWidth() {
		return myWidth;
	}
	
	public int getHeight() {
		return myHeight;
	}	
	
	public float getX() {
		return myPosX;
	}
	
	public float getY() {
		return myPosY;
	}
	
	public void removeImageView(ViewGroup root) {
		root.removeView(myImageView);
	}
	
	public void setIsBeingMoved(boolean b) {
		isBeingMoved =true;		
	}
	
	public boolean isBeingMoved() {
		return isBeingMoved;
	}
	
	public void rotate(float angle) {
		matrix = new Matrix();

		if (prevAngle==angle) {
			return;
		}
		else {
			prevAngle=angle;		
		}
		rotationDegrees=(rotationDegrees-angle/4)%360;		//do divide by 4 to make the angle rotate less quickly
/*		
		matrix.postRotate(rotationDegrees, myImageView.getWidth()/2, myImageView.getHeight()/2);
		myImageView.setScaleType(ScaleType.MATRIX);
		myImageView.setImageMatrix(matrix);
*/
		matrix.postRotate(rotationDegrees, myImageView.getWidth()/2, myImageView.getHeight()/2);
		myImageView.setScaleType(ScaleType.MATRIX);
		myImageView.setImageMatrix(matrix);

		Log.d(">>>>","inside rotate(...)");
	}
	
	public void setMaxCanvasWidthHeight(int w, int h) {
		this.myMaxCanvasWidth = w;
		this.myMaxCanvasHeight = h;		
	}
	
	//Reference: http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has-a-clean-canvas
	//Last accessed on: July 2, 2010
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
	    if (myBitmap != null) {
		  myBitmap.recycle();
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_8888);//4444);//ARGB_8888
		  myCanvas = new Canvas(myBitmap);
		  myImageView.draw(myCanvas);

//		  clearCanvas();
//    	  Paint p = new Paint();
//    	  p.setColor(0xFF6A9D69); //blackboard green
//    	  myCanvas.drawRect(0, 0, myBitmap.getWidth(), myBitmap.getHeight(), p);

		}
		catch(Exception e) {
			e.printStackTrace();
		}	  
	}

	public void destroy() {
        if (myBitmap != null) {
            myBitmap.recycle();
        }
    }	
}