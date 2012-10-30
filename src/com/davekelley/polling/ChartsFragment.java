package com.davekelley.polling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class ChartsFragment extends SherlockFragment {
	Chart activity;
	String title;
	String id;
	String category;
	ActionBar bar;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (Chart) getActivity();
		View v = inflater.inflate(R.layout.chartfragment, container, false);
	Bundle extras = activity.getExtras();
	title = extras.getString("TITLE");
	id = extras.getString("ID");
	category = extras.getString("CATEGORY");
	//setTitle(category + " #" + id);
	TextView chartTitleTextView = (TextView) v.findViewById(R.id.chartTitleTextView);
	chartTitleTextView.setText(title);
	

	
	return v;
	}
	

}
