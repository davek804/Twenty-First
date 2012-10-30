package com.davekelley.polling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockFragment;

public class DemographicsFragment extends SherlockFragment {
	Chart activity;
	String title;
	String id;
	String category;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (Chart) getActivity();
		View v = inflater.inflate(R.layout.demofragment, container, false);
	Bundle extras = activity.getExtras();
	TextView title = (TextView) v.findViewById(R.id.demoFragmentTitleTextView);
	title.setText(extras.getString("TITLE"));
	return v;
	}
}

