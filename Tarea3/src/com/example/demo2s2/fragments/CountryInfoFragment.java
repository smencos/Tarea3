package com.example.demo2s2.fragments;

import com.example.demo2s2.R;
import com.example.demo2s2.R.id;
import com.example.demo2s2.R.layout;
import com.example.demo2s2.activities.CountryDetailActivity;
import com.example.demo2s2.activities.ImageDetailActivity;
import com.example.demo2s2.activities.MainActivity;
import com.example.demo2s2.data.Tienda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class CountryInfoFragment extends Fragment {
	
	private View view;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();
		if(activity instanceof CountryDetailActivity){
			String country = ((CountryDetailActivity)getActivity()).getCountry();
			loadWebViewContent(country);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_country_info, container, false);
		return view;
	}
	
	public void loadWebViewContent(String country) {
		
		Tienda tiendaMos = MainActivity.tiendastarea1.get(country);
		String nombre = tiendaMos.name1;
		String dire = tiendaMos.direccion1;
		final String te = tiendaMos.telefono1;
		String hora = tiendaMos.horario1;
		String ema = tiendaMos.email1;
		String we = tiendaMos.website1;
		TextView nombret = (TextView) view.findViewById(R.id.nombre_tienda);
		nombret.setText(nombre);
		TextView direc = (TextView) view.findViewById(R.id.direccion_tienda);
		direc.setText(dire);
		TextView tele = (TextView) view.findViewById(R.id.telefono_tienda);
		tele.setText(te);
		TextView horas = (TextView) view.findViewById(R.id.horario_tienda);
		horas.setText(hora);
		TextView emal = (TextView) view.findViewById(R.id.email_tienda);
		emal.setText(ema);
		TextView wel = (TextView) view.findViewById(R.id.sitio_tienda);
		wel.setText(we);
		
		
		Linkify.addLinks(wel, Linkify.ALL);
		Linkify.addLinks(emal, Linkify.ALL);
		Linkify.addLinks(direc, Linkify.MAP_ADDRESSES);
		Linkify.addLinks(tele, Linkify.ALL);
		
		Button btnllamar = (Button)view.findViewById(R.id.llamar);
		
		btnllamar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uri = "tel:" + te;
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(uri));
				startActivity(intent);
				
			}
		});
		
		Button btnImagen = (Button)view.findViewById(R.id.Imagen);
		btnImagen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity().getApplicationContext(), ImageDetailActivity.class);
				startActivity(intent);


				
			}
		});
		
		

	}
	
}
