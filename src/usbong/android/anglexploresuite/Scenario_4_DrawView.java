package usbong.android.anglexploresuite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import usbong.android.utils.RotationGestureDetector;

/*
 * Reference: http://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android;
 * last accessed: 20 Nov. 2013
 */
public class Scenario_4_DrawView extends View implements OnTouchListener, RotationGestureDetector.OnRotationGestureListener {
    Paint paint = new Paint();

	private long mMoveDelay = 600;
	private int lastPosX;
	private int lastPosY;
	private int currPosX;
	private int currPosY;

	private Bitmap myBitmap;
	private Canvas myCanvas;

	private int myMaxWidth;
	private int myMaxHeight;	
	private int myWidth;
	private int myHeight;

	
	private int[][] answer;
	private int[][] correctAnswer;
	
	private boolean hasAnswered;
	private boolean isAnswerCorrect;
	
	private int numOfBoxesHorizontal=13;
	private int numOfBoxesVertical=8;

	/*
	private ImageView myAngleImageView;
	private int myAnglePosX=-50;
	private int myAnglePosY=-50;
*/
	private AngleTouchableObject[] myAngle;
	private final int totalAngles = 1; //7
	private boolean hasInitAngles;
//	private boolean isMovingAnObject;
	
	private float bagPosX;
	private float bagPosY;
	private int bagWidth;
	private int bagHeight;

	private ImageView myScenarioImageView;
	
	//Reference: http://stackoverflow.com/questions/5255184/android-and-setting-width-and-height-programmatically-in-dp-units;
	//last accessed: Dec. 10, 2013; answer by Robby Pond
	final float scale = getContext().getResources().getDisplayMetrics().density;
	
	private FrameLayout.LayoutParams lp;
//	private RelativeLayout.LayoutParams lp;
	
	private Button hintButton;
	private Button naviButton;
	
	private RotationGestureDetector mRotationDetector;

