package com.snbc.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter.BarPrinterBuilder;
import com.snbc.sdk.barcode.IBarInstruction.ILabelConfig;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.PaperMode;
import com.snbc.sdk.barcode.enumeration.PrintMethod;
import com.snbc.sdk.barcode.enumeration.PrintMode;
import com.snbc.sdk.connect.connectImpl.BluetoothConnect;
import com.snbc.sdk.connect.connectImpl.NETConnect;
import com.snbc.sdk.connect.connectImpl.SerialConnect;
import com.snbc.sdk.connect.connectImpl.USBConnect;
import com.snbc.sdk.connect.connectImpl.WifiConnect;
import com.snbc.sdk.exception.BarFunctionNoSupportException;
import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.CGSize;
import com.snbc.demo.General.FontInfo;
import com.snbc.demo.General.SNBCApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
@SuppressWarnings("deprecation")
public class ConnectivityActivity extends TabActivity {
	private TabHost tabhost;
	private TabHost.TabSpec ts_widget_wifiSpec;
	private TabHost.TabSpec ts_widget_bluetoothSpec;
	private TabHost.TabSpec ts_widget_usbSpec;
	private TabHost.TabSpec ts_widget_netSpec;
	private TabHost.TabSpec ts_widget_comSpec;
	private Button btn_wifi_discover;
	private Button btn_bluetooth_discover;
	private Button btn_usb_connect;
	private Button btn_net_discover;
	private Button btn_com_connect;
	private TextView tv_back;
	private ListView lv_wifi_result;
	private ListView lv_bluetooth_result;
	private ListView lv_net_result;
	private LinearLayout ll_wifi_info;
	private LinearLayout ll_bluetooth_info;
	private LinearLayout ll_net_info;
	private static final String DEVICE_NAME = "DEVICE_NAME";
	private static final String DEVICE_IP = "DEVICE_IP";
	private EditText et_port_number;
	private EditText net_port_number;
	private Spinner sp_wifi_instrution_set;
	private Spinner sp_bluetooth_instrution_set;
	private Spinner sp_usb_instrution_set;
	private Spinner sp_net_instrution_set;
	private Spinner sp_com_instrution_set;
	private List<String> instrutionList = new ArrayList<String>();
	private ArrayAdapter<String> instrutionAdapter ;
	private String wifi_connect_ip;
	private String wifi_connect_name;
	private String bluetooth_connect_ip;
	private String bluetooth_connect_name;
	private String net_connect_ip;
	private String net_connect_name;
	private Button btn_net_connect;
	private Button btn_wifi_connect;
	private Button btn_bluetooth_connect;
	private String[]  OSImageFileForPrintArray = new String[]{"lion.bmp","dragon.bmp","color.bmp","color.jpg"};
	private Spinner baudrate_spi;
	private Button openBtn; 	// Open Port
	private int error_code = 0;
	private SerialConnect interface_com = null;
	public static final int POS_SUCCESS=1000;
	private boolean sdk_flag = false;
	private TextView set_port;
	private int com_baudrate_connect;

