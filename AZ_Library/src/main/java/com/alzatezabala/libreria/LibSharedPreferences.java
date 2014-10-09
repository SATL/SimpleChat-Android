package com.alzatezabala.libreria;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class LibSharedPreferences {
	private Context _ctx;
	private String _name;
	private android.content.SharedPreferences sharedpreferences;

	public LibSharedPreferences(Context context, String name) {
		this._ctx = context;
		this._name = name;
	}

	public void saveMultipleData(HashMap<String, String> values) {
		sharedpreferences = _ctx.getSharedPreferences(_name,
				Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		for (Entry<String, String> e : values.entrySet()) {
			editor.putString(e.getKey(), e.getValue());
		}
		editor.commit();
	}

	public void setValue(String key, String val) {
		sharedpreferences = _ctx.getSharedPreferences(_name,
				Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString(key, val);
		editor.commit();
	}

	public String getValue(String key) {
		sharedpreferences = _ctx.getSharedPreferences(_name,
				Context.MODE_PRIVATE);
		if (sharedpreferences.contains(key)) {
			return sharedpreferences.getString(key, "");

		} else
			return "";
	}

	public void empty() {
		sharedpreferences = _ctx.getSharedPreferences(_name,
				Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.clear();
		editor.commit();
	}
}
