package com.davekelley.polling;

import java.util.List;
import library.DatabaseHandler;
import library.UserFunctions;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DemoTask extends AsyncTask<String, Void, Integer> {
	ProgressDialog progressDialog;
	List<NameValuePair> param;
	JSONArray questionList;
	DemoPanel activity;
	DatabaseHandler dbHandler;
	String user;
	
	public DemoTask(DemoPanel act, List<NameValuePair> param) {
		this.param = param;
		this.activity = act;
	}
	
	protected void onPreExecute() {}

	protected Integer doInBackground(String... params) {
		UserFunctions userFunctions = new UserFunctions();
		userFunctions.addDemographics(param);
		Log.v("DT", param.toString());
		return 1;
	}
	
	protected void onPostExecute(Integer numQuestions) {
		Toast toast = Toast.makeText(activity.getActivity().getApplicationContext(), "Updated!", Toast.LENGTH_SHORT);
		toast.show();
	}
}
