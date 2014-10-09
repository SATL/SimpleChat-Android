package com.alzatezabala.bitmap;

import java.io.InputStream;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class loadImage extends AsyncTask<String, Void, Void>{
	private String id;
	private ImageView bmImage;
	private Context ctx;
	private int imgloading;
	
	public loadImage(ImageView img, Context ctx, int imgLoading){
		this.bmImage=img;
		this.ctx=ctx;
		this.imgloading=imgLoading;
	}
	
	 protected Void doInBackground(String... urls) {
		 ImageLoader imgl= new ImageLoader(ctx, imgloading);
		 Log.d("image", urls[0]);
		 imgl.DisplayImage(urls[0], bmImage);
		 
		return null;
	      /*String url = urls[0];
	      Bitmap mIcon = null;
	      try {
	        InputStream in = new java.net.URL(url).openStream();
	        mIcon = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          Log.e("Error", e.getMessage());
	      }
	      return mIcon;*/
		 
	  }
	
}
