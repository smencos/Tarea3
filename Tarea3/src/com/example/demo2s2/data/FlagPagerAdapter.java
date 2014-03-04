package com.example.demo2s2.data;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.demo2s2.R;
import com.example.demo2s2.fragments.FlagFragment;

public class FlagPagerAdapter extends FragmentPagerAdapter {
	private int [] arrayFlag = new int[]{
			R.drawable.calzado,
			R.drawable.lego,
			R.drawable.apple,
			R.drawable.android,
			R.drawable.camisas
	};
	
	public FlagPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = new FlagFragment();
		Bundle args = new Bundle();
		args.putInt(FlagFragment.RESOURCE, arrayFlag[position]);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayFlag.length;
	}

}
