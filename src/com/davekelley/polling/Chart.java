package com.davekelley.polling;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

public class Chart extends SherlockFragmentActivity {
	ActionBar bar;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	Bundle extras;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager2);
		setContentView(mViewPager);
		
		bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("Charts"),
				ChartsFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Demographics"),
				DemographicsFragment.class, null);
	}
	
	public Bundle getExtras() {
		return extras;
	}

	public void setExtras(Bundle extras) {
		this.extras = extras;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.chartsmenu, menu);
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
		 Intent i = new Intent(this, Polling.class);
       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(i);
       //activity.startActivity(i);
		 	return true;
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
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}
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
			SharedPreferences sp;
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
	
}