    public Scenario_4_DrawView(Context context) {
        super(context);
		this.setOnTouchListener(this);
		mRotationDetector = new RotationGestureDetector(this);
//		myScenarioImageView = (ImageView) ((Activity)context).findViewById(R.id.scenario_imageview);				

/*		
		lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		myScenarioImageView = new ImageView(context);
		myScenarioImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.scenario_4));
		((Activity)context).addContentView(myScenarioImageView, lp); 		
*/
		myAngle = new AngleTouchableObject[totalAngles];
		for (int i=0; i<totalAngles; i++) {
			myAngle[i] = new AngleTouchableObject(context);
			myAngle[i].setVisible(View.INVISIBLE);
		}
/*		
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
*/
		lastPosX=-1;//getWidth()/2;
		lastPosY=-1;//getHeight()/2;
		currPosX=-1;
		currPosY=-1;
/*
		myAngleImageView = new ImageView(context);
		myAngleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bottle));
		
		((Activity)context).addContentView(myAngleImageView, lp); 
*/
/*		
		hasAnswered=false;
		isAnswerCorrect=false;
*/		
    	hintButton = (Button)((Activity)context).findViewById(R.id.hint_button);		
    	hintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AlertDialog.Builder prompt = new AlertDialog.Builder(new ContextThemeWrapper(Scenario_1_Activity.this, 5));
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_4_Activity.instance);
				prompt.setTitle("Hint");
				prompt.setMessage(R.string.hint_scenario_4);
				prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				prompt.show();

			}
    	});

    	naviButton = (Button)((Activity)context).findViewById(R.id.navi_button);		
    	naviButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder prompt = new AlertDialog.Builder(Scenario_4_Activity.instance);
				prompt.setTitle("Navigation");
				prompt.setMessage(R.string.navigation);
				prompt.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_4_Activity.instance.returnToMainMenu();
					}
				});/*
				prompt.setNegativeButton("About", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Scenario_4_Activity.instance.goToAbout();
					}
				});*/
				prompt.show();

			}
    	});

		update();
    }

    public void removeBottles(ViewGroup root) {
		for (int i=0; i<totalAngles; i++) {
			if (myAngle[i]!=null) {
				myAngle[i].removeImageView(root);
			}
		}
//		rootView.removeAllViews();
    }
    
    public boolean isTaskCompleted() {
    	return isAnswerCorrect;
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	Log.d(">>>inside","onDraw");
        paint.setColor(Color.RED);
    	paint.setStrokeWidth((float) 3.0);
        //        canvas.drawLine(0, 0, 20, 20, paint);
//        canvas.drawLine(20, 0, 0, 20, paint);
    	myMaxWidth = canvas.getWidth();
    	myMaxHeight = canvas.getHeight();
/*    	
    	Log.d(">>>>>>canvas.getWidth()",""+canvas.getWidth());
    	Log.d(">>>>>>canvas.getHeight()",""+canvas.getHeight());
*/    	
    	myWidth = myMaxWidth/numOfBoxesHorizontal;
    	myHeight = myMaxHeight/numOfBoxesVertical;
/*
    	for (int i=0; i<numOfBoxesHorizontal+1; i++) { //why +1? for the right-most line
    		//draw vertical lines
        	canvas.drawLine(myWidth*i, 0, myWidth*i, myMaxHeight, paint);    		
    	}
    	
    	for (int i=0; i<numOfBoxesVertical+1; i++) { //why +1? for the bottom line
	    	//draw horizontal lines
	    	canvas.drawLine(0, myHeight*i, myMaxWidth, myHeight*i, paint);    		        	
    	}
*/
    	//init bag
    	bagPosX=myWidth*0;
    	bagPosY=myHeight*4;
    	bagWidth=myWidth*2;
    	bagHeight=myHeight*2;
/*
		try{
			canvas.drawBitmap(myBitmap,
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					new Rect(0,0, myBitmap.getWidth(),myBitmap.getHeight()),
					paint);//0,0,null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
*/
		if (!hasInitAngles) {
			resetBottlesPosition();
	    	for (int i=0; i<totalAngles; i++) {
				myAngle[i].setVisible(View.VISIBLE);
	    	}
			hasInitAngles=true;
		}
    	
    	for (int i=0; i<totalAngles; i++) {
			if (!isAnswerCorrect) {
				myAngle[i].draw(canvas);
			}
		}
/*
    	for (int i=0; i<totalAngles; i++) {
			Log.d(">>>>>>","inside for loop");			
			
			if (!hasInitAngles) {
				Log.d(">>>>>>","!hasInitAngles");			
				resetBottlesPosition();
				myAngle[i].setVisible(View.VISIBLE);
				
				if (i==totalAngles-1) { //if this is the last bottle
					hasInitAngles=true;
				}
			}
			if (!isAnswerCorrect) {
				Log.d(">>>>>>","!isAnswerCorrect");			
				myAngle[i].draw(canvas);
			}
		}
*/    	
//        if (hasAnswered) {
			Log.d(">>>>>>>> is answer correct?:",""+isAnswerCorrect);
			
			if (isAnswerCorrect) {
//				resetSettings();
/*
				isAnswerCorrect=false;
				hasInitAngles=false;
*/				
				//so that the bus's position doesn't go back to the start position while the scenario is transitioning to the next scenario
//		        myAngleImageView.setVisibility(View.INVISIBLE);
				
				//do resetAnswers(), etc so that if the back key is pressed it'll return to the previous scenario
				((ScenarioActivity)this.getContext()).processNextScreen();
			}
			else {
//				resetSettings();
			}
//		}
    }
    
    public void resetSettings() {    	
    	Log.d(">>>inside","resetSettings()");

    	resetBottlesPosition();	//this should come before isAnswerCorrect
		hasAnswered=false;
		isAnswerCorrect=false;
//		resetAnswers();
//		this.onSizeChanged(this.getWidth(),this.getHeight(),0,0);
//		this.invalidate();
		
		hasInitAngles=false;
    }
    
    public void resetBottlesPosition() {
    	myAngle[0].setMaxCanvasWidthHeight(myMaxWidth, myMaxHeight);
		myAngle[0].setXYPos(myWidth*8, myHeight*5);
		Log.d(">>>>>", "resetBottlesPosition()");
/*		
		float angle = 45.0f;
		int startX = currPosX;
		int startY = currPosY;
		int endX   = (int) (currPosX + 40 * Math.sin(Math.toRadians(angle)));
		int endY   = (int) (currPosY + 40 * Math.cos(Math.toRadians(angle)));
*/		
/*    	
    	int bottle_set_1_offsetY = (int) (17 * scale + 0.5f);
		int bottle_set_2_offsetY = (int) (9 * scale + 0.5f);
		int bottle_set_3_offsetY = (int) (36 * scale + 0.5f);

		int est_bottle_width = (int) (28 * scale + 0.5f);

		myAngle[0].setXYPos(myWidth*3-est_bottle_width, myHeight*1 + bottle_set_1_offsetY);
		myAngle[1].setXYPos(myWidth*3-est_bottle_width*2, myHeight*1 + bottle_set_1_offsetY);

		myAngle[2].setXYPos(myWidth*8, myHeight*5 + bottle_set_2_offsetY);
		myAngle[3].setXYPos(myWidth*8-est_bottle_width, myHeight*5 + bottle_set_2_offsetY);				

		myAngle[4].setXYPos(myWidth*12-est_bottle_width*2, 0 + bottle_set_3_offsetY);
		myAngle[5].setXYPos(myWidth*12-est_bottle_width*3, 0 + bottle_set_3_offsetY);				
		myAngle[6].setXYPos(myWidth*12-est_bottle_width*4, 0 + bottle_set_3_offsetY);				    	
*/		
    }

    /*    
    public void resetAnswers() {
		//set 0 as default
		for(int i=0; i<numOfBoxesHorizontal; i++) {
			for(int k=0; k<numOfBoxesVertical; k++) {
				answer[i][k]=0;
			}
		}
    }
*/   
/*
	//Reference: http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has-a-clean-canvas
	//Last accessed on: July 2, 2010
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
	    if (myBitmap != null) {
		  myBitmap.recycle();
        }
		try{
		  myBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_4444);
		  myCanvas = new Canvas(myBitmap);

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
*/    
	public void update() {
        long now = System.currentTimeMillis();

        //TODO change this, because "now" is always greater than mMoveDelay)
        if (now  > mMoveDelay) {
        	//do updates
        }
        mRedrawHandler.sleep(mMoveDelay);
	}
	
	/**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    private class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
        	Scenario_4_DrawView.this.update();
        	Scenario_4_DrawView.this.invalidate();
        }

        public void sleep(long delayMillis) {
                this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    
	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		System.out.println("TOUCHED!");
		
		mRotationDetector.onTouchEvent(me);
		
		if ((me.getX() >= getWidth() - hintButton.getWidth()) &&
				(me.getY() <= 0 + hintButton.getHeight())){
				return false;
		}
		
		if ((me.getX() >= getWidth() - naviButton.getWidth()) &&
				(me.getY() >= getHeight() - naviButton.getHeight())){
				return false;
		}
		
		if (!hasAnswered) {
			int meAction = me.getAction();
			if (lastPosX==-1) {
			  lastPosX = (int) me.getX();
			}
			if (lastPosY==-1) {
			  lastPosY = (int) me.getY();
			}
			
			switch(meAction) {
				case MotionEvent.ACTION_UP:
					lastPosX = -1;
					lastPosY = -1;
					currPosX = -1;
					currPosY = -1;
/*					//no need to do this in AngleXploreSuite
		    		for (int i=0; i<totalAngles; i++) {
		    			//check if the bottle is in the bag bag
		    			//where its [x][y] = [0][4] and width and height are 2 boxes
		    			if (myAngle[i].hasIntersectedWithRectangle(bagPosX,
		    														bagPosY,
		    														bagWidth,
		    														bagHeight)) {
		    				myAngle[i].setVisible(View.INVISIBLE);
		    			}		    			
		    		}
*/
		    		//check if all the bottles have been placed in the bag
		    		boolean hasRemainingVisibleBottle=false;
		    		for (int i=0; i<totalAngles; i++) {
		    			if (myAngle[i].getIsVisible()) {
		    				hasRemainingVisibleBottle=true;
				    		myAngle[i].setIsBeingMoved(false);
		    			}
		    		}
		    		if (!hasRemainingVisibleBottle) {
						isAnswerCorrect=true;										    			
		    		}
	/*
					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							Log.d(">>>>answer["+i+"]["+k+"]=",""+answer[i][k]);
						}
					}

					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							Log.d(">>>>correctAnswer["+i+"]["+k+"]=",""+correctAnswer[i][k]);
						}
					}
					
					//check if correct answer
					for(int i=0; i<numOfBoxesHorizontal; i++) {
						for(int k=0; k<numOfBoxesVertical; k++) {
							if ((correctAnswer[i][k]==1) && (answer[i][k]!=correctAnswer[i][k])){
								isAnswerCorrect=false;								
							}
						}
					}				
					
					if ((answer[2][1]==1) ||
						(answer[3][1]==1) ||
						(answer[5][1]==1) ||
						(answer[6][1]==1) ||
						(answer[5][3]==1) ||
						(answer[6][3]==1)) {
						isAnswerCorrect=false;						
					}
*/					

