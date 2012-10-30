package com.davekelley.polling;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TableRow;

public class TagObj {
	JSONObject question;
	RadioGroup radioGroup;
	TableRow tableRow;
	private Class<?> clss;
	private Bundle args;
	
	public TagObj(Class<?> _class, Bundle _args) {
		clss = _class;
		args = _args;
	}
	
	public TagObj(JSONObject question, RadioGroup radioGroup, TableRow tr){
		this.question = question;
		this.radioGroup = radioGroup;
		this.tableRow = tr;
	}
	
	public JSONObject getQuestion() {
		return question;
	}
	
	public RadioGroup getRadioGroup() {
		return radioGroup;
	}
	
	public TableRow getTR() {
		return tableRow;
	}

}
