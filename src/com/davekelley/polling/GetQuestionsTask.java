package com.davekelley.polling;

import java.util.ArrayList;

import library.DatabaseHandler;
import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;

public class GetQuestionsTask extends AsyncTask<Void, Void, Integer> {
	Polling activity;
	ProgressBar progressBar;
	Fragment fragment;
	JSONArray questionList;
	RelativeLayout progressRow;
	String category;
	UserFunctions userFunctions;
	DatabaseHandler dbHandler;
	ArrayList<Boolean> checkList;

	public GetQuestionsTask(Polling activity, RelativeLayout progressRow2, Fragment fragment) {
		this.fragment = fragment;
		this.activity = activity;
		progressRow = progressRow2;
		userFunctions = new UserFunctions();
		dbHandler = activity.getDB();
		category = fragment.getUsableTag();
	}

	protected void onPreExecute() {}

	protected Integer doInBackground(Void... params) {
		questionList = userFunctions.getQuestions(category);
		checkList = new ArrayList<Boolean>();
		for (int j = 0; j < questionList.length(); j++) {
			boolean check;
			try {
				check = userFunctions.hasUserAnswered(questionList.getJSONObject(j), 
						dbHandler.getUserDetails().get("email"));
				checkList.add(j, check);
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		return questionList.length();
	}

	protected void onPostExecute(Integer numQuestions) {

		for (int j = 0; j < questionList.length(); j++) {
			JSONObject question;
			try {
				question = questionList.getJSONObject(j);
				if (userFunctions.isUserLoggedIn(activity)) {
					if (checkList.get(j)) {
						fragment.buildQuestions(question, true);
					}
					else {
						fragment.buildQuestions(question, false);
					}

				}} catch (JSONException e) {
					e.printStackTrace();
				}	
		}
		progressRow.setVisibility(View.GONE);
	}
}
