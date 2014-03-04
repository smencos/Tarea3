package com.example.demo2s2.data;


import java.util.ArrayList;

import org.w3c.dom.Text;

import android.R.array;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.demo2s2.helper.BitmapLRUCache;
import com.example.demo2s2.R;
import com.example.demo2s2.activities.MainActivity;
import com.example.demo2s2.fragments.ComunidadFragment;

public class ImageAdapter extends BaseAdapter {
	private ImageLoader imageLoader;
	private ArrayList<Image> dataArray;
	
	private LayoutInflater inflater;
	
	public ImageAdapter(Context context, ArrayList<Image> dataArray) {
		this.dataArray = dataArray;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(ComunidadFragment.requestqueue, new BitmapLRUCache());
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataArray.size();
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Image current = dataArray.get(position);
		if (convertView == null){
			convertView = inflater.inflate(R.layout.fragment_imagenapi, null);
			holder = new ViewHolder();
			holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
			holder.imgFlag = (NetworkImageView)convertView.findViewById(R.id.imageFlag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtName.setText(current.getUserName());
		holder.imgFlag.setImageUrl(current.getImgUrl(), imageLoader);
		
		return convertView;
	}
	
	static class ViewHolder {
		public TextView txtName;
		public NetworkImageView imgFlag;
		
	}
	
	public static Bitmap decodeSampleBitmapFromResources(Resources res, int resId, int reqWith, int reqHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWith, reqHeight);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeResource(res, resId, options);
		
	}
	
	public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = Math.round((float) height / (float) reqHeight);
	        final int halfWidth = Math.round ((float) width / (float) reqWidth);
	
	        inSampleSize = halfHeight < halfWidth ? halfHeight : halfWidth;
	        
	    }
	
	    return inSampleSize;
}

}
