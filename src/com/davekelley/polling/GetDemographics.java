package com.davekelley.polling;

import java.util.List;
import library.DatabaseHandler;
import library.UserFunctions;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;

public class GetDemographics extends AsyncTask<String, Void, Integer> {
	ProgressDialog progressDialog;
	List<NameValuePair> param;
	JSONArray questionList;
	DemoPanel activity;
	DatabaseHandler dbHandler;
	String user;
	JSONObject object;

	public GetDemographics(DemoPanel act, String string) {
		activity = act;
		user = string;
	}

	protected void onPreExecute() {}

	protected Integer doInBackground(String... params) {
		UserFunctions userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(activity.getActivity().getApplicationContext())) {
			object = (userFunctions.getDemographics(user));

			return 1;
		} else
			return 0;
	}

	protected void onPostExecute(Integer response) {
		if (response == 1) {

			try {
				int age = object.getInt("age");
				String marital = object.getString("marital");
				String background = object.getString("background");
				String income = object.getString("income");
				String education = object.getString("education");
				String party = object.getString("party");
				String emp = object.getString("employment");
				String sex = object.getString("sex");

				if (marital != "null") 
					activity.maritalEditText.setText(marital);
				
				if (background != "null") 
					activity.backgroundEditText.setText(background);
				
				if (income != "null") 
					activity.incomeEditText.setText(income);
				
				if (education != "null") 
					activity.educationEditText.setText(education);

				if (age != 0) 
					activity.ageEditText.setText(Integer.toString(age));
				
				if (party != "null") 
					activity.partyEditText.setText(party);

				if (sex.matches("male")) {
					activity.sexMale.setChecked(true);					
				} else if (sex.matches("female")) {
					activity.sexFemale.setChecked(true);					
				}

				if (emp.matches("yes")) {
					activity.employmentYes.setChecked(true);					
				} else if (emp.matches("no")) {
					activity.employmentNo.setChecked(true);
				}else if (emp.matches("self-employed")){
					activity.employmentSelf.setChecked(true);
				}else {}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	if (response == 0) {
		Log.v("GD", "Response 0");
	}
	}

}
