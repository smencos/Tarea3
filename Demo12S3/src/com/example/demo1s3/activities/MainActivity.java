package com.example.demo1s3.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.demo1s3.R;
import com.example.demo1s3.data.Helper;
import com.example.demo1s3.data.Image;
import com.example.demo1s3.data.ImageAdapter;
import com.example.demo1s3.fragments.PhotoDialogFragment;
import com.example.demo1s3.fragments.PhotoDialogFragment.NoticeDialogListener;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends FragmentActivity implements OnClickListener, NoticeDialogListener{
	Button btnPhoto;
	Button btnUpdate;
	Button btnParse;
	ImageAdapter adapter;
	ArrayList<Image> imagesArray;
	public static RequestQueue requestqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "Z9Vlqk5EiHCduSXfqqmRL3wGfilxSXZZf4kWkLs5", "ZUBn2uJ3BfloezNxqbYQQS47PXcs4CZ5GQ4nEZYj");
        
        requestqueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        
        GridView gridView = (GridView)findViewById(R.id.grid);
        imagesArray = new ArrayList<Image>();
        adapter = new ImageAdapter(this, imagesArray);
        gridView.setAdapter(adapter);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnParse = (Button) findViewById(R.id.btnParse);
        btnPhoto.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnParse.setOnClickListener(this);
        
        
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == btnPhoto.getId()){
			new PhotoDialogFragment().show(getSupportFragmentManager(), "");
		} else if (v.getId() == btnUpdate.getId()){
			APICall();
		} else if (v.getId() == btnParse.getId()) {
			parse();
		}
	}
	
	private void parse(){
		ParseObject test = new ParseObject("Prueba");
		test.put("nombre", "Stuardo");
		test.saveInBackground();
		Log.e("TAG" , "Guardando...");
		//JImZiru658
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Prueba");
		query.getInBackground("JImZiru658", new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject obj, ParseException arg1) {
				// TODO Auto-generated method stub
				if (obj != null) {
					Toast.makeText(getApplicationContext(), obj.getString("nombre"), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void showNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(getString(R.string.txt_notif_title))
			.setContentText(getString(R.string.txt_notif_subtitle));
		Intent result = new Intent(this, CameraActivity.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(CameraActivity.class);
		stackBuilder.addNextIntent(result);
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		
		
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, builder.build());
		
	}
	

	private void APICall() {
		// TODO Auto-generated method stub
		String url = Helper.getRecentMediaUrl("lego");

		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		btnUpdate.setEnabled(false);
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject arg0) {
				findViewById(R.id.progressBar).setVisibility(View.GONE);
				findViewById(R.id.grid).setVisibility(View.VISIBLE);
				btnUpdate.setEnabled(true);
				JSONArray data;
				try{
					data = arg0.getJSONArray("data");
					for(int i = 0; i< data.length(); i++){
						JSONObject element = data.getJSONObject(i);
						String type = element.getString("type");
						if(type.equals("image")){
							JSONObject user = element.getJSONObject("user");
							JSONObject images = element.getJSONObject("images");
							JSONObject standardResolution = images.getJSONObject("standard_resolution");
							String userName = user.getString("username");
							String imageUrl = standardResolution.getString("url");
							Image image = new Image();
							image.setImgUrl(imageUrl);
							image.setUserName(userName);
							imagesArray.add(image);
							
						}
					}
					adapter.notifyDataSetChanged();
					showNotification();
				}catch (JSONException e){
					Log.e("ERROR", Log.getStackTraceString(e));
				}
				
			}
			
		};
		
		
		JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, 
								url, null, listener, null);
		
		requestqueue.add(request);
	}

	@Override
	public void onDialogPositiveClick() {
		Toast.makeText(this, "Hizo click en si", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void onDialogNegativeClick() {
		Toast.makeText(this, "Hizo click en no :(", Toast.LENGTH_SHORT).show();
		
	}

	
	
	



}
