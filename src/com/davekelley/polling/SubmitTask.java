package com.davekelley.polling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import library.DatabaseHandler;
import library.JSONParser;
import library.UserFunctions;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitTask extends AsyncTask<String, Void, Integer> {

	private Polling activity;
	private int id = -1;
	private JSONParser jsonParser;
	private static String loginURL = "http://davidjkelley.net/android_api/";
	private static String registerURL = "http://davidjkelley.net/android_api/";
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private int responseCode = 0;
	private JSONObject question;
	private int response;
	private String responseText;
	private String title;
	private String category;
	private String email;
	private TagObj tagObj;
	private HashMap<String, String> user;
	

	public SubmitTask(Polling activity, JSONObject question, int response,
			HashMap<String, String> user, Object object)	{
		this.activity = activity;
		this.question = question;
		this.response = response;
		this.user = user;
		this.tagObj = (TagObj)object;
	}

	protected void onPreExecute() {
		try {
			email = user.get(KEY_EMAIL);
			category = question.getString("category");
			id = question.getInt("id");
			title = question.getString("title");	
			responseText = question.getString("answer" + Integer.toString(response));
		} catch (JSONException e) {e.printStackTrace();}
	}

	protected Integer doInBackground(String... arg0) {
		UserFunctions userFunctions = new UserFunctions();
		userFunctions.submitQuestion(email, category, id, title, response, responseText);
		return id;

	}

	protected void onPostExecute(Integer responseCode) {
		TableRow tr = tagObj.getTR();
		RadioGroup rG = (RadioGroup) tr.findViewById(R.id.responseRadioGroup);
		rG.setVisibility(View.GONE);
		TextView title = (TextView) tr.findViewById(R.id.questionTextView);
		title.setText("Answered: " + title.getText().toString());
		Button submitButton = (Button) tr.findViewById(R.id.submitButton);
		submitButton.setVisibility(View.GONE);
		//tr.setBackgroundColor(activity.getResources().getColor(R.color.androidBlue));
	}
}