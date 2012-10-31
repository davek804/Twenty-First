package com.davekelley.polling;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import library.DatabaseHandler;
import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class Polling extends SherlockFragmentActivity {
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	private DatabaseHandler db;
	SharedPreferences sp;
	Toast toast;
	ActionBar bar;
	/*String [] tabs = {"Econ", "Elections", "Geo", "Politics", "Science", "Finance", "Religion", 
			"Military", "International" };*/
	
	/*First method that runs when the app launches. Establishes the ActionBar,
	 * instantiates each tab on screen, and builds a ViewPager that allows the user
	 * to swipe anywhere on the screen to move between tabs. Lastly it instantiates a 
	 * class-wide dataBase that stores user preferences and responses.*/
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(getApplicationContext());
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);
/*		if(savedInstanceState != null) {
		mViewPager.setCurrentItem(savedInstanceState.getInt("lastPosition"));
		//savedInstanceState.get
		}*/
		
		bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
				
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		/*try {
			mTabsAdapter.buildTabs();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		mTabsAdapter.addTab(bar.newTab().setText(R.string.login),
				LoginFragment.class, null);
		//mTabsAdapter.addTab(bar.newTab().setText("Geographics"),
				//GeoFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.economics),
				EconFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.elections),
				ElectionsFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.politics),
				PoliticsFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.science),
				ScienceFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.finance),
				FinanceFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.religion),
				ReligionFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.military),
				MilitaryFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(R.string.international),
				InternationalFragment.class, null);	
		
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

	public void onResume() {
		super.onResume();
		SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
		//Log.v("resume", Integer.toString(sp.getInt("lastPosition", 0)));
		mViewPager.setCurrentItem(sp.getInt("lastPosition", 0));	
	}
	
	

	protected void onPause() {
		super.onPause();			
		sp = getSharedPreferences("prefs", MODE_PRIVATE);
		SharedPreferences.Editor preferencesEditor = sp.edit();
		int lastPosition = mViewPager.getCurrentItem();
		preferencesEditor.putInt("lastPosition", lastPosition);
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
	
	/*This is an anonymous inner class that is used in onCreate() to build the tabs,
	 * add a TabsAdapter as well as a ViewPager. It is the core of ensuring the UI builds
	 * properly and without error.
	 */
	public class TabsAdapter extends FragmentPagerAdapter
	implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private Polling activity;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}
		/*Constructor method that adds a TabsAdapter to each tab that is created.
		 * It also adds the ViewPager to each tab so that the user can swipe to change tabs.
		 */
		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			this.activity = (Polling) activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}
		
		/*This is the method I've been trying to use to solve the problem, but it's not really cutting it!*/
		
/*		public void buildTabs() throws ClassNotFoundException {	
			String [] tabs = {"Econ", "Elections", "Geo", "Politics", "Science", "Finance", "Religion", 
					"Military", "International" };
			final String resource = "R.string.";			
			mTabsAdapter.addTab(bar.newTab().setText("Login"),
					LoginFragment.class, null, "Login");
			for (int j = 0; j < tabs.length; j++) {
				String res = resource + tabs[j];
				String clas = tabs[j] + "Fragment";
				String total = "com.davekelley.polling." + clas;
			mTabsAdapter.addTab(bar.newTab().setText(tabs[j]),
					CategoryFragment.class, null, tabs[j]);
			}			
		}*/
		
		/*A fairly simple method that sets the TabInfo for each tab so that the TabsAdapter
		 * knows which class the tab that is being added actually belonds to. It also updates
		 * the UI interface when each tab is added. 
		 */
		
		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}	

		public int getCount() {
			return mTabs.size();
		}
		/*A method that is used in other classes to allow each tab Fragment to 
		 * access its inherited methods from a mother-class, in this case, SherlockFragment
		 */
		
		public int getPosition(SherlockFragment fragment) {
			for (int j = 1; j < mTabs.size(); j++) {
				TabInfo info = (TabInfo) mActionBar.getTabAt(j).getTag();
				/*if (info.title.matches(mTabs.get(j).title)) {
					return j;
				}*/
			}
			return -1;
			
			
		}
		public SherlockFragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return (SherlockFragment)Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}
		/*This method reads the user's selection for a new tab and sets that tab as
		 * the new current focus.*/
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
			selectInSpinnerIfPresent(position, true);
		}
		
		private void selectInSpinnerIfPresent(int position, boolean animate) {
		    try {
		        View actionBarView = findViewById(R.id.abs__action_bar);
		        if (actionBarView == null) {
		            int id = getResources().getIdentifier("action_bar", "id", "android");
		            actionBarView = findViewById(id);
		        }

		        Class<?> actionBarViewClass = actionBarView.getClass();
		        Field mTabScrollViewField = actionBarViewClass.getDeclaredField("mTabScrollView");
		        mTabScrollViewField.setAccessible(true);

		        Object mTabScrollView = mTabScrollViewField.get(actionBarView);
		        if (mTabScrollView == null) {
		            return;
		        }

		        Field mTabSpinnerField = mTabScrollView.getClass().getDeclaredField("mTabSpinner");
		        mTabSpinnerField.setAccessible(true);

		        Object mTabSpinner = mTabSpinnerField.get(mTabScrollView);
		        if (mTabSpinner == null) {
		            return;
		        }

		        Method setSelectionMethod = mTabSpinner.getClass().getSuperclass().getDeclaredMethod("setSelection", Integer.TYPE, Boolean.TYPE);
		        setSelectionMethod.invoke(mTabSpinner, position, animate);

		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    } catch (NoSuchFieldException e) {
		        e.printStackTrace();
		    } catch (NoSuchMethodException e) {
		        e.printStackTrace();
		    } catch (InvocationTargetException e) {
		        e.printStackTrace();
		    }
		}
		
		public void onPageScrollStateChanged(int state) {}
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
		/* This is the method that actually draws the newest tab onto the screen when
		 * it is selected.*/
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			mViewPager.setCurrentItem(tab.getPosition());

			sp = getSharedPreferences("prefs", MODE_PRIVATE);
			SharedPreferences.Editor preferencesEditor = sp.edit();
			preferencesEditor.putInt("lastPosition", mViewPager.getCurrentItem());
			preferencesEditor.commit();
		}
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {}
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {}
	}
	
	public DatabaseHandler getDB() {
		return db;
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("polling", "onSIS");
		outState.putInt("tab", mViewPager.getCurrentItem());
	}

	public String getThisTab(SherlockFragment fragment) {
		int n = mTabsAdapter.getPosition(fragment);
		return bar.getTabAt(n).getText().toString();
	}
}