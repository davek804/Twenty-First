package com.davekelley.polling;

import java.util.ArrayList;

import library.DatabaseHandler;
import library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class EconFragment extends Fragment {
	View layout;
	TableLayout questionContainer;
	int leftMargin=0;
	int topMargin=4;
	int rightMargin=0;
	int bottomMargin=0;
	RadioGroup radioGroup;
	Polling activity;
	DatabaseHandler dbHandler;
	UserFunctions userFunctions;
	public static final int KEY = 1;
	ArrayList<TableRow> tableList;
	LayoutInflater inflater;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(activity)) {

			this.inflater = inflater;
			tableList = new ArrayList<TableRow>();

			dbHandler = activity.getDB();
			View v = inflater.inflate(R.layout.categoryfragment, container, false);
			layout = v;
			RelativeLayout progressRow = (RelativeLayout) layout.findViewById(R.id.progressRow);
			GetQuestionsTask questionsTask = new GetQuestionsTask(activity, progressRow, this);
			questionsTask.execute();	
			return v;
		} else {
			this.inflater = inflater;
			View v = inflater.inflate(R.layout.notloggedin, container, false);
			TextView error = (TextView) v.findViewById(R.id.noUserTextView);
			layout = v;
			error.setText("Please log in to view questions!");
			return v;
		}
	}

	public void buildQuestions(JSONObject question, boolean isAnswered) throws JSONException {
		if (isAnswered){
			questionContainer = (TableLayout) layout.findViewById(R.id.questionContainer);
			View questionBox = inflater.inflate(R.layout.question, null);
			questionBox.setId(Integer.parseInt(question.getString("id")));
			TextView title = (TextView) questionBox.findViewById(R.id.questionTextView);
			title.setText("Answered: " + question.getString("title"));

			Button chartsButton = (Button) questionBox.findViewById(R.id.chartsButton);
			chartsButton.setTag(question);
			Button submitButton = (Button) questionBox.findViewById(R.id.submitButton);

			chartsButton.setOnClickListener(chartsListener);
			//submitButton.setOnClickListener(submitListener);
			submitButton.setVisibility(View.GONE);
			//TextView submitTextView = (TextView) questionBox.findViewById(R.id.questionTextView);
			//submitTextView.setText("Submitted");
			TableRow tr = (TableRow) questionBox;
			TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT);
			trParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			//tr.setLayoutParams(trParams);
			tableList.add(tr);
			questionContainer.addView(tr);
			TagObj tagObj = new TagObj(question, radioGroup, tr);
			submitButton.setTag(tagObj);
		}else {
			questionContainer = (TableLayout) layout.findViewById(R.id.questionContainer);
			View questionBox = inflater.inflate(R.layout.question, null);
			questionBox.setId(Integer.parseInt(question.getString("id")));
			TextView title = (TextView) questionBox.findViewById(R.id.questionTextView);
			title.setText(question.getString("title"));

			radioGroup = (RadioGroup) questionBox.findViewById(R.id.responseRadioGroup);
			String typeFromTable = question.getString("default");

			int responseType = Integer.parseInt(typeFromTable);
			if (responseType == 1) {
				//populate default radio buttons
				Resources res = getResources();
				String[] defaultAnswers = res.getStringArray(R.array.defaultAnswers);
				int j = 0;
				while (j < 5) {
					RadioButton rb = new RadioButton(activity);
					rb.setText(defaultAnswers[j]);
					rb.setId(j);
					radioGroup.addView(rb);
					j++;
				}
			}
			else if (responseType == 0) {
				for (int j = 0; j < 5; j++) {
					if (question.getString("answer" + Integer.toString(j)) != "null") {
						RadioButton rb;
						rb = new RadioButton(activity);
						rb.setId(j);
						rb.setText(question.getString("answer" + Integer.toString(j)));
						if (question.getString("answer" + Integer.toString(j)).length() != 0) {					
							radioGroup.addView(rb);
						}
					}
					else {
						if (question.getString("answer" + Integer.toString(j)) == "null") {
							RadioButton rb;
							rb = new RadioButton(activity);
							rb.setText("null");
							rb.setId(j);//may not work
							radioGroup.addView(rb);
						}
					}
				}
			}

			Button chartsButton = (Button) questionBox.findViewById(R.id.chartsButton);
			chartsButton.setTag(question);
			Button submitButton = (Button) questionBox.findViewById(R.id.submitButton);

			chartsButton.setOnClickListener(chartsListener);
			submitButton.setOnClickListener(submitListener);

			TableRow tr = (TableRow) questionBox;
			TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT);
			trParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			//tr.setLayoutParams(trParams);
			tableList.add(tr);
			questionContainer.addView(tr);
			TagObj tagObj = new TagObj(question, radioGroup, tr);
			submitButton.setTag(tagObj);
		}
	}

	public OnClickListener chartsListener = new OnClickListener() {
		public void onClick(View v) {
			Intent chart = new Intent();
//			chart.setClass(activity, Chart.class);
//			chart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			JSONObject question = (JSONObject) v.getTag();
//			try {
//				String category = question.getString("category");
//				String id = question.getString("id");
//				String title = question.getString("title");
//				chart.putExtra("CATEGORY", category);
//				chart.putExtra("ID", id);
//				chart.putExtra("TITLE", title);
//
//			} catch (JSONException e) {e.printStackTrace();}
//			startActivity(chart);
		}	
	};

	public OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			if (userFunctions.isUserLoggedIn(activity)) {
				TagObj tagObject = (TagObj) v.getTag();
				RadioGroup radioGroup = tagObject.getRadioGroup();
				JSONObject question = tagObject.getQuestion();
				int btn = radioGroup.getCheckedRadioButtonId();
				SubmitTask submitTask;
				try {
					switch (btn) {
					case (0):				
						Log.v("submit", question.getString("answer0"));
					submitTask = new SubmitTask((Polling) activity, question, 0, dbHandler.getUserDetails(), v.getTag());
					submitTask.execute();
					v.setClickable(false);
					break;
					case (1):
						Log.v("submit", question.getString("answer1"));
					submitTask = new SubmitTask((Polling) activity, question, 1, dbHandler.getUserDetails(), v.getTag());
					submitTask.execute();
					v.setClickable(false);
					((Button) v).setText("Submitted");
					break;
					case (2):
						Log.v("submit", question.getString("answer2"));
					submitTask = new SubmitTask((Polling) activity, question, 2,  dbHandler.getUserDetails(), v.getTag());
					submitTask.execute();
					v.setClickable(false);
					((Button) v).setText("Submitted");
					break;
					case(3):
						Log.v("submit", question.getString("answer3"));
					submitTask = new SubmitTask((Polling) activity, question, 3,  dbHandler.getUserDetails(), v.getTag());
					submitTask.execute();
					v.setClickable(false);
					((Button) v).setText("Submitted");
					break;
					case (4):
						Log.v("submit", question.getString("answer4"));
					submitTask = new SubmitTask((Polling) activity, question, 4,  dbHandler.getUserDetails(), v.getTag());
					submitTask.execute();
					v.setClickable(false);
					((Button) v).setText("Submitted");
					break;
					case (-1):
						Toast toast = Toast.makeText(activity.getApplicationContext(), "No Response Selected!", Toast.LENGTH_SHORT);
					toast.show();
					break;

					}
				}catch (JSONException e) {e.printStackTrace();}
			}
			else {
				Toast toast = Toast.makeText(activity.getApplicationContext(), "Please login!", Toast.LENGTH_SHORT);
				toast.show();
			}	
		}	
	};

	public void onResume() {
		super.onResume();

	}
	
	public void onStart() {
		super.onStart();
	}
	
	public void onStop() {
		super.onStop();
	}
	
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void onPause() {
		super.onPause();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (Polling) activity;
	}

	public String getUsableTag() {
		return "economics";
	}
}
