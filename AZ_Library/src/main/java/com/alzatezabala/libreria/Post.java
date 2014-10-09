package com.alzatezabala.libreria;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Post {

	private String url;
	private HashMap<String, String> params;
    private NotifyPost notifyPost;

	public Post(String url, HashMap<String, String> par) {
		this.url = url;
		this.params = par;
	}

    public NotifyPost getNotifyPost() {
        return notifyPost;
    }

    public void setNotifyPost(NotifyPost notifyPost) {
        this.notifyPost = notifyPost;
    }

    public Post(String url) {
		this.url = url;
	}

	public void setParams(HashMap<String, String> par) {
		this.params = par;
	}

	public void post() {
		new AsyncTask<Post, Void, String>() {
			@Override
			protected String doInBackground(Post... params) {
				return params[0].request();
			}

			protected void onPostExecute(String msg) {
				if(notifyPost!=null){
                    notifyPost.postFinished(msg);
                }
                Log.d("post", msg);
			}

		}.execute(this);
	}

	public String request() {
		String responseStr = "";
		String postReceiverUrl = url;
		// HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		// post header
		HttpPost httpPost = new HttpPost(postReceiverUrl);

		// add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		if (params.isEmpty()) {
			Log.d("Post", "Empty params");
		} else {
			for (Entry<String, String> e : params.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(e.getKey(), e
						.getValue()));
			}


			try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
				httpPost.setEntity(urlEncodedFormEntity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.v("PostLib", e.toString());
			}

			// execute HTTP post request
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.v("PostLib", e.toString());
			}
			HttpEntity resEntity = null;

			try {
				resEntity = response.getEntity();
			} catch (NullPointerException e) {
				Log.v("PostLib", e.toString());
			}

			if (resEntity != null) {

				try {
					responseStr = EntityUtils.toString(resEntity).trim();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.v("PostLib", e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.v("PostLib", e.toString());
				}
			}
		}
		return responseStr;

	}
}
