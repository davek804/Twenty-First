package com.davekelley.polling;


import library.DatabaseHandler;
import library.JSONParser;
import library.UserFunctions;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class LoginTask extends AsyncTask<String, Void, Integer> {

	private ProgressDialog progressDialog;
	private Polling activity;
	private DatabaseHandler dbHandler;
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private int responseCode = 0;
	private ScrollView loginLayout;
	private LinearLayout loggedInLayout;

	public LoginTask(Polling activity, ProgressDialog progressDialog, ScrollView loginLayout, LinearLayout loggedInLayout) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.loginLayout = loginLayout;
		this.loggedInLayout = loggedInLayout;
	}

	protected void onPreExecute() {
		progressDialog.show();
	}

	protected Integer doInBackground(String... arg0) {
		EditText userName = (EditText)activity.findViewById(R.id.emailEditText);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.passEditText);
		UserFunctions userFunction = new UserFunctions();
		if (userName.getText().toString().length() < 5 || passwordEdit.getText().toString().length() < 5) {
        	return 2;
	}
		JSONObject json = userFunction.loginUser(userName.getText().toString(), passwordEdit.getText().toString());
		Log.v("", json.toString());
		// check for login response
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);

				if(Integer.parseInt(res) == 1){
					//user successfully logged in
					// Store user details in SQLite Database
					dbHandler = new DatabaseHandler(activity.getApplicationContext());
					JSONObject json_user = json.getJSONObject("user");
					
					// Clear all previous data in database
					userFunction.logoutUser(activity.getApplicationContext());
					dbHandler.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), 
							json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                        

					responseCode = 1;
					// Close Login Screen
					

				}else{
					responseCode = 0;
					// Error in login
				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return responseCode;
	}

	protected void onPostExecute(Integer responseCode) {
		EditText userName = (EditText)activity.findViewById(R.id.emailEditText);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.passEditText);
		CheckBox rememberBox = (CheckBox)activity.findViewById(R.id.rememberCheckBox);

		dbHandler = new DatabaseHandler(activity.getApplicationContext());		
		
		if (responseCode == 1) {
			progressDialog.dismiss();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(activity.getApplicationContext(), "Logged in.", duration);
			toast.show();
			activity.bar.getTabAt(0).setText(dbHandler.getUserDetails().get("email"));
			
			loginLayout.setVisibility(ScrollView.INVISIBLE);
			loggedInLayout.setVisibility(LinearLayout.VISIBLE);
			TextView loggedIn = (TextView) loggedInLayout.findViewById(R.id.userTextView);
			loggedIn.setText(dbHandler.getUserDetails().get("email"));
			Button logOutButton = (Button) loggedInLayout.findViewById(R.id.logoutButton);
			logOutButton.setOnClickListener(logoutListener);
			Button demoButton = (Button) loggedInLayout.findViewById(R.id.demoButton);
			demoButton.setOnClickListener(demoListener);
			
			if (rememberBox.isChecked()) {
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
				SharedPreferences.Editor preferencesEditor = sp.edit();
				
				String email = userName.getText().toString();
				String password = passwordEdit.getText().toString();
				
				preferencesEditor.putString("email", email);
				preferencesEditor.putString("password", password);
				preferencesEditor.apply();
			}
			else {userName.setText("");
			passwordEdit.setText("");
			}
		}
		if (responseCode == 0 || responseCode == 2) {
			progressDialog.dismiss();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(activity.getApplicationContext(), "Incorrect username/password", duration);
			dbHandler.resetTables();
			toast.show();
		}
	}
	public OnClickListener logoutListener = new OnClickListener() {
		public void onClick(View v) {
			UserFunctions userFunctions = new UserFunctions();
			userFunctions.logoutUser(activity.getApplicationContext());
			activity.bar.getTabAt(0).setText(R.string.login);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(activity.getApplicationContext(), "Logged out", duration);
			toast.show();
			loginLayout.setVisibility(ScrollView.VISIBLE);
			loggedInLayout.setVisibility(LinearLayout.GONE);
		}
	};
	public OnClickListener demoListener = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent();
			i.setClass(activity.getApplicationContext(), DemoPanel.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(i);
		}
	};
}