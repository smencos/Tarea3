package com.example.demo2s2.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

public class GetData {
	public static RequestQueue requestqueue1;

	public GetData (){
String url = Helper.getRecentMediaUrl("lego");
		
		
		

		
		
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject arg0) {
				
				
				
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
							Log.e("username", userName);
					}
					}
					
				}catch (JSONException e){
					Log.e("ERROR", Log.getStackTraceString(e));
				}
				
			}
			
		};
		
		
		JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, 
								url, null, listener, null);
		
		requestqueue1.add(request);
	}
	
}
