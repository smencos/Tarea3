package com.example.demo2s2.activities;

import java.io.File;
import java.text.SimpleDateFormat;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demo2s2.R;
import com.example.demo2s2.data.Helper;

public class CameraActivity extends Activity implements OnClickListener {

	private static final int LOAD_IMAGE = 1;
	private static final int CAMERA = 2;
	Button btnFromGallery;
	Button btnFromCamera;
	String photoPath;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		btnFromGallery = (Button) findViewById(R.id.btnFromGallery);
		btnFromCamera = (Button) findViewById(R.id.btnFromCamera);
		btnFromGallery.setOnClickListener(this);
		btnFromCamera.setOnClickListener(this);
		
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case LOAD_IMAGE:
				if (resultCode == RESULT_OK) {
					fromGallery(data);
				}
				break;
			case CAMERA:
				if (resultCode == RESULT_OK) {
					fromCamera(data);
				}
				break;
		}

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
	
	
	
	public void fromCamera (Intent data){
		ImageView img = (ImageView) findViewById(R.id.img);
		
		Bitmap bitmap = resizeBitmap(img.getWidth(), img.getHeight());
		img.setImageBitmap(bitmap);
		
		Intent mediaScan = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File file = new File(photoPath);
		Uri contentUri = Uri.fromFile(file);
		mediaScan.setData(contentUri);
		this.sendBroadcast(mediaScan);
	}

	public void fromGallery (Intent data){
		if (data != null){
			Uri selectedImage = data.getData();	
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			if(cursor.moveToFirst()){
				int columIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columIndex);
				cursor.close();
				
				ImageView img = (ImageView) findViewById(R.id.img);
				img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			}
			
		}
	}


	@Override
	public void onClick(View v) {
		Intent intent = null;
		int code = 0;
		if (v.getId() == btnFromGallery.getId()) {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					);	
			code = LOAD_IMAGE;
			
		} else if (v.getId() == btnFromCamera.getId()){
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File photo = setupFile();
			photoPath = photo.getAbsolutePath();
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			code = CAMERA;
		}
		startActivityForResult(intent, code);
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

	
}
