package com.davekelley.polling;

import library.DatabaseHandler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Polling extends Activity {
//	#ViewPager mViewPager;
//	TabsAdapter mTabsAdapter;
	private DatabaseHandler db;
	SharedPreferences sp;
	Toast toast;
	ActionBar bar;
	String [] tabs = {"Econ", "Elections", "Geo", "Politics", "Science", "Finance", "Religion", 
			"Military", "International" };
	private DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	
	/*First method that runs when the app launches. Establishes the ActionBar,
	 * instantiates each tab on screen, and builds a ViewPager that allows the user
	 * to swipe anywhere on the screen to move between tabs. Lastly it instantiates a 
	 * class-wide dataBase that stores user preferences and responses.*/
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(getApplicationContext());
		setContentView(R.layout.prelogin);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, tabs));
		
		//mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		
/*		if(savedInstanceState != null) {
		mViewPager.setCurrentItem(savedInstanceState.getInt("lastPosition"));
		//savedInstanceState.get
		}*/
		
		bar = getActionBar();
		//bar.setNavigationMode(ActionBar);
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayShowHomeEnabled(true);
				
//		mTabsAdapter = new TabsAdapter(this, mViewPager);
//
//		
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.login),
//				LoginFragment.class, null);
//		//mTabsAdapter.addTab(bar.newTab().setText("Geographics"),
//				//GeoFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.economics),
//				EconFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.elections),
//				ElectionsFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.politics),
//				PoliticsFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.science),
//				ScienceFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.finance),
//				FinanceFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.religion),
//				ReligionFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.military),
//				MilitaryFragment.class, null);
//		mTabsAdapter.addTab(bar.newTab().setText(R.string.international),
//				InternationalFragment.class, null);	
		
	}
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Quit?")
            .setMessage("Really quit?")
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Stop the activity
                    finish();    
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
	
	public void userLoggedOut() {
		this.recreate();
	}

	public void onResume() {
		super.onResume();
		SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
		//Log.v("resume", Integer.toString(sp.getInt("lastPosition", 0)));
		//mViewPager.setCurrentItem(sp.getInt("lastPosition", 0));	
	}
	
	

	protected void onPause() {
		super.onPause();			
		sp = getSharedPreferences("prefs", MODE_PRIVATE);
		SharedPreferences.Editor preferencesEditor = sp.edit();
		//int lastPosition = mViewPager.getCurrentItem();
		//preferencesEditor.putInt("lastPosition", lastPosition);
		preferencesEditor.commit();

	}
	
	public void onStop() {
		super.onStop();
/*		sp = getSharedPreferences("prefs", MODE_PRIVATE);
		SharedPreferences.Editor preferencesEditor = sp.edit();
		int lastPosition = mViewPager.getCurrentItem();
		preferencesEditor.putInt("lastPosition", lastPosition);
		preferencesEditor.commit();*/
	}
	
/*	public boolean onCreateOptionsMenu(Menu menu) {
		menu.setGroupVisible(0, false);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;	
	}
	*/
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
		 }
	}
	
	public DatabaseHandler getDB() {
		return db;
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("polling", "onSIS");
		//outState.putInt("tab", mViewPager.getCurrentItem());
	}

}