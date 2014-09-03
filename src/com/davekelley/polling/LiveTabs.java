package com.davekelley.polling;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LiveTabs extends DialogFragment implements
		DialogInterface.OnClickListener, OnMultiChoiceClickListener {
	
	String URLhome;
	String Title;
	String type;
	TextView textView;
	Activity activity;
	Polling polling;
	List<NameValuePair> params;
	Boolean[] backgroundState;      
	Button tabsButton;    

	public LiveTabs(String type, TextView textView, DemoPanel activity) {
		this.type = type;
		this.textView = textView;
		this.activity = activity;
		backgroundState = new Boolean[activity.getResources().getStringArray(R.array.backgroundArray).length];
	}
	
	public LiveTabs(String type2, Button button, Polling activity2) {
		this.type = type2;
		tabsButton = button;
		this.activity = activity2;
		backgroundState = new Boolean[activity.getResources().getStringArray(R.array.backgroundArray).length];
	}

	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(true);
        //int style = DialogFragment.STYLE_NORMAL, theme = 0;
        //setStyle(style, theme);
        
    }
	
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View dialogLayout = inflater.inflate(R.layout.dialog, null);
	        AlertDialog.Builder builder;

	        if (tabsButton != null) {
	        switch (tabsButton.getId()) {
	        case (R.id.tabsButton):
		        builder = new AlertDialog.Builder(activity);
	        builder.setTitle(type);
	        builder.setNegativeButton("Cancel", this);
	        builder.setView(dialogLayout);
	        builder.setMultiChoiceItems(activity.getResources().getStringArray(R.array.tabsArray),
					null, new OnMultiChoiceClickListener() {
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							Log.v("", activity.getResources().getStringArray(R.array.tabsArray)[which].toString());
							
						}
	        	
	        });
		builder.setPositiveButton("Okay", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}			
		});
	        	return builder.create();
	        }
	        }
			return null;
	    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		Log.v(Integer.toString(which), Boolean.toString(isChecked));
		backgroundState[which] = isChecked;
	}
	 
}
