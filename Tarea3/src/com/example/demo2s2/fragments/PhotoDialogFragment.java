package com.example.demo2s2.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.demo2s2.R;



public class PhotoDialogFragment extends DialogFragment {
	
	NoticeDialogListener listener;
	
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			listener = (NoticeDialogListener)getActivity();
			
		} catch (ClassCastException e) {
			Log.e("ERROR", Log.getStackTraceString(e));
		}
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.btn_photo).setPositiveButton("Fotografia", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogPositiveClick();
				
			}
		}).setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogNegativeClick();
				
			}
		});
		return builder.create();
	}
	
	public interface NoticeDialogListener {
		public void onDialogPositiveClick();
		public void onDialogNegativeClick();

	}

}
