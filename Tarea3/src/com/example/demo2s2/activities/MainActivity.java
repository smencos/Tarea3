package com.example.demo2s2.activities;


import java.util.Hashtable;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.demo2s2.R;
import com.example.demo2s2.data.Tienda;
import com.example.demo2s2.fragments.AboutFragment;
import com.example.demo2s2.fragments.ComunidadFragment;
import com.example.demo2s2.fragments.CountriesContentFragment;
import com.example.demo2s2.fragments.CountriesFlagFragment;
import com.example.demo2s2.fragments.FlagFragment;

public class MainActivity extends ActionBarActivity {
	private ListView drawerList;
	private String[] drawerOptions;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private Fragment[] fragments = new Fragment[]{
			new CountriesFlagFragment(), new CountriesContentFragment(), (Fragment) new ComunidadFragment()
	};
	
	public static Tienda tienda1 = new Tienda("Mall Guatemala", "Ciudad de Guatemala","2388-5600", "8:00AM - 5:00PM", "www.tienda1.com", "tienda1@tienda.com");
	public static Tienda tienda2 = new Tienda("Mall Mixco", "Ciudad de Mixco","2388-5601", "8:00AM - 5:00PM", "www.tienda2.com", "tienda2@tienda.com");
	public static Tienda tienda3 = new Tienda("Mall Villanueva", "Ciudad de Villanueva","2388-5602", "8:00AM - 6:00PM", "www.tienda3.com", "tienda3@tienda.com");
	
	public static final Hashtable<String, Tienda> tiendastarea1 = new Hashtable<String, Tienda>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		drawerList = (ListView) findViewById(R.id.leftDrawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerOptions = getResources().getStringArray(R.array.drawer_options);
		
		drawerList.setAdapter(new ArrayAdapter<String>(this, 
													   R.layout.drawer_list_item, 
													   drawerOptions));
		drawerList.setItemChecked(0, true);
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		drawerToggle= new ActionBarDrawerToggle(this, 
											drawerLayout, 
											R.drawable.ic_drawer, 
											R.string.drawer_open, 
											R.string.drawer_close){
			public void onDrawerClosed(View view) {
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
			
			public void onDrawerOpened (View drawerView){
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};
		
		drawerLayout.setDrawerListener(drawerToggle);
		
		ActionBar actionBar = getSupportActionBar();
		
		actionBar.setTitle(drawerOptions[0]);
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().add(R.id.contentFrame, fragments[0])
								  .add(R.id.contentFrame, fragments[1])
								  .add(R.id.contentFrame, fragments[2])
								  .hide(fragments[1])
								  .hide(fragments[2])
								  .commit();
		
	
	}
	
	public void setContent (int index){
		Fragment toHide = null;
		
		Fragment toShow = null;
		Fragment toHide2 = null;
		ActionBar actionBar = getSupportActionBar();
		
		switch (index){
			case 0:
				toHide = fragments[1];
				toShow = fragments[0];
				toHide2 = fragments[2];
				
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				
				
				break;
			case 1:
				toHide = fragments[0];
				toShow = fragments[1];
				toHide2 = fragments[2];
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				break;
			case 2:
				toHide = fragments[0];
				toHide2 = fragments[1];
				toShow = fragments[2];
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				break;
			
		}
		actionBar.setTitle(drawerOptions[index]);
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().hide(toHide)
								  .show(toShow)
								  .hide(toHide2)
								  .commit();
		
		drawerList.setItemChecked(index, true);
		drawerLayout.closeDrawer(drawerList);
		
	}
	
	@Override
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		super.onPostCreate(savedInstanceState);
		
		drawerToggle.syncState();
	}

	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home) {
			if (drawerLayout.isDrawerOpen(drawerList)){
				drawerLayout.closeDrawer(drawerList);
			} else {
				drawerLayout.openDrawer(drawerList);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class DrawerItemClickListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			setContent(position);
			
		}
		
	}
}