//					hasAnswered=true;
					this.invalidate();
					Log.d(">>>>>>>DONE","DONE");					
					return true;
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					currPosX = (int) me.getX();
					currPosY = (int) me.getY();

		        	//check if there is an intersection between currPosX and currPosY AND the bottles
		    		for (int i=0; i<totalAngles; i++) {
		    			if (myAngle[i].getIsVisible()) {
/*		    				
			    			if ((myAngle[i].hasIntersectedWithPoint(currPosX, currPosY)) ||
			    				(myAngle[i].hasIntersectedWithPoint(lastPosX, lastPosY))) {
//			    				Log.d(">>>> currPos intersected!","Hooray!");
			    				//allow user to move the bottle object
			    				myAngle[i].setXYPos(currPosX-myAngle[i].getWidth()/2,currPosY-myAngle[i].getHeight()/2);
			    				break;		    				
			    			}
*/
			    			if (myAngle[i].hasIntersectedWithPoint(currPosX, currPosY)) {
//				    			Log.d(">>>> currPos intersected!","Hooray!");
			    				//allow user to move the bottle object
			    				myAngle[i].setXYPos(currPosX-myAngle[i].getWidth()/2,currPosY-myAngle[i].getHeight()/2);
			    				myAngle[i].setIsBeingMoved(true);
			    				break;		    				
				    		}
		    			}
			    			/*
			    			else if (myAngle[i].hasIntersectedWithPoint(lastPosX, lastPosY)) {
    				 			myAngle[i].setXYPos(lastPosX-myAngle[i].getWidth()/2,lastPosY-myAngle[i].getHeight()/2);
    				 			break;
		    				}*/
		    		}

					this.invalidate();
					return true;
			}
		}
		return false;
	}
	
	//reference: http://stackoverflow.com/questions/10682019/android-two-finger-rotation;
	//answer by: leszek.hanusz, edited by: Gopal Singh Sirvi
	//last accessed: 20160220
	@Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
        float angle = rotationDetector.getAngle();
        Log.d("RotationGestureDetector", "Rotation: " + Float.toString(angle));
		for (int i=0; i<totalAngles; i++) {
			if (myAngle[i].getIsVisible()) {
				if (myAngle[i].isBeingMoved()) {
					myAngle[i].rotate(angle);
				}
			}
		}
		this.invalidate();
    }
}