package com.snbc.demo.General;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.SimpleAdapter;

public class AlertDialogUtil {
	public static  void showDialog(Exception e,Context context){
		String errorMsg = String.format("Exception class:%s \r\n Message:%s", e.getClass().toString(),e.getMessage());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(errorMsg);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});
		builder.show();
	}

	public static  void showDialog(String msg,Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});
		builder.show();
	}
}