	private String pathSeparator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connectivity);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		initView();
	}
	//init view
	private void initView() {
		tv_back = (TextView) findViewById(R.id.tv_back_connectivity);

		tabhost = getTabHost();
		ts_widget_wifiSpec = tabhost.newTabSpec("WIFI");
		ts_widget_wifiSpec.setIndicator(getResources().getString(R.string.wifi_tag));
		ts_widget_wifiSpec.setContent(R.id.widget_layout_wifi);
		btn_wifi_discover = (Button) findViewById(R.id.btn_wifi_discover);
		lv_wifi_result = (ListView) findViewById(R.id.lv_wifi_result);

		ts_widget_bluetoothSpec = tabhost.newTabSpec("BLUETOOTH");
		ts_widget_bluetoothSpec.setIndicator(getResources().getString(R.string.bluetooth_tag));
		ts_widget_bluetoothSpec.setContent(R.id.widget_layout_bluetooth);
		btn_bluetooth_discover = (Button) findViewById(R.id.btn_bluetooth_discover);
		lv_bluetooth_result = (ListView) findViewById(R.id.lv_bluetooth_result);

		ts_widget_usbSpec = tabhost.newTabSpec("USB");
		ts_widget_usbSpec.setIndicator(getResources().getString(R.string.usb_tag));
		ts_widget_usbSpec.setContent(R.id.widget_layout_usb);
		btn_usb_connect = (Button) findViewById(R.id.btn_usb_connect);

		ts_widget_comSpec = tabhost.newTabSpec("COM");
		ts_widget_comSpec.setIndicator(getResources().getString(R.string.com_tag));
		ts_widget_comSpec.setContent(R.id.widget_layout_com);
		btn_com_connect = (Button) findViewById(R.id.btn_com_connect);

		ts_widget_netSpec = tabhost.newTabSpec("NET");
		ts_widget_netSpec.setIndicator(getResources().getString(R.string.net_tag));
		ts_widget_netSpec.setContent(R.id.widget_layout_net);
		btn_net_discover = (Button) findViewById(R.id.btn_net_discover);
		lv_net_result = (ListView) findViewById(R.id.lv_net_result);


		tabhost.addTab(ts_widget_wifiSpec);
		tabhost.addTab(ts_widget_bluetoothSpec);
		tabhost.addTab(ts_widget_usbSpec);
		tabhost.addTab(ts_widget_netSpec);
		tabhost.addTab(ts_widget_comSpec);
		tabhost.setCurrentTab(1);

		et_port_number = (EditText) findViewById(R.id.et_port_number);
		net_port_number = (EditText) findViewById(R.id.net_port_number);
		sp_wifi_instrution_set = (Spinner) findViewById(R.id.sp_wifi_instrution_set);
		sp_bluetooth_instrution_set = (Spinner) findViewById(R.id.sp_bluetooth_instrution_set);
		sp_usb_instrution_set = (Spinner) findViewById(R.id.sp_usb_instrution_set);
		sp_com_instrution_set = (Spinner) findViewById(R.id.sp_com_instrution_set);
		sp_net_instrution_set = (Spinner) findViewById(R.id.sp_net_instrution_set);
		ll_wifi_info = (LinearLayout) findViewById(R.id.ll_wifi_info);
		ll_bluetooth_info = (LinearLayout) findViewById(R.id.ll_bluetooth_info);
		ll_net_info = (LinearLayout) findViewById(R.id.ll_net_info);
		btn_wifi_connect = (Button) findViewById(R.id.btn_connect_wifi);
		btn_bluetooth_connect = (Button) findViewById(R.id.btn_connect_bluetooth);
		btn_net_connect = (Button) findViewById(R.id.btn_connect_net);

		//Set baud rate
		baudrate_spi = (Spinner)findViewById(R.id.com_baudrate_spi);
		String[] fonttype_items = {"1200","2400","4800","9600","19200","38400","57600","115200"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fonttype_items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		baudrate_spi.setAdapter(adapter);
		baudrate_spi.setSelection(5);

		tv_back.setOnClickListener(new BackListener());
		btn_wifi_discover.setOnClickListener(new WifiDiscoverListener());
		btn_wifi_connect.setOnClickListener(new WifiConnectListener());
		btn_bluetooth_discover.setOnClickListener(new BluetoothDiscoverListener());
		btn_bluetooth_connect.setOnClickListener(new BluetoothConnectListener());
		btn_net_discover.setOnClickListener(new NetDiscoverListener());
		btn_net_connect.setOnClickListener(new NetConnectListener());
		btn_usb_connect.setOnClickListener(new USBConnectListener());
		btn_com_connect.setOnClickListener(new SerialConnectListener());
		//instrutionList.add(InstructionType.BPLC.toString());
		for (int i = 0; i < InstructionType.values().length; i++) {
			instrutionList.add(InstructionType.values()[i].toString());
		}

		instrutionAdapter = new ArrayAdapter<String>(ConnectivityActivity.this, android.R.layout.simple_spinner_item, instrutionList);
		instrutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_usb_instrution_set.setAdapter(instrutionAdapter);
		sp_com_instrution_set.setAdapter(instrutionAdapter);
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ConnectivityActivity.this, MainActivity.class);
			startActivity(intent);
		}

	}

	/// \if English
	/// \defgroup WIFI_DISCOVER_DEVICE Discover WIFI device
	/// \elseif Chinese
	/// \defgroup WIFI_DISCOVER_DEVICE 搜索WIFI设备
	/// \endif
	/// \code
	private class WifiDiscoverListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (!WifiConnect.isWiFiActive(ConnectivityActivity.this)) {
				AlertDialogUtil.showDialog("please open wifi first",ConnectivityActivity.this);
				return ;
			}
			if(ll_wifi_info.getVisibility() == View.VISIBLE){
				ll_wifi_info.setVisibility(View.GONE);
			}
			Map<String, String> printInfoMap = null;
			List<Map<String,String>> wifi_devices_info = new ArrayList<Map<String,String>>();
			Map<String,String> wifi_item = null;

			try {
				printInfoMap = WifiConnect.searchDevice();
				for(Map.Entry<String, String> entry:printInfoMap.entrySet()){
					wifi_item = new HashMap<String, String>();
					wifi_item.put(DEVICE_NAME, entry.getValue());
					wifi_item.put(DEVICE_IP, entry.getKey());
					wifi_devices_info.add(wifi_item);
				}
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				return;
			}
			SimpleAdapter wifiAdapter = new SimpleAdapter(ConnectivityActivity.this, wifi_devices_info,
					R.layout.item_wifi_devices, new String[]{DEVICE_NAME,DEVICE_IP},new int[]{R.id.wifi_device_name,R.id.wifi_device_ip});
			lv_wifi_result.setAdapter(wifiAdapter);
			lv_wifi_result.setSelector(android.R.color.background_light);
			lv_wifi_result.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Map<String,String> map = (Map) parent.getItemAtPosition(position);
					wifi_connect_ip =  map.get(DEVICE_IP);
					wifi_connect_name = map.get(DEVICE_NAME);
					myHandler.sendEmptyMessage(0);
				}
			});

		}

	}
	/// \endcode

	/// \if English
	/// \defgroup WIFI_CONNECT_DEVICE Connect WIFI device
	/// \elseif Chinese
	/// \defgroup WIFI_CONNECT_DEVICE 连接WIFI设备
	/// \endif
	/// \code
	private class WifiConnectListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(btn_wifi_connect.getText().equals(getResources().getString(R.string.connect))){
				if ("".equals(et_port_number.getText().toString())) {
					Toast.makeText(ConnectivityActivity.this, "please input port number", Toast.LENGTH_SHORT).show();
					return ;
				}
				try {
					WifiConnect wifiConnect = new WifiConnect(wifi_connect_ip, Integer.parseInt(et_port_number.getText().toString()));
					SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
					wifiConnect.DecodeType(application.getDecodeType() );
					wifiConnect.connect();
					BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
					builder.buildDeviceConnenct(wifiConnect);
					String mType = sp_wifi_instrution_set.getItemAtPosition(sp_wifi_instrution_set.getSelectedItemPosition()).toString();
					builder.buildInstruction(InstructionType.valueOf(mType));
					final BarPrinter printer = builder.getBarPrinter();
					application.setPrinter(printer);
					application.setConnect(wifiConnect);

					if(InstructionType.valueOf(mType) != InstructionType.BPLC){
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
						updateStoredCustomFontArray(application,printer,ConnectivityActivity.this);
						updateStoredImageArray(application,printer,ConnectivityActivity.this);
						updateStoredFormatArray(application,printer,ConnectivityActivity.this);
						updateOSImageFileArray(application,printer,ConnectivityActivity.this);}
					else {
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
					}
					btn_wifi_connect.setText(getResources().getString(R.string.disconnect));

					if(InstructionType.valueOf(mType) != InstructionType.BPLA){
						AlertDialogUtil.showDialog("  Connect to print successful!\r\n The printer's name is "+printer.labelQuery().getPrinterName(), ConnectivityActivity.this);
					}else{
						AlertDialogUtil.showDialog("  Connect to print successful!", ConnectivityActivity.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				}
			}
			else if(btn_wifi_connect.getText().toString().equals(getResources().getString(R.string.disconnect))){
				WifiConnect wifiConnect = (WifiConnect) ((SNBCApplication)ConnectivityActivity.this.getApplication()).getConnect();
				if(null != wifiConnect){
					try {
						wifiConnect.disconnect();
						applicationClean();
						btn_wifi_connect.setText(getResources().getString(R.string.connect));
					} catch (Exception e) {
						e.printStackTrace();
						AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
					}
				}
			}

		}

	}
	/// \if English
	/// \defgroup NET_DISCOVER_DEVICE Discover WIFI device
	/// \elseif Chinese
	/// \defgroup NET_DISCOVER_DEVICE 搜索WIFI设备
	/// \endif
	/// \code
	private class NetDiscoverListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(ll_net_info.getVisibility() == View.VISIBLE){
				ll_net_info.setVisibility(View.GONE);
			}
			Map<String, String> printInfoMap = null;
			List<Map<String,String>> net_devices_info = new ArrayList<Map<String,String>>();
			Map<String,String> net_item = null;

			try {
				printInfoMap = NETConnect.NetsearchDevice();
				for(Map.Entry<String, String> entry:printInfoMap.entrySet()){
					net_item = new HashMap<String, String>();
					net_item.put(DEVICE_NAME, entry.getValue());
					net_item.put(DEVICE_IP, entry.getKey());
					net_devices_info.add(net_item);
				}
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				return;
			}
			SimpleAdapter netAdapter = new SimpleAdapter(ConnectivityActivity.this, net_devices_info,
					R.layout.item_net_devices, new String[]{DEVICE_NAME,DEVICE_IP},new int[]{R.id.net_device_name,R.id.net_device_ip});
			lv_net_result.setAdapter(netAdapter);
			lv_net_result.setSelector(android.R.color.background_light);
			lv_net_result.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Map<String,String> map = (Map) parent.getItemAtPosition(position);
					net_connect_ip =  map.get(DEVICE_IP);
					net_connect_name = map.get(DEVICE_NAME);
					myHandler.sendEmptyMessage(2);
				}
			});



		}

	}
	/// \endcode

	/// \if English
	/// \defgroup WIFI_CONNECT_DEVICE Connect WIFI device
	/// \elseif Chinese
	/// \defgroup WIFI_CONNECT_DEVICE 连接WIFI设备
	/// \endif
	/// \code
	private class NetConnectListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(btn_net_connect.getText().equals(getResources().getString(R.string.connect))){
				if ("".equals(net_port_number.getText().toString())) {
					Toast.makeText(ConnectivityActivity.this, "please input port number", Toast.LENGTH_SHORT).show();
					return ;
				}
				try {
					NETConnect netConnect = new NETConnect(net_connect_ip, Integer.parseInt(net_port_number.getText().toString()));
					SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
					netConnect.DecodeType(application.getDecodeType() );
					netConnect.connect();
					BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
					builder.buildDeviceConnenct(netConnect);
					String mType = sp_net_instrution_set.getItemAtPosition(sp_net_instrution_set.getSelectedItemPosition()).toString();
					builder.buildInstruction(InstructionType.valueOf(mType));
					final BarPrinter printer = builder.getBarPrinter();
					application.setPrinter(printer);
					application.setConnect(netConnect);

					if(InstructionType.valueOf(mType) != InstructionType.BPLC){
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
						updateStoredCustomFontArray(application,printer,ConnectivityActivity.this);
						updateStoredImageArray(application,printer,ConnectivityActivity.this);
						updateStoredFormatArray(application,printer,ConnectivityActivity.this);
						updateOSImageFileArray(application,printer,ConnectivityActivity.this);}
					else {
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
					}
					btn_wifi_connect.setText(getResources().getString(R.string.disconnect));
					if(InstructionType.valueOf(mType) != InstructionType.BPLA){
						AlertDialogUtil.showDialog("  Connect to print successful!\r\n"/* The printer's name is "+printer.labelQuery().getPrinterName()*/, ConnectivityActivity.this);
					}else{
						AlertDialogUtil.showDialog("  Connect to print successful!", ConnectivityActivity.this);
					}
					btn_net_connect.setText(getResources().getString(R.string.disconnect));
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				}
			}
			else if(btn_net_connect.getText().toString().equals(getResources().getString(R.string.disconnect))){
				NETConnect netConnect = (NETConnect) ((SNBCApplication)ConnectivityActivity.this.getApplication()).getConnect();
				if(null != netConnect){
					try {
						netConnect.disconnect();
						applicationClean();
						btn_net_connect.setText(getResources().getString(R.string.connect));
					} catch (Exception e) {
						e.printStackTrace();
						AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
					}
				}
			}



		}

	}
	/// \endcode


	/// \if English
	/// \defgroup BLUETOOTH_DISCOVER_DEVICE Discover BLUETOOTH device
	/// \elseif Chinese
	/// \defgroup BLUETOOTH_DISCOVER_DEVICE 搜索BLUETOOTH设备
	/// \endif
	/// \code
	private class BluetoothDiscoverListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			BluetoothAdapter blueToothAdapter= BluetoothAdapter.getDefaultAdapter();
			if(!blueToothAdapter.isEnabled()){
				blueToothAdapter.enable();
			}
			if(ll_bluetooth_info.getVisibility() == View.VISIBLE){
				ll_bluetooth_info.setVisibility(View.GONE);
			}
			Set<BluetoothDevice> blueToothDevices =  blueToothAdapter.getBondedDevices();
			List<Map<String,String>> blueTooth_devices_info = new ArrayList<Map<String,String>>();
			if(blueToothDevices.size()>0){
				Map<String,String> blueTooth_item = null;
				for (BluetoothDevice device : blueToothDevices) {
					blueTooth_item = new HashMap<String, String>();
					blueTooth_item.put(DEVICE_NAME, device.getName());
					blueTooth_item.put(DEVICE_IP, device.getAddress());
					blueTooth_devices_info.add(blueTooth_item);
				}
			}else{
				AlertDialogUtil.showDialog("please connect bluetooth first", ConnectivityActivity.this);
				return;
			}

			SimpleAdapter blueToothDataAdapter = new SimpleAdapter(ConnectivityActivity.this, blueTooth_devices_info,
					R.layout.item_bluetooth_devices, new String[]{DEVICE_NAME,DEVICE_IP},new int[]{R.id.bluetooth_device_name,R.id.bluetooth_device_ip});
			lv_bluetooth_result.setAdapter(blueToothDataAdapter);
			lv_bluetooth_result.setSelector(android.R.color.background_light);
			lv_bluetooth_result.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Map<String,String> map = (Map) parent.getItemAtPosition(position);
					bluetooth_connect_ip =  map.get(DEVICE_IP);
					bluetooth_connect_name = map.get(DEVICE_NAME);
					myHandler.sendEmptyMessage(1);
				}
			});
		}

	}
	/// \endcode

	/// \if English
	/// \defgroup BLUETOOTH_CONNECT_DEVICE Connect BLUETOOTH device
	/// \elseif Chinese
	/// \defgroup BLUETOOTH_CONNECT_DEVICE 连接蓝牙设备
	/// \endif
	/// \code
	private class BluetoothConnectListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(btn_bluetooth_connect.getText().toString().equals(getResources().getString(R.string.connect))){
				try {

					BluetoothConnect bluetoothConnect = new BluetoothConnect(BluetoothAdapter.getDefaultAdapter(), bluetooth_connect_ip);
					SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
					//bluetoothConnect.DecodeType(application.getDecodeType() );
					bluetoothConnect.DecodeType("GB18030");
					bluetoothConnect.connect();
					BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
					builder.buildDeviceConnenct(bluetoothConnect);
					String mType = sp_bluetooth_instrution_set.getItemAtPosition(sp_bluetooth_instrution_set.getSelectedItemPosition()).toString();
					builder.buildInstruction(InstructionType.valueOf(mType));
					final BarPrinter printer = builder.getBarPrinter();
					application.setPrinter(printer);
					application.setConnect(bluetoothConnect);

					if(InstructionType.valueOf(mType) != InstructionType.BPLC){
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
						updateStoredCustomFontArray(application,printer,ConnectivityActivity.this);
						updateStoredImageArray(application,printer,ConnectivityActivity.this);
						updateStoredFormatArray(application,printer,ConnectivityActivity.this);
						updateOSImageFileArray(application,printer,ConnectivityActivity.this);}
					else {
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
					}
					btn_wifi_connect.setText(getResources().getString(R.string.disconnect));
					if(InstructionType.valueOf(mType) != InstructionType.BPLA){
						AlertDialogUtil.showDialog("  Connect to print successful!\r\n"/* The printer's name is "+printer.labelQuery().getPrinterName()*/, ConnectivityActivity.this);
					}else{
						AlertDialogUtil.showDialog("  Connect to print successful!", ConnectivityActivity.this);
					}
					btn_bluetooth_connect.setText(getResources().getString(R.string.disconnect));
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				}
			}
			else if (btn_bluetooth_connect.getText().toString().equals(getResources().getString(R.string.disconnect))){
				BluetoothConnect bluetoothConnect = (BluetoothConnect) ((SNBCApplication)getApplication()).getConnect();
				if(null != bluetoothConnect){
					try {
						bluetoothConnect.disconnect();
						applicationClean();
						btn_bluetooth_connect.setText(getResources().getString(R.string.connect));
					} catch (IOException e) {
						e.printStackTrace();
						AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
					}
				}
			}
		}

	}
	/// \endcode
	/// \if English
	/// \defgroup COM_CONNECT_DEVICE Connect COM device
	/// \elseif Chinese
	/// \defgroup COM_CONNECT_DEVICE 连接COM设备
	/// \endif
	/// \code
	private class SerialConnectListener implements OnClickListener{

		@Override
		public void onClick(View v) {


			if(btn_com_connect.getText().toString().equals(getResources().getString(R.string.connect))){
				try {
					String port_name = null;
					String spi_str;
					int baud_rate = 0;
					TextView set_port = (TextView) findViewById(R.id.com_port);
					port_name = (String)set_port.getText().toString();
					spi_str = baudrate_spi.getSelectedItem().toString();
					baud_rate = Integer.parseInt(spi_str);
					SerialConnect comConnect = new SerialConnect(new File(port_name),baud_rate);
					SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
					comConnect.DecodeType(application.getDecodeType() );
					comConnect.connect();

					BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
					builder.buildDeviceConnenct(comConnect);
					String mType = sp_com_instrution_set.getItemAtPosition(sp_com_instrution_set.getSelectedItemPosition()).toString();
					builder.buildInstruction(InstructionType.valueOf(mType));
					final BarPrinter printer = builder.getBarPrinter();
					application.setPrinter(printer);
					application.setConnect(comConnect);


					if(InstructionType.valueOf(mType) != InstructionType.BPLC){
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
						updateStoredCustomFontArray(application,printer,ConnectivityActivity.this);
						updateStoredImageArray(application,printer,ConnectivityActivity.this);
						updateStoredFormatArray(application,printer,ConnectivityActivity.this);
						updateOSImageFileArray(application,printer,ConnectivityActivity.this);}
					else {
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
					}

					if(InstructionType.valueOf(mType) != InstructionType.BPLA){
						//printer.labelQuery().getPrinterName();
						AlertDialogUtil.showDialog("  Connect to print successful!\r\n The printer's name is "/*+printer.labelQuery().getPrinterName()*/, ConnectivityActivity.this);
					}else{
						AlertDialogUtil.showDialog("  Connect to print successful!", ConnectivityActivity.this);
					}
					btn_com_connect.setText(getResources().getString(R.string.disconnect));
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
				}
			}
			else if(btn_com_connect.getText().toString().equals(getResources().getString(R.string.disconnect))){

				SerialConnect comConnect = (SerialConnect) ((SNBCApplication)getApplication()).getConnect();
				if(null != comConnect){
					try {
						comConnect.disconnect();
						applicationClean();
						btn_com_connect.setText(getResources().getString(R.string.connect));
					} catch (IOException e) {
						e.printStackTrace();
						AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
					}
				}
			}

		}

	}

	/// \if English
	/// \defgroup USB_CONNECT_DEVICE Connect USB device
	/// \elseif Chinese
	/// \defgroup USB_CONNECT_DEVICE 连接USB设备
	/// \endif
	/// \code
	private class USBConnectListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(btn_usb_connect.getText().toString().equals(getResources().getString(R.string.connect))){
				try {
					USBConnect usbConnect = new USBConnect(ConnectivityActivity.this);
					SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
					usbConnect.DecodeType(application.getDecodeType() );
					usbConnect.connect();
					BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
					builder.buildDeviceConnenct(usbConnect);
					String mType = sp_usb_instrution_set.getItemAtPosition(sp_usb_instrution_set.getSelectedItemPosition()).toString();
					builder.buildInstruction(InstructionType.valueOf(mType));
					final BarPrinter printer = builder.getBarPrinter();
					application.setPrinter(printer);
					application.setConnect(usbConnect);
					//printer.labelConfig().setPrintMode(PrintMode.TearOff, PaperMode.WebSensing,PrintMethod.DirectThermal, 0);
					if(InstructionType.valueOf(mType) != InstructionType.BPLC){
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
						updateStoredCustomFontArray(application,printer,ConnectivityActivity.this);
						updateStoredImageArray(application,printer,ConnectivityActivity.this);
						updateStoredFormatArray(application,printer,ConnectivityActivity.this);
						updateOSImageFileArray(application,printer,ConnectivityActivity.this);}
					else {
						updateStoredBuiltinFontArray(application,printer,ConnectivityActivity.this);
						updateOSFontFileArray(application,printer,ConnectivityActivity.this);
						updateOSFormatFileArray(application,printer,ConnectivityActivity.this);
						updateDiskSymbol(application,printer,ConnectivityActivity.this);
						updateOSImageFileForPrintArray(application,printer,ConnectivityActivity.this);
					}

				/*	if(InstructionType.valueOf(mType) != InstructionType.BPLA){
						AlertDialogUtil.showDialog("  Connect to print successful!\r\n The printer's name is "+printer.labelQuery().getPrinterName(), ConnectivityActivity.this);
					}else{
						AlertDialogUtil.showDialog("  Connect to print successful!", ConnectivityActivity.this);
					}*/
					btn_usb_connect.setText(getResources().getString(R.string.disconnect));
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog("  Connect to print fail!", ConnectivityActivity.this);
				}
			}
			else if(btn_usb_connect.getText().toString().equals(getResources().getString(R.string.disconnect))){

				USBConnect usbConnect = (USBConnect) ((SNBCApplication)getApplication()).getConnect();
				if(null != usbConnect){
					try {
						usbConnect.disconnect();
						applicationClean();
						btn_usb_connect.setText(getResources().getString(R.string.connect));
					} catch (IOException e) {
						e.printStackTrace();
						AlertDialogUtil.showDialog(e, ConnectivityActivity.this);
					}
				}
			}

		}

	}
	/// \endcode

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int id = msg.what;
			switch (id) {
				//WIFI item click
				case 0:
					et_port_number.setText("9100");
					sp_wifi_instrution_set.setAdapter(instrutionAdapter);
					ll_wifi_info.setVisibility(View.VISIBLE);

					break;
				//BLUETOOTH item click
				case 1:
					sp_bluetooth_instrution_set.setAdapter(instrutionAdapter);
					ll_bluetooth_info.setVisibility(View.VISIBLE);

					break;
				case 2:
					net_port_number.setText("9100");
					sp_net_instrution_set.setAdapter(instrutionAdapter);
					ll_net_info.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}

		};
	};

	private void updateStoredBuiltinFontArray(SNBCApplication application,BarPrinter printer,Context context){
		if(application.getStoredBuildinFontArray()!=null ){
			application.setStoredBuildinFontArray(null);
		}
		switch (printer.labelQuery().getPrinterLanguage()) {
			case BPLZ:
				application.setStoredBuildinFontArray(new FontInfo[]{	new FontInfo("Z:A.FNT",new CGSize(5, 9)),
						new FontInfo("Z:B.FNT",new CGSize(7, 11)),
						new FontInfo("Z:D.FNT",new CGSize(10, 18)),
						new FontInfo("Z:E6.FNT",new CGSize(10, 21)),
						new FontInfo("Z:E8.FNT",new CGSize(15, 28)),
						new FontInfo("Z:E12.FNT",new CGSize(20, 42)),
						new FontInfo("Z:E24.FNT",new CGSize(20, 42)),
						new FontInfo("Z:F.FNT",new CGSize(13, 26)),
						new FontInfo("Z:G.FNT",new CGSize(40, 60)),
						new FontInfo("Z:H6.FNT",new CGSize(11, 17)),
						new FontInfo("Z:H8.FNT",new CGSize(13, 21)),
						new FontInfo("Z:H12.FNT",new CGSize(22, 34)),
						new FontInfo("Z:H24.FNT",new CGSize(22, 34)),
						new FontInfo("Z:P.FNT",new CGSize(18, 20)),
						new FontInfo("Z:Q.FNT",new CGSize(24, 28)),
						new FontInfo("Z:R.FNT",new CGSize(31, 35)),
						new FontInfo("Z:S.FNT",new CGSize(35, 40)),
						new FontInfo("Z:T.FNT",new CGSize(42, 48)),
						new FontInfo("Z:U.FNT",new CGSize(53, 59)),
						new FontInfo("Z:V.FNT",new CGSize(71, 80)),
						new FontInfo("Z:0.FNT",new CGSize(12, 15))   });
				break;
			case BPLE:
				application.setStoredBuildinFontArray(new FontInfo[]{new FontInfo("1",new CGSize(8, 12),FontInfo.FONT_ZOOMIN_MODE_MULTIPLE),
						new FontInfo("2",new CGSize(10,16),FontInfo.FONT_ZOOMIN_MODE_MULTIPLE),
						new FontInfo("3",new CGSize(12,20),FontInfo.FONT_ZOOMIN_MODE_MULTIPLE),
						new FontInfo("4",new CGSize(14,24),FontInfo.FONT_ZOOMIN_MODE_MULTIPLE),
						new FontInfo("5",new CGSize(32,48),FontInfo.FONT_ZOOMIN_MODE_MULTIPLE)});
				break;
			case BPLT:
				application.setStoredBuildinFontArray(  new FontInfo[]{new FontInfo("0"),
						new FontInfo("1", new CGSize(8, 12)),
						new FontInfo("2", new CGSize(12, 20)),
						new FontInfo("3", new CGSize(16, 24)),
						new FontInfo("4", new CGSize(24, 32)),
						new FontInfo("5", new CGSize(32, 48)),
						new FontInfo("6", new CGSize(14, 19)),
						new FontInfo("7", new CGSize(21, 27)),
						new FontInfo("8", new CGSize(14, 25)),
						new FontInfo("ROMAN.TTF")});
				break;
			case BPLC:
				application.setStoredBuildinFontArray(new FontInfo[]{
						new FontInfo("0"),
						new FontInfo("1"),
						new FontInfo("2"),
						new FontInfo("4"),
						new FontInfo("5"),
						new FontInfo("6"),
						new FontInfo("7")});
				break;
			case BPLA:
				application.setStoredBuildinFontArray(new FontInfo[]{
						new FontInfo("0"),
						new FontInfo("1"),
						new FontInfo("2"),
						new FontInfo("3"),
						new FontInfo("4"),
						new FontInfo("5"),
						new FontInfo("6"),
						new FontInfo("7"),
						new FontInfo("8"),
						new FontInfo("000"),
						new FontInfo("001"),
						new FontInfo("002"),
						new FontInfo("003"),
						new FontInfo("004"),
						new FontInfo("005"),
						new FontInfo("006"),
						new FontInfo("007"),
						new FontInfo("P08"),
						new FontInfo("P10"),
						new FontInfo("P12"),
						new FontInfo("P14"),
						new FontInfo("P18")
				});
				break;
			default:
				break;
		}

	}
	private void updateOSImageFileArray(SNBCApplication application,BarPrinter printer,Context context){
		switch (printer.labelQuery().getPrinterLanguage()) {
			case BPLZ:
				application.setOsImageFileArray(new String[]{"lion.bmp", "dragon.bmp", "color.bmp", "color.jpg"});
				break;
			case BPLE:
				application.setOsImageFileArray(new String[]{"bw.pcx"});
				break;
			case BPLT:
				application.setOsImageFileArray(new String[]{"lion.bmp","dragon.bmp","color.bmp","color.jpg","bw.pcx"});
				break;
			case BPLC:
				application.setOsImageFileArray(new String[]{"bw.pcx"});
				break;
			case BPLA:
				application.setOsImageFileArray(new String[]{"lion.bmp","dragon.bmp","color.bmp","color.jpg","bw.pcx"});
				break;
			default:
				break;
		}
	}
	private void updateOSFontFileArray(SNBCApplication application,BarPrinter printer,Context context){
		switch (printer.labelQuery().getPrinterLanguage()) {
			case BPLZ:
				application.setOsFontFileArray(new String[]{"arial.ttf","timesi.ttf"});
				break;
			case BPLE:
				application.setOsFontFileArray(new String[]{""});
				break;
			case BPLT:
				application.setOsFontFileArray(new String[]{"arial.ttf","timesi.ttf"});
				break;
			case BPLC:
				application.setOsFontFileArray(new String[]{"arial.ttf","timesi.ttf"});
				break;
			case BPLA:
				break;
			default:
				break;
		}
	}
	private void updateOSFormatFileArray(SNBCApplication application,BarPrinter printer,Context context){

		switch (printer.labelQuery().getPrinterLanguage()) {
			case BPLZ:
				application.setOsFormatFileArray(new String[]{"formatBPLZ1.fmt","formatBPLZ2.fmt"});
				break;
			case BPLE:
				application.setOsFormatFileArray(new String[]{"formatBPLE1.fmt","formatBPLE2.fmt"});
				break;
			case BPLT:
				application.setOsFormatFileArray(new String[]{""});
				break;
			case BPLC:
				application.setOsFormatFileArray(new String[]{"formatBPLC1.fmt","formatBPLC2.fmt"});
				break;
			case BPLA:
				break;
			default:
				break;
		}
	}
	private void updateDiskSymbol(SNBCApplication application,BarPrinter printer,Context context){
		switch (printer.labelQuery().getPrinterLanguage()) {
			case BPLZ:
				application.setRamDiskSymbol("R:\\");
				application.setFlashDiskSymbol("E:\\");
				break;
			case BPLE:
				application.setRamDiskSymbol("");
				application.setFlashDiskSymbol("");
				break;
			case BPLT:
				application.setRamDiskSymbol("R:\\");
				application.setFlashDiskSymbol("E:\\");
				break;
			case BPLC:
				application.setRamDiskSymbol("");
				application.setFlashDiskSymbol("");
				break;
			case BPLA:
				application.setRamDiskSymbol("R:\\");
				application.setFlashDiskSymbol("E:\\");
				break;
			default:
				break;
		}
	}
	private void updateOSImageFileForPrintArray(SNBCApplication application,BarPrinter printer,Context context){
		application.setOsImageFileForPrintArray(OSImageFileForPrintArray);
	}
	public static void updateStoredCustomFontArray(SNBCApplication application,BarPrinter printer,Context context){
		try {
			ILabelQuery labelQuery = printer.labelQuery();
			if(labelQuery.getPrinterLanguage() == InstructionType.BPLA && !(application.getConnect() instanceof USBConnect)){
				application.setStoredCustomFontArray(null);
				return;
			}
			Set<String> fontSet;
			fontSet = labelQuery.getFontFileName();
			FontInfo[] storedBuiltinFontArray = application.getStoredBuildinFontArray();
			for (int i = 0; i < storedBuiltinFontArray.length; i++) {
				if(fontSet.contains(storedBuiltinFontArray[i].getFontName())){
					fontSet.remove(storedBuiltinFontArray[i].getFontName());
				}
			}
			application.setStoredCustomFontArray(fontSet.toArray(new String[fontSet.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, context);
		}
	}
	public static void updateStoredImageArray(SNBCApplication application,BarPrinter printer,Context context){
		try {
			ILabelQuery labelQuery = printer.labelQuery();
			if(labelQuery.getPrinterLanguage() == InstructionType.BPLA && !(application.getConnect() instanceof USBConnect)){
				application.setStoredImageArray(new String[]{"lion","bw"});
				return;
			}
			Set<String> imageSet;
			imageSet = labelQuery.getImageFileName();
			application.setStoredImageArray(imageSet.toArray(new String[imageSet.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, context);
		}
	}
	public static void updateStoredFormatArray(SNBCApplication application,BarPrinter printer,Context context){
		try {
			ILabelQuery labelQuery = printer.labelQuery();
			if(labelQuery.getPrinterLanguage() == InstructionType.BPLA){
				return;
			}
			Set<String> formatSet = labelQuery.getFormatFileName();
			application.setStoredFormatArray(formatSet.toArray(new String[formatSet.size()]));
		} catch(BarFunctionNoSupportException e){

		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, context);
		}
	}


	private void applicationClean(){
		SNBCApplication application = (SNBCApplication)ConnectivityActivity.this.getApplication();
		application.setConnect(null);
		application.setFlashDiskSymbol(null);
		application.setOsFontFileArray(null);
		application.setOsFormatFileArray(null);
		application.setOsImageFileArray(null);
		application.setOsImageFileForPrintArray(null);
		application.setPrinter(null);
		application.setRamDiskSymbol(null);
		application.setStoredBuildinFontArray(null);
		application.setStoredCustomFontArray(null);
		application.setStoredFormatArray(null);
		application.setStoredImageArray(null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
