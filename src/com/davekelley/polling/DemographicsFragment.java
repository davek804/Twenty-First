package com.davekelley.polling;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DemographicsFragment extends Fragment {
	//Chart activity;
	String title;
	String id;
	String category;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//activity = (Chart) getActivity();
		View v = inflater.inflate(R.layout.demofragment, container, false);
	//Bundle extras = activity.getExtras();
	TextView title = (TextView) v.findViewById(R.id.demoFragmentTitleTextView);
	//title.setText(extras.getString("TITLE"));
	return v;
	}
}

