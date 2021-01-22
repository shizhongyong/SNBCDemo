package com.snbc.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter.BarPrinterBuilder;
import com.snbc.sdk.barcode.IBarInstruction.ILabelConfig;
import com.snbc.sdk.barcode.IBarInstruction.ILabelControl;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
import com.snbc.sdk.barcode.IBarInstruction.ILabelFormat;
import com.snbc.sdk.barcode.IBarInstruction.ILabelImageAndFont;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.BarCodeType;
import com.snbc.sdk.barcode.enumeration.HRIPosition;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.PaperMode;
import com.snbc.sdk.barcode.enumeration.PrintMethod;
import com.snbc.sdk.barcode.enumeration.PrintMode;
import com.snbc.sdk.barcode.enumeration.PrinterDirection;
import com.snbc.sdk.barcode.enumeration.Rotation;
import com.snbc.sdk.barcode.enumeration.SensorMode;
import com.snbc.sdk.connect.connectImpl.BluetoothConnect;
import com.snbc.sdk.connect.connectImpl.USBConnect;
import com.snbc.sdk.connect.connectImpl.WifiConnect;
import com.snbc.sdk.exception.BarFunctionNoSupportException;


public class MainActivity extends Activity implements OnClickListener{

	//PortType Button
	private Button btn_connect_printer;
	private Button btn_query_status;
	private Button btn_print_buildin_font;
	private Button btn_print_custom_font;
	private Button btn_print_barcode;
	private Button btn_print_graphics;
	private Button btn_print_image;
	private Button btn_print_label;
	private Button btn_print_label_format;
	private Button btn_printer_configration;
	private Button btn_download_image;
	private Button btn_download_font;
	private Button btn_download_format;
	private Button btn_query_fonts;
	private Button btn_query_images;
	private Button btn_query_formats;
	private Button btn_direct_io;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		initView();

	}
	//init view
	private void initView(){
		btn_connect_printer = (Button) findViewById(R.id.btn_connectPrinter);
		btn_query_status = (Button) findViewById(R.id.btn_query_status);
		btn_print_buildin_font = (Button) findViewById(R.id.btn_print_in_font);
		btn_print_custom_font = (Button) findViewById(R.id.btn_print_with_custom_font);
		btn_print_barcode = (Button) findViewById(R.id.btn_print_barcode);
		btn_print_graphics = (Button) findViewById(R.id.btn_print_graphics);
		btn_print_image = (Button) findViewById(R.id.btn_print_image);
		btn_print_label = (Button) findViewById(R.id.btn_print_label);
		btn_print_label_format = (Button) findViewById(R.id.btn_print_label_with_format);
		btn_printer_configration = (Button) findViewById(R.id.btn_printer_configuration);
		btn_download_image = (Button) findViewById(R.id.btn_download_image);
		btn_download_font = (Button) findViewById(R.id.btn_download_font);
		btn_download_format = (Button) findViewById(R.id.btn_download_format);
		btn_query_fonts = (Button) findViewById(R.id.btn_query_fonts);
		btn_query_images = (Button) findViewById(R.id.btn_query_images);
		btn_query_formats = (Button) findViewById(R.id.btn_query_formats);
		btn_direct_io = (Button) findViewById(R.id.btn_direct_io);

		btn_connect_printer.setOnClickListener(this);
		btn_query_status.setOnClickListener(this);
		btn_print_buildin_font.setOnClickListener(this);
		btn_print_custom_font.setOnClickListener(this);
		btn_print_barcode.setOnClickListener(this);
		btn_print_graphics.setOnClickListener(this);
		btn_print_image.setOnClickListener(this);
		btn_print_label.setOnClickListener(this);
		btn_print_label_format.setOnClickListener(this);
		btn_printer_configration.setOnClickListener(this);
		btn_download_image.setOnClickListener(this);
		btn_download_font.setOnClickListener(this);
		btn_download_format.setOnClickListener(this);
		btn_query_fonts.setOnClickListener(this);
		btn_query_images.setOnClickListener(this);
		btn_query_formats.setOnClickListener(this);
		btn_direct_io.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if (null == ((SNBCApplication)getApplication()).getPrinter() && v.getId() != R.id.btn_connectPrinter) {
			AlertDialogUtil.showDialog(getResources().getString(R.string.connect_warn), MainActivity.this);
			return;
		};
		switch (v.getId()) {
			case R.id.btn_connectPrinter:
				intent.setClass(MainActivity.this, ConnectivityActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_query_status:

				intent.setClass(MainActivity.this, QueryPrinterStatusActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_in_font:
				intent.setClass(MainActivity.this, PrintTextUseBuiltinFontActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_with_custom_font:
				intent.setClass(MainActivity.this, PrintTextUseCustomFontActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_barcode:
				intent.setClass(MainActivity.this, PrintBarcodeActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_graphics:
				intent.setClass(MainActivity.this, PrintGraphicsActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_image:
				intent.setClass(MainActivity.this, PrintImageActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_label:
				intent.setClass(MainActivity.this, PrintLabelActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_print_label_with_format:
				intent.setClass(MainActivity.this, PrintLabelWithFormatActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_printer_configuration:
				intent.setClass(MainActivity.this, PrinterConfigurationActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_download_image:
				intent.setClass(MainActivity.this, DownloadImageActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_download_font:
				intent.setClass(MainActivity.this, DownloadFontActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_download_format:
				intent.setClass(MainActivity.this, DownloadFormatActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_query_fonts:
				intent.setClass(MainActivity.this, QueryFontNamesActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_query_images:
				intent.setClass(MainActivity.this, QueryImageNamesActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_query_formats:
				intent.setClass(MainActivity.this, QueryFormatNamesActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_direct_io:
				intent.setClass(MainActivity.this, DirectIOActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			exitBy2Click();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按�?出程�?", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			finish();
		}

	}
}


