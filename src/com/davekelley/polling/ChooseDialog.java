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
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooseDialog extends DialogFragment implements
DialogInterface.OnClickListener, OnMultiChoiceClickListener {

	String type;
	EditText editText;
	Activity activity;
	Boolean[] backgroundState;

	public ChooseDialog(String type, EditText editText, DemoPanel activity) {
		this.type = type;
		this.editText = editText;
		//this.activity = activity;
		//backgroundState = new Boolean[activity.getResources().getStringArray(R.array.backgroundArray).length];

	}
/*
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setCancelable(true);
		//int style = DialogFragment.STYLE_NORMAL, theme = 0;
		//setStyle(style, theme);

	}*/

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogLayout = inflater.inflate(R.layout.dialog, null);
		AlertDialog.Builder builder;

		switch (editText.getId()) {
		case (0) :
		ListView list = (ListView) dialogLayout.findViewById(R.id.listView1);
		list.setAdapter(new ArrayAdapter<String>(activity, R.layout.dialoglist, 
				activity.getResources().getStringArray(R.array.ageArray)));
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				editText.setText(activity.getResources().getStringArray(R.array.ageArray)[arg2]);
				dismiss();
			}	
		});
		builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
			new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);
		//dialogLayout.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		builder.setNegativeButton("Cancel", this);
		builder.setView(dialogLayout);

		return builder.create();

		case (1) :
			ListView edulist = (ListView) dialogLayout.findViewById(R.id.listView1);
		edulist.setAdapter(new ArrayAdapter<String>(activity, R.layout.dialoglist, 
				activity.getResources().getStringArray(R.array.eduArray)));
		edulist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				editText.setText(activity.getResources().getStringArray(R.array.eduArray)[arg2]);
				dismiss();
			}	
		});
		builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
			new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);
		builder.setNegativeButton("Cancel", this);
		builder.setView(dialogLayout);

		return builder.create();

		case (2) :
			ListView maritallist = (ListView) dialogLayout.findViewById(R.id.listView1);
		maritallist.setAdapter(new ArrayAdapter<String>(activity, R.layout.dialoglist, 
				activity.getResources().getStringArray(R.array.maritalArray)));
		maritallist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				editText.setText(activity.getResources().getStringArray(R.array.maritalArray)[arg2]);
				dismiss();
			}	
		});
		builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
			new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);
		builder.setNegativeButton("Cancel", this);
		builder.setView(dialogLayout);

		return builder.create();

		case (3) :
			builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
				new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);

			for (int j = 0; j < backgroundState.length; j++) {
				backgroundState[j] = new Boolean(null);
			}
			builder.setInverseBackgroundForced(true);
			builder.setMultiChoiceItems(activity.getResources().getStringArray(R.array.backgroundArray),
					null, new OnMultiChoiceClickListener() {
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							backgroundState[which] = isChecked;
						}
			
			});
			builder.setNegativeButton("Cancel", this);
			builder.setPositiveButton("Okay", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					int p = 0;
					for (int j = 0; j < backgroundState.length; j++) {
						if(backgroundState[j]) {
							p++;
						}
					}
					if (p > 1) {
						editText.setText("Multiple Selected");
					} else if (p == 0) {
						editText.setText("Select");
					}else if (p == 1) {
						for (int j = 0; j < backgroundState.length; j++) {
							if (backgroundState[j]) {
								editText.setText(activity.getResources().getStringArray(R.array.backgroundArray)[j].toString());
							}
						}
						
					}
					}
			});
			
			builder.setView(dialogLayout);
			return builder.create();

		case (4) :
			ListView incomelist = (ListView) dialogLayout.findViewById(R.id.listView1);
		incomelist.setAdapter(new ArrayAdapter<String>(activity, R.layout.dialoglist, 
				activity.getResources().getStringArray(R.array.incomeArray)));
		incomelist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				editText.setText(activity.getResources().getStringArray(R.array.incomeArray)[arg2]);
				dismiss();
			}	
		});
		builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
			new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);
		builder.setNegativeButton("Cancel", this);
		builder.setView(dialogLayout);

		return builder.create();

		case (5) :
			ListView partylist = (ListView) dialogLayout.findViewById(R.id.listView1);
		partylist.setAdapter(new ArrayAdapter<String>(activity, R.layout.dialoglist, 
				activity.getResources().getStringArray(R.array.partyArray)));
		partylist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				editText.setText(activity.getResources().getStringArray(R.array.partyArray)[arg2]);
				dismiss();
			}	
		});
		builder = (Integer.parseInt(android.os.Build.VERSION.SDK) < 11)? new AlertDialog.Builder(activity) : 
			new AlertDialog.Builder(activity, android.R.style.Theme_Translucent);
		builder.setNegativeButton("Cancel", this);
		builder.setView(dialogLayout);

		return builder.create();
		}
		return null;
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		

	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		Log.v(Integer.toString(which), Boolean.toString(isChecked));
		backgroundState[which] = isChecked;
	}

}
