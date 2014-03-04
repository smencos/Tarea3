package com.example.demo2s2.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.demo2s2.activities.CameraActivity;
import com.example.demo2s2.fragments.PhotoDialogFragment;
import com.example.demo2s2.data.Helper;
import com.example.demo2s2.data.Image;
import com.example.demo2s2.data.ImageAdapter;
import com.example.demo2s2.R;
import com.example.demo2s2.activities.CountryDetailActivity;
import com.example.demo2s2.fragments.PhotoDialogFragment.NoticeDialogListener;

public class ComunidadFragment extends Fragment implements OnClickListener, NoticeDialogListener {
	
	public static RequestQueue requestqueue;

	Button btnPhoto;
	Button btnUpdate;
	Button btnParse;
	ImageAdapter adapter;
	ArrayList<Image> imagesArray;
	String photoPath;
	private static final int LOAD_IMAGE = 1;
	private static final int CAMERA = 2;
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		requestqueue = Volley.newRequestQueue(getActivity().getApplicationContext());
		
		GridView gridView = (GridView)getActivity().findViewById(R.id.grid);
        imagesArray = new ArrayList<Image>();
        adapter = new ImageAdapter(getActivity().getApplicationContext(), imagesArray);
        gridView.setAdapter(adapter);
        btnUpdate = (Button)getActivity().findViewById(R.id.buttonAPI);
       
        btnUpdate.setOnClickListener(this);
        
        APICall();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View view = inflater.inflate(R.layout.fragment_api_images, container,false);
		return view;
	}

	
	
	public Bitmap resizeBitmap(int targetW, int targetH){
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		int scaleFactor = 1;
		if ((targetW > 0 ) || (targetH>0)){
			scaleFactor = Math.min(photoW/targetW , photoH/targetH);
		}
		
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
		return BitmapFactory.decodeFile(photoPath,bmOptions);
		
	
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case LOAD_IMAGE:
				if (resultCode == getActivity().RESULT_OK) {
					fromGallery(data);
				}
				break;
			case CAMERA:
				if (resultCode == getActivity().RESULT_OK) {
					fromCamera(data);
				}
				break;
		}

	}
	
	public void fromCamera (Intent data){
		getActivity().findViewById(R.id.img).setVisibility(View.VISIBLE);
		ImageView img = (ImageView) getActivity().findViewById(R.id.img);
		
		Bitmap bitmap = resizeBitmap(img.getWidth(), img.getHeight());
		img.setImageBitmap(bitmap);
		
		Intent mediaScan = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File file = new File(photoPath);
		Uri contentUri = Uri.fromFile(file);
		mediaScan.setData(contentUri);
		getActivity().sendBroadcast(mediaScan);
	}
	
	public void fromGallery (Intent data){
		getActivity().findViewById(R.id.img).setVisibility(View.VISIBLE);

		if (data != null){
			Uri selectedImage = data.getData();	
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			if(cursor.moveToFirst()){
				int columIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columIndex);
				cursor.close();
				
				ImageView img = (ImageView) getActivity().findViewById(R.id.img);
				img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			}
			
		}
	}

	
	private File setupFile() {
		File albumDir;
		String albumName ="ejemplo";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			albumDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
		} else {
			albumDir = new File(
					Environment.getExternalStorageDirectory() + "/dcim" +albumName);
		}
		albumDir.mkdirs();
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
		String imageFileNam = "IMG_" + timeStamp + ".jpg";
		File image = new File(albumDir + "/" + imageFileNam);
		
		return image;
	}

	@Override
	public void onDialogNegativeClick() {
		Intent intent = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
				);	
		int code = LOAD_IMAGE;
		
		startActivity(intent);
		
	}
	
	@Override
	public void onDialogPositiveClick() {
		/*Intent intent = null;
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = setupFile();
		photoPath = photo.getAbsolutePath();
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		int code = CAMERA;
			
		
		startActivityForResult(intent, code);
		
		*/
		
		
		
	
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent(getActivity(), CameraActivity.class);
		startActivity(intent);
	}
	
	private void APICall() {
		// TODO Auto-generated method stub
		String url = Helper.getRecentMediaUrl("lego");

		
		btnUpdate.setEnabled(false);
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject arg0) {
				
				getActivity().findViewById(R.id.grid).setVisibility(View.VISIBLE);
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
				}catch (JSONException e){
					Log.e("ERROR", Log.getStackTraceString(e));
				}
				
			}
			
		};
		
		
		JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, 
								url, null, listener, null);
		
		requestqueue.add(request);
	}


}
