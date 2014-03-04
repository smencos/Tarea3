package com.example.demo2s2.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

import com.example.demo2s2.R;
import com.example.demo2s2.activities.DepthPageTransformer;
import com.example.demo2s2.data.FlagPagerAdapter;

public class CountriesFlagFragment extends Fragment {
	ViewPager viewPager;
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		FlagPagerAdapter adapter = new FlagPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countries_flags, container,false);
		viewPager = (ViewPager)view.findViewById(R.id.pager);
		viewPager.setPageTransformer(true, new DepthPageTransformer());
		return view; 
	}
	
	

}
