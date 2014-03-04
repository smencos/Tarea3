package com.example.demo1s3.data;

public class Helper {
	public final static String INSTAGRAM_API_KEY = "c66d0dd9d421450499374e94ec67f0bb";
	public final static String BASE_API_URL = "https://api.instagram.com/v1";
	
	public static String getRecentMediaUrl (String tag){
			return BASE_API_URL + "/tags/" + tag + "/media/recent?client_id=" + INSTAGRAM_API_KEY;
			
	}
	
}
