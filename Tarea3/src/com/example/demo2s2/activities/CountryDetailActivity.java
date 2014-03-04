package com.example.demo2s2.activities;

import com.example.demo2s2.R;
import com.example.demo2s2.R.id;
import com.example.demo2s2.R.layout;
import com.example.demo2s2.R.menu;
import com.example.demo2s2.R.string;
import com.example.demo2s2.data.GetData;
import com.example.demo2s2.data.Tienda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class CountryDetailActivity extends FragmentActivity {
	private String country ="";
	public static final String COUNTRY ="country";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_detail);
		
		Intent intent = getIntent();
		country = (intent.getStringExtra(COUNTRY));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_share:
				if (!country.equals("")) {
					String url = "Te comparto la tienda " + country;
					Tienda tiendaMos = MainActivity.tiendastarea1.get(country);
					String nombre = tiendaMos.name1;
					String dire = tiendaMos.direccion1;
					String we = tiendaMos.website1;
					String msg = url + " direccion: " + dire + " Webpage: " + we;
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

	public String getCountry() {
		return country;
	}



}
