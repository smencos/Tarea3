package com.example.demo2s2.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.demo2s2.R;
import com.example.demo2s2.data.Tienda;
import com.example.demo2s2.fragments.ComunidadFragment;
import com.example.demo2s2.fragments.CountriesContentFragment;
import com.example.demo2s2.fragments.CountriesFlagFragment;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class MainActivity extends ActionBarActivity {
	private ListView drawerList;
	private String[] drawerOptions;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private Fragment[] fragments = new Fragment[]{
			new CountriesFlagFragment(), new CountriesContentFragment(), (Fragment) new ComunidadFragment()
	};
	
	public static String nombretienda = "";
	public static String direcciontienda = "";
	public static String telefonotienda = "";
	public static String horariotienda = "";
	public static String urltienda = "";
	public static String emailtienda = "";
	
	
	public static Tienda tienda1;
	public static Tienda tienda2 = new Tienda("Mall Mixco", "Ciudad de Mixco","2388-5601", "8:00AM - 5:00PM", "www.tienda2.com", "tienda2@tienda.com");
	public static Tienda tienda3 = new Tienda("Mall Villanueva", "Ciudad de Villanueva","2388-5602", "8:00AM - 6:00PM", "www.tienda3.com", "tienda3@tienda.com");
	
	public static final Hashtable<String, Tienda> tiendastarea1 = new Hashtable<String, Tienda>();

	public static RequestQueue requestqueue1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestqueue1 = Volley.newRequestQueue(this);
		
		
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
		
	
		try {
			APICall();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tienda1 = new Tienda(nombretienda, direcciontienda,telefonotienda, horariotienda, urltienda,emailtienda);
		
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
	
	private void APICall() throws IOException {
		StringBuilder buf=new StringBuilder();
	    InputStream json=getAssets().open("contents.json");
	    BufferedReader in=
	        new BufferedReader(new InputStreamReader(json));
	    String str;

	    while ((str=in.readLine()) != null) {
	      buf.append(str);
	    }

	    in.close();
		
	    String m = buf.toString();
	    
		
		
		JsonFactory f = new JsonFactory();
		JsonParser jp  = f.createJsonParser(m);
		jp.nextToken();
		
		
		
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jp.getCurrentName();
			jp.nextToken(); 
			 
			
					Log.e("dondevoy", fieldname);
					if(fieldname.equals("type")){
					
						
						
					}
					if(fieldname.equals("name")){
						nombretienda = jp.getText();
						
						
					}
					if(fieldname.equals("direccion")){
						direcciontienda = jp.getText();
						
						
					}
					if(fieldname.equals("telefono")){
						
						telefonotienda = jp.getText();
						
						
					}
					if(fieldname.equals("horario")){
						
						horariotienda = jp.getText();
						
						
					}
					if(fieldname.equals("url")){
						
						urltienda = jp.getText();
						
						
					}
					if(fieldname.equals("email")){
						
						emailtienda = jp.getText();
						
						
					}
				
				
				
			
		}
		jp.close();
		
		Log.e("json_final", nombretienda);
		/*// TODO Auto-generated method stub
		//String url = Helper.getRecentMediaUrl("lego");
		
		Uri uri = Uri.parse("android.resource://" + getPackageName()
                + "/assets/" + "mydemo.txt");
		String url = uri.toString();
		Log.e("envio", url);
		

		
		
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject arg0) {
				
				Log.e("entre", "entre");
				
				JSONArray data;
				try{
					data = arg0.getJSONArray("store");
					for(int i = 0; i< data.length(); i++){
						JSONObject element = data.getJSONObject(i);
						String type = element.getString("type");
						if(type.equals("name")){
							
							
							String userName = element.getString("name");
							
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
		
		*/
	} 
}
