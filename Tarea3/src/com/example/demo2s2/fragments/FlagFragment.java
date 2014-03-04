package com.example.demo2s2.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.demo2s2.R;

public class FlagFragment extends Fragment implements
OnItemClickListener  {
	String country = "";
	public final static String RESOURCE = "resource";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_flag, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
		Bundle args = getArguments();
		
		imageView.setImageResource(args.getInt(RESOURCE));
		return view;
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		return onOptionsItemSelected(item);
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		country = ((TextView)info.targetView).getText().toString();
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_share:
				if (!country.equals("")) {
					String url = "http://es.m.wikipedia.org/wiki/" + country;
					String msg = getString(R.string.msg_share, country, url);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT, msg);
					intent.setType("text/plain");
					startActivity(Intent.createChooser(intent, getString(R.string.action_share)));
				}
				return true; 
			default: 
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.main, menu);
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	

}
