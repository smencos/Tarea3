package com.example.demo2s2.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.demo2s2.R;
import com.example.demo2s2.activities.CountryDetailActivity;
import com.example.demo2s2.activities.MainActivity;
import com.example.demo2s2.data.Tienda;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CountriesListFragment extends Fragment implements
		OnItemClickListener {
	ListView list ;
	String country  ="";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setHasOptionsMenu(true);
		MainActivity.tiendastarea1.put("Mall Guatemala", MainActivity.tienda1);
		MainActivity.tiendastarea1.put("Mall Mixco", MainActivity.tienda2);
		MainActivity.tiendastarea1.put("Mall Villanueva", MainActivity.tienda3);
		String[] arrayCountries = new String[]{"Mall Guatemala", "Mall Mixco", "Mall Villanueva"};

		ArrayList<String> countries = new ArrayList<String>(Arrays.asList(arrayCountries));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
		
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);

		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countries_list, container,false);
		list = (ListView) view.findViewById(R.id.listView);
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


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.main, menu);
	}
	


	

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		boolean landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		MenuItem share = menu.getItem(menu.size() -1);
		share.setVisible(landscape);

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		country = adapterView.getItemAtPosition(position).toString();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
			CountryInfoFragment fragment = (CountryInfoFragment) manager.findFragmentById(R.id.fragmentCountryInfo);
			fragment.loadWebViewContent(country);
			
		} else {
			Intent intent = new Intent(getActivity(), CountryDetailActivity.class);
			intent.putExtra(CountryDetailActivity.COUNTRY, country);
			startActivity(intent);
		}
		
	}


}
