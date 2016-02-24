package usbong.android.anglexploresuite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ScenarioActivity extends Activity {
	protected ImageView myImageView;
	protected Intent toNextIntent;
	protected Intent toPreviousIntent;
	protected boolean hasCompletedTask;

//	protected GestureDetector gdt = new GestureDetector(new GestureListener(this));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenario_4);	
/*//originally commented out
		ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
		rootView.removeAllViews();
*/		
//		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		
/*		
		//default; change these in Scenario_...
		toNextIntent = new Intent().setClass(this, Scenario_2_Activity.class);
		toPreviousIntent = new Intent().setClass(this, MainActivity.class);		
*/				
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void init() {				
	}
	
	public void returnToMainMenu() {
		Intent toMainMenuIntent = new Intent().setClass(this, MainActivity.class);
		toMainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(toMainMenuIntent);
	}
/*
	public void goToAbout() {
		Intent toAboutIntent = new Intent().setClass(this, AboutActivity.class);
//		toAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		finish();
		startActivity(toAboutIntent);
	}
*/
	public void processNextScreen() {
		toNextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(toNextIntent);
	}

	public void processPreviousScreen() {
		toPreviousIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(toPreviousIntent);
	}

	//override these
	public void processTopToBottomSwipe() {		
	}
	
	public void processBottomToTopSwipe() {		
	}
	
	public void processLeftToRightSwipe() {		
		processPreviousScreen();
	}

	public void processRightToLeftSwipe() {		
		processNextScreen();
	}
}
