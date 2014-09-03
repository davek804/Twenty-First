package com.davekelley.polling;

import java.util.ArrayList;
import java.util.List;

import library.DatabaseHandler;
import library.UserFunctions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class DemoPanel extends Fragment {
	String email;
	DatabaseHandler dbHandler;
	EditText ageEditText;
	EditText educationEditText;
	EditText maritalEditText;
	EditText backgroundEditText;
	EditText incomeEditText;
	EditText partyEditText;
	RadioGroup sex;
	RadioButton sexFemale;
	RadioButton sexMale;
	String sexString;
	RadioGroup employment;
	RadioButton employmentYes;
	RadioButton employmentNo;
	RadioButton employmentSelf;
	String empString;
	List<NameValuePair> params;
	Context context;
	DemoPanel act;
	Button reset;
	Button submit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setContentView(R.layout.demopanel);
		act = this;
		ActionBar bar = getActivity().getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		context = getActivity().getApplicationContext();
		dbHandler = new DatabaseHandler(context);
		bar.setTitle(dbHandler.getUserDetails().get("email"));	
		
		ageEditText = (EditText) getActivity().findViewById(R.id.ageEditText);
		ageEditText.setOnClickListener(fieldListener);
		ageEditText.setId(0);
		educationEditText = (EditText) getActivity().findViewById(R.id.educationEditText);
		educationEditText.setOnClickListener(fieldListener);
		educationEditText.setId(1);
		maritalEditText = (EditText) getActivity().findViewById(R.id.maritalEditText);
		maritalEditText.setOnClickListener(fieldListener);
		maritalEditText.setId(2);
		backgroundEditText = (EditText) getActivity().findViewById(R.id.backgroundEditText);
		backgroundEditText.setOnClickListener(fieldListener);
		backgroundEditText.setId(3);
		incomeEditText = (EditText) getActivity().findViewById(R.id.incomeEditText);
		incomeEditText.setOnClickListener(fieldListener);
		incomeEditText.setId(4);
		partyEditText = (EditText) getActivity().findViewById(R.id.partyEditText);
		partyEditText.setOnClickListener(fieldListener);
		partyEditText.setId(5);
		

		sex = (RadioGroup) getActivity().findViewById(R.id.sexRadioGroup);
		sexFemale = (RadioButton) getActivity().findViewById(R.id.femaleRadio);
		sexMale = (RadioButton) getActivity().findViewById(R.id.maleRadio);
		sexFemale.setId(1);
		sexMale.setId(2);
		
		employment = (RadioGroup) getActivity().findViewById(R.id.employmentRadioGroup);
		employmentYes = (RadioButton) getActivity().findViewById(R.id.employedTrueRadio);
		employmentNo = (RadioButton) getActivity().findViewById(R.id.employedSelfRadio);
		employmentSelf = (RadioButton) getActivity().findViewById(R.id.employedSelfRadio);
		employmentYes.setId(1);
		employmentNo.setId(2);
		employmentSelf.setId(3);
		
		reset = (Button) getActivity().findViewById(R.id.resetButton);
		reset.setOnClickListener(fieldListener);
		
		submit = (Button) getActivity().findViewById(R.id.submitButton);
		submit.setOnClickListener(fieldListener);
		
		GetDemographics gd = new GetDemographics(act, dbHandler.getUserDetails().get("email"));
		gd.execute();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.demomenu, menu);
	    return true;
	}

	public void onResume() {
		super.onResume();
	}

	public OnClickListener fieldListener = new OnClickListener() {
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			switch (v.getId()) {
			case (0) :
				
	        ChooseDialog ageDialog = new ChooseDialog("Age", ageEditText, act);
	        ageDialog.show(fm, "fragment_edit_name");
				break;
			case (1) :
	        ChooseDialog educationDialog = new ChooseDialog("Education", educationEditText, act);
	       
	        educationDialog.show(fm, "fragment_edit_name");
				break;
			case (2) :
	        ChooseDialog maritalDialog = new ChooseDialog("Marital", maritalEditText, act);
	       
	        maritalDialog.show(fm, "fragment_edit_name");
				break;
			case (3) :
	        ChooseDialog backgroundDialog = new ChooseDialog("Background", backgroundEditText, act);
	       
	        backgroundDialog.show(fm, "fragment_edit_name");
				break;
			case (4):
				ChooseDialog incomeDialog = new ChooseDialog("Income", incomeEditText, act);
				
				incomeDialog.show(fm, "fragment_edit_name");
					break;
			case (5):
				ChooseDialog partyDialog = new ChooseDialog("Party", partyEditText, act);
				
				partyDialog.show(fm, "fragment_edit_name");
					break;
					
			case (R.id.resetButton) :
				reset();
				break;
			case (R.id.submitButton) :
				submit();
				break;
			}			
		}
	};

	private void reset() {
		ageEditText.setText(R.string.pleaseSelect);
		maritalEditText.setText(R.string.pleaseSelect);
		backgroundEditText.setText(R.string.pleaseSelect);
		educationEditText.setText(R.string.pleaseSelect);
		incomeEditText.setText(R.string.pleaseSelect);
		partyEditText.setText(R.string.pleaseSelect);
		employmentYes.setChecked(false);
		employmentNo.setChecked(false);
		employmentSelf.setChecked(false);
		sexMale.setChecked(false);
		sexFemale.setChecked(false);
		sexString = "";
		empString = "";
		submit();
		GetDemographics gd = new GetDemographics(act, dbHandler.getUserDetails().get("email"));
		gd.execute();
	}
	
	private List<NameValuePair> newParams() {
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("age", "null"));
		params.add(new BasicNameValuePair("marital","null"));
		params.add(new BasicNameValuePair("background", "null"));
		params.add(new BasicNameValuePair("education", "null"));
		params.add(new BasicNameValuePair("income", "null"));
		params.add(new BasicNameValuePair("sex", "null"));
		params.add(new BasicNameValuePair("employment","null"));
		params.add(new BasicNameValuePair("party","null"));
		return params;
		
	}
	
	private void submit() {
		params = new ArrayList<NameValuePair>();
		getButtons();
		params.add(new BasicNameValuePair("age", ageEditText.getText().toString()));
		params.add(new BasicNameValuePair("marital", maritalEditText.getText().toString()));
		params.add(new BasicNameValuePair("background", backgroundEditText.getText().toString()));
		params.add(new BasicNameValuePair("education", educationEditText.getText().toString()));
		params.add(new BasicNameValuePair("income", incomeEditText.getText().toString()));
		params.add(new BasicNameValuePair("sex", sexString));
		params.add(new BasicNameValuePair("employment", empString));
		params.add(new BasicNameValuePair("party", partyEditText.getText().toString()));
		
		params.add(new BasicNameValuePair("user", dbHandler.getUserDetails().get("email").toString()));
		DemoTask demoTask = new DemoTask(act, params);
		demoTask.execute();
	}

	private void getButtons() {
		int n = sex.getCheckedRadioButtonId();
		int e = employment.getCheckedRadioButtonId();
		switch (n) {
		case 1:
			sexString = "female";
			break;
		case 2:
			sexString = "male";
			break;
		case -1:
			sexString = "";
			break;
		}	
		switch (e) {
		case 1:
			empString = "yes";
			break;
		case 2:
			empString = "no";
			break;
		case 3:
			empString = "self-employed";
			break;
		case -1:
			empString = "";
			break;
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 case android.R.id.home:
		 //Intent i = new Intent(this, Polling.class);
			 Intent i;
			 i = new Intent();
			 i.setClass(getActivity(), Polling.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
		 	return true;
		 case R.id.saveDemo:
			 submit();
			 return true;
		 case R.id.deleteDemo:
			 reset();
			 return true;
	        default:
	        	Log.v("v", "failed, but ran");
	            return super.onOptionsItemSelected(item);
		 }
	}
}