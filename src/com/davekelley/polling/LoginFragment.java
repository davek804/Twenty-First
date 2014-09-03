package com.davekelley.polling;

import library.DatabaseHandler;
import library.UserFunctions;

import com.actionbarsherlock.R;

import com.actionbarsherlock.app.SherlockFragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class LoginFragment extends SherlockFragment {
	Button loginButton;
	Button registerButton;
	Button logoutButton;
	Button tabsButton;
	Button demoButton;
	EditText emailEditText;
	EditText passwordEditText;
	CheckBox rememberBox;
	Polling activity;
	UserFunctions userFunctions;
	DatabaseHandler dbHandler;
	SharedPreferences sp;
	View wholeLayout;
	ScrollView loginLayout;
	LinearLayout loggedInLayout;

	/*As this class is actually a fragment, onCreate() does not run first. The method belows
	 * launches the class. In order to be able to add questions from the server, this method
	 * inflaters an XML layout, stores it as a field for the entire class to access, and returns
	 * that View (XML Layout) to the TabAdapter in Polling.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Log.v("LF", "onCV");
		userFunctions = new UserFunctions();
		dbHandler = activity.getDB();
		View v = inflater.inflate(R.layout.bothlogins, container, false);
		wholeLayout = v;
		loginLayout = (ScrollView) v.findViewById(R.id.loginScrollView);
		loggedInLayout = (LinearLayout) v.findViewById(R.id.loggedInLinearLayout);
		if (userFunctions.isUserLoggedIn(activity.getApplicationContext())) {
			loginLayout.setVisibility(ScrollView.GONE);
		}else {
			loggedInLayout.setVisibility(LinearLayout.GONE);
		}
		return v;		
	}

	/*This method instantiates the elements that need to be accessed programmatically. It
	 * stores the variables as class wide fields so that other methods can access their data*/
	public void onResume() {
		super.onResume();
		//Log.v("LF", "onResume");
		loginLayout.setVisibility(ScrollView.VISIBLE);
		loggedInLayout.setVisibility(LinearLayout.VISIBLE);
		if (userFunctions.isUserLoggedIn(activity.getApplicationContext())) {
			loginLayout.setVisibility(ScrollView.GONE);
			TextView loggedInTextView = (TextView) loggedInLayout.findViewById(R.id.userTextView);
			loggedInTextView.setText(dbHandler.getUserDetails().get("email"));
			logoutButton = (Button) wholeLayout.findViewById(R.id.logoutButton);
			logoutButton.setOnClickListener(logoutListener);
			demoButton = (Button) wholeLayout.findViewById(R.id.demoButton);
			demoButton.setOnClickListener(demoListener);
			tabsButton = (Button) wholeLayout.findViewById(R.id.tabsButton);
			tabsButton.setOnClickListener(tabsListener);
			activity.bar.getTabAt(0).setText(dbHandler.getUserDetails().get("email"));
		}else {
			loggedInLayout.setVisibility(LinearLayout.GONE);
		}	
		loginButton = (Button) getActivity().findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
		registerButton = (Button) getActivity().findViewById(R.id.registerButton);
		registerButton.setOnClickListener(registerListener);
		rememberBox = (CheckBox) activity.findViewById(R.id.rememberCheckBox);
		sp = PreferenceManager.getDefaultSharedPreferences(activity);
		if (sp.getBoolean("rememberBox", false)) {
			rememberBox.setChecked(true);

			EditText emailEditText = (EditText) activity.findViewById(R.id.emailEditText);
			EditText passwordEditText = (EditText) activity.findViewById(R.id.passEditText);
			emailEditText.setText(sp.getString("email", ""));
			passwordEditText.setText(sp.getString("password", ""));
		}
		rememberBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				boolean remember = rememberBox.isChecked();
				sp = PreferenceManager.getDefaultSharedPreferences(activity);
				SharedPreferences.Editor preferencesEditor = sp.edit();
				preferencesEditor.putBoolean("rememberBox", remember);
				emailEditText = (EditText) activity.findViewById(R.id.emailEditText);
				passwordEditText = (EditText) activity.findViewById(R.id.passEditText);
				emailEditText.setText(sp.getString("email", ""));
				passwordEditText.setText(sp.getString("password", ""));
				preferencesEditor.apply();
			}
		});
	}
	
	public OnClickListener tabsListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.tabsButton):
				LiveTabs cD = new LiveTabs("Modify Tabs", tabsButton, activity);
				cD.show(activity.getSupportFragmentManager(), "");
				break;
				default:
					break;
			}
		}
	};

	public OnClickListener logoutListener = new OnClickListener() {
		public void onClick(View v) {
			userFunctions.logoutUser(activity.getApplicationContext());
			activity.bar.getTabAt(0).setText(R.string.login);
			Toast toast = Toast.makeText(activity.getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);
			toast.show();
			loginLayout.setVisibility(ScrollView.VISIBLE);
			loggedInLayout.setVisibility(LinearLayout.GONE);
			activity.userLoggedOut();
		}
	};
	public OnClickListener demoListener = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent();
			i.setClass(activity.getApplicationContext(), DemoPanel.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
	};

	/*This method adds a new listener to an individual button and runs operations. 
	 * For this specific listener, it launches an AsyncTask designed to login the user
	 * on a thread that does not slow down the UI. Without this method, the UI would freeze
	 * when the user tried to login.*/
	public OnClickListener loginListener = new OnClickListener() {
		public void onClick(View v) {
			ProgressDialog progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Logging in...");
			LoginTask loginTask = new LoginTask(activity, progressDialog, loginLayout, loggedInLayout);
			loginTask.execute();
		}
	};

	/*This method adds a new listener to an individual button and runs operations. 
	 * For this specific listener, it launches an AsyncTask designed to register the user
	 * on a thread that does not slow down the UI. Without this method, the UI would freeze
	 * when the user tried to register.*/
	public OnClickListener registerListener = new OnClickListener() {
		public void onClick(View v) {
			ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Registering new user...");
			RegisterTask registerTask = new RegisterTask((Polling) getActivity(), progressDialog);
			registerTask.execute();
		}

	};

	public void onPause() {
		super.onPause();
		if(rememberBox.isChecked()) {
			sp = PreferenceManager.getDefaultSharedPreferences(activity);
			SharedPreferences.Editor preferencesEditor = sp.edit();
			boolean checked = true;
			preferencesEditor.putBoolean("rememberBox", checked);
			preferencesEditor.apply();
		}
	}

	/*This method attaches the fragment to the main Activity's actionBar once onCreateView
	 * completes.*/
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (Polling) activity;
	}
	public void onCreate(Bundle savedInstanceState) {
		//Log.v("LF", "onCreate");
		super.onCreate(savedInstanceState);
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public void onStart() {
		//Log.v("LF", "onStart");
		super.onStart();
	}
	public void onStop() {
		super.onStop();
	}
	public void onDestroyView() {
		super.onDestroyView();
	}
	public void onDestroy() {
		super.onDestroy();
	}
	public void onDetach() {
		super.onDetach();
	}

}