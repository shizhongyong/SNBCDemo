package com.snbc.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelConfig;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.PaperMode;
import com.snbc.sdk.barcode.enumeration.PrintMethod;
import com.snbc.sdk.barcode.enumeration.PrintMode;
import com.snbc.sdk.barcode.enumeration.PrinterDirection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PrinterConfigurationActivity extends Activity {
	private TextView tv_back_configure_printer;
	private Spinner sp_print_speed;
	private List<String> speedList = new ArrayList<String>();
	private ArrayAdapter<String> speedAdapter;
	private Spinner sp_print_density;
	private List<String> densityList = new ArrayList<String>();
	private ArrayAdapter<String> densityAdapter;
	private Spinner sp_print_direction;
	private List<String> directionList = new ArrayList<String>();
	private ArrayAdapter<String> directionAdapter;
	private Spinner sp_print_mode;
	private List<String> modeList = new ArrayList<String>();
	private ArrayAdapter<String> modeAdapter;
	private Spinner sp_print_method;
	private List<String> methodList = new ArrayList<String>();
	private ArrayAdapter<String> methodAdapter;
	private Spinner sp_paper_type;
	private List<String> paperTypeList = new ArrayList<String>();
	private ArrayAdapter<String> paperAdapter;
	private TextView tv_print_name;
	private TextView tv_firmware_version;
	private TextView tv_print_resolution;
	private Button btn_read_from_printer;
	private Button btn_update_to_printer;
	private Spinner sp_delete_file;
	private Button btn_format_flash;
	private String[] speedArray = new String[20];
	private String[] densityArray = new String[30];
	private String[] DIRECTIONARRAY = {"Normal","Rotate 180°"};
	private String[] MODEARRAY = {"Tear-off","Peel-off","Rewind","Cutter"};
	private String[] METHODARRAY = {"Direct Thermal","Thermal Transfer"};
	private String[] PAPERTYPE = {"Label With Gaps","Continuous","Label With Mark"};
	private SNBCApplication application;
	private ArrayAdapter<String> fileAdapter;
	private String[] customFontArray;
	private String[] storedImageArray;
	private String[] storedFormatArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_printer);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		initView();
	}
	//init View
	private void initView() {
		application = (SNBCApplication)getApplication();
		tv_back_configure_printer = (TextView) findViewById(R.id.tv_back_configure_printer);
		sp_print_speed= (Spinner) findViewById(R.id.sp_print_speed);
		sp_print_density = (Spinner) findViewById(R.id.sp_print_density);
		sp_print_direction = (Spinner) findViewById(R.id.sp_print_direction);
		sp_print_mode = (Spinner) findViewById(R.id.sp_print_mode);
		sp_print_method = (Spinner) findViewById(R.id.sp_print_method);
		sp_paper_type = (Spinner) findViewById(R.id.sp_paper_type);
		tv_print_name  = (TextView) findViewById(R.id.tv_print_name);
		tv_firmware_version = (TextView) findViewById(R.id.tv_firmware_version);
		tv_print_resolution = (TextView) findViewById(R.id.tv_print_resolution);
		btn_read_from_printer = (Button) findViewById(R.id.btn_read_from_printer);
		btn_update_to_printer = (Button) findViewById(R.id.btn_update_to_printer);
		sp_delete_file = (Spinner) findViewById(R.id.sp_delete_file);
		btn_format_flash = (Button) findViewById(R.id.btn_format_flash);

		for (int i = 0; i < 20; i++) {
			speedArray[i] = String.format("%d inch/s", i+1);
		}
		for (int i = 0; i < 30; i++) {
			densityArray[i] = String.format("%d", i+1);
		}


		speedList = Arrays.asList(speedArray);
		densityList = Arrays.asList(densityArray);
		directionList = Arrays.asList(DIRECTIONARRAY);
		modeList = Arrays.asList(MODEARRAY);
		methodList = Arrays.asList(METHODARRAY);
		paperTypeList = Arrays.asList(PAPERTYPE);

		try {
			speedAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,speedList);
			speedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_speed.setAdapter(speedAdapter);

			densityAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,densityList);
			densityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_density.setAdapter(densityAdapter);

			directionAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,directionList);
			directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_direction.setAdapter(directionAdapter);

			modeAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,modeList);
			modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_mode.setAdapter(modeAdapter);

			methodAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,methodList);
			methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_method.setAdapter(methodAdapter);

			paperAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_item,paperTypeList);
			paperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_paper_type.setAdapter(paperAdapter);

			customFontArray = application.getStoredCustomFontArray();
			storedImageArray = application.getStoredImageArray();
			String[] fileArray = null;
			InstructionType mType = application.getPrinter().labelQuery().getPrinterLanguage();
			if(mType == InstructionType.BPLA){
				btn_read_from_printer.setVisibility(View.GONE);
			}
			if(mType == InstructionType.BPLC){
				AlertDialogUtil.showDialog("Sorry, BPLC instruction can't support this function.", PrinterConfigurationActivity.this);
			}else{
				if(mType == InstructionType.BPLZ || mType == InstructionType.BPLE){
					storedFormatArray = application.getStoredFormatArray();
					fileArray = concatAll(new String[]{getResources().getString(R.string.prompt_delete_file)},customFontArray, storedFormatArray,storedImageArray);
				}else{
					if(null != customFontArray && null!=storedImageArray){
						fileArray = concatAll(new String[]{getResources().getString(R.string.prompt_delete_file)},customFontArray, storedImageArray);
					}
					fileArray = concatAll(new String[]{getResources().getString(R.string.prompt_delete_file)}, storedImageArray);
				}
				List<String> fileList = new ArrayList<String>(Arrays.asList(fileArray));
				fileAdapter = new ArrayAdapter<String>(PrinterConfigurationActivity.this, android.R.layout.simple_spinner_dropdown_item,fileList);
				fileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_delete_file.setAdapter(fileAdapter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, PrinterConfigurationActivity.this);
		}
		tv_back_configure_printer.setOnClickListener(new BackListener());
		btn_read_from_printer.setOnClickListener(new ReadFromPrinterListener());
		btn_update_to_printer.setOnClickListener(new UpdateToPrinterListener());
		sp_delete_file.setOnItemSelectedListener(new DeleteFileListener());
		btn_format_flash.setOnClickListener(new FormatFlashListener());

	}
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrinterConfigurationActivity.this,MainActivity.class);
			startActivity(intent);
		}

	}
	/// \if English
	/// \defgroup READ_PRINTER_CONFIGURATION Query printer configuration
	/// \elseif Chinese
	/// \defgroup READ_PRINTER_CONFIGURATION 查询打印机配置
	/// \endif
	/// \code
	private class ReadFromPrinterListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			try {
				BarPrinter printer = application.getPrinter();
				ILabelQuery labelQuery = printer.labelQuery();
				if (labelQuery.getPrinterLanguage() == InstructionType.BPLA) {
					AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", PrinterConfigurationActivity.this);
					return;
				}
				int printSpeed = labelQuery.getPrintSpeed();
				int printDensity = 15;
				InstructionType mType = labelQuery.getPrinterLanguage();
				if(mType == InstructionType.BPLZ || mType == InstructionType.BPLE || mType == InstructionType.BPLT){
					printDensity = labelQuery.getPrintDensity();
				}
				PrintMode printMode = labelQuery.getPrintMode();
				PrintMethod printMethod = labelQuery.getPrintMethod();
				PrinterDirection printerDirection = labelQuery.getPrintDirection();
				String printerName = labelQuery.getPrinterName();
				String firmwareVersion = labelQuery.getFirmwareVersion();
				int printResolution = labelQuery.getResolution();
				PaperMode paperMode = labelQuery.getPaperMode();

				tv_print_name.setText(printerName);
				tv_firmware_version.setText(firmwareVersion);
				tv_print_resolution.setText(printResolution+" DPI");
				sp_print_speed.setSelection(printSpeed -1);
				sp_print_density.setSelection(printDensity -1);
				if (printMode == PrintMode.TearOff) {
					sp_print_mode.setSelection(0);
				}else if(printMode == PrintMode.PeelOff){
					sp_print_mode.setSelection(1);
				}else if(printMode == PrintMode.Rewind){
					sp_print_mode.setSelection(2);
				}
				else{
					sp_print_mode.setSelection(3);
				}
				if(printMethod == PrintMethod.DirectThermal){
					sp_print_method.setSelection(0);
				}else{
					sp_print_method.setSelection(1);
				}

				if(printerDirection == PrinterDirection.Normal){
					sp_print_direction.setSelection(0);
				}else{
					sp_print_direction.setSelection(1);
				}

				if(paperMode == PaperMode.MarkSensing){
					sp_paper_type.setSelection(0);
				}else if(paperMode == PaperMode.Continue){
					sp_paper_type.setSelection(1);
				}else {
					sp_paper_type.setSelection(2);
				}

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrinterConfigurationActivity.this);
			}
		}
	}
	/// \endcode

	/// \if English
	/// \defgroup UPDATE_PRINTER_CONFIGURATION Update printer configuration
	/// \elseif Chinese
	/// \defgroup UPDATE_PRINTER_CONFIGURATION 更新打印机配置
	/// \endif
	/// \code
	private class UpdateToPrinterListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			try {
				BarPrinter printer = application.getPrinter();
				ILabelConfig labelConfig = printer.labelConfig();
				int printSpeed = sp_print_speed.getSelectedItemPosition()+1;
				int printDensity = sp_print_density.getSelectedItemPosition()+1;
				PrintMode printMode = null ;
				if (sp_print_mode.getSelectedItemPosition() == 0) {
					printMode = PrintMode.TearOff;
				}
				else if(sp_print_mode.getSelectedItemPosition() == 1) {
					printMode =  PrintMode.PeelOff;
				}
				else if(sp_print_mode.getSelectedItemPosition() == 2) {
					printMode =  PrintMode.Rewind;
				}
				else if(sp_print_mode.getSelectedItemPosition() == 3) {
					printMode =  PrintMode.Cutter;
				}
				PrintMethod printMethod;
				if(sp_print_method.getSelectedItemPosition() == 0){
					printMethod = PrintMethod.DirectThermal;
				}
				else{
					printMethod = PrintMethod.ThermalTransfer;
				}

				PrinterDirection printerDirection ;
				if(sp_print_direction.getSelectedItemPosition() == 0){
					printerDirection = PrinterDirection.Normal;
				}
				else{
					printerDirection = PrinterDirection.Rotation180;
				}
				PaperMode paperMode;
				if(sp_paper_type.getSelectedItemPosition() == 0){
					paperMode = PaperMode.MarkSensing;
				}
				else if(sp_paper_type.getSelectedItemPosition() == 1){
					paperMode = PaperMode.Continue;
				}
				else {
					paperMode = PaperMode.WebSensing;
				}

				labelConfig.setPrintSpeed(printSpeed);
				labelConfig.setPaperMode(paperMode);
				labelConfig.setPrintDensity(printDensity);
				if (printer.labelQuery().getPrinterLanguage() == InstructionType.BPLA) {
					labelConfig.setPrintMode(printMode, printMethod, 16);
				}else{
					labelConfig.setPrintMode(printMode, printMethod);
				}
				labelConfig.setPrintDirection(printerDirection);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrinterConfigurationActivity.this);
			}

		}

	}
	/// \endcode\

	/// \if English
	/// \defgroup DELETE_FILE Delete the file that stored in printer
	/// \elseif Chinese
	/// \defgroup DELETE_FILE 删除打印机中的文件
	/// \endif
	/// \code
	private class DeleteFileListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if(getResources().getString(R.string.prompt_delete_file).equals(parent.getSelectedItem().toString())){
				return;
			}
			new AlertDialog.Builder(PrinterConfigurationActivity.this)
					.setTitle("Confirm")
					.setMessage("delete "+parent.getSelectedItem().toString()+"?")
					.setPositiveButton("Confirm", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								String selectedItem = sp_delete_file.getItemAtPosition(sp_delete_file.getSelectedItemPosition()).toString();
								BarPrinter printer = application.getPrinter();
								ILabelConfig labelConfig = printer.labelConfig();
								labelConfig.deleteFile(selectedItem);
								if (printer.labelQuery().getPrinterLanguage() == InstructionType.BPLT) {
									labelConfig.deleteFile("F,"+selectedItem);
								}
								fileAdapter.remove(sp_delete_file.getSelectedItem().toString());
								fileAdapter.notifyDataSetChanged();
								ConnectivityActivity.updateStoredCustomFontArray(application, printer, PrinterConfigurationActivity.this);
								ConnectivityActivity.updateStoredImageArray(application, printer, PrinterConfigurationActivity.this);
								ConnectivityActivity.updateStoredFormatArray(application, printer, PrinterConfigurationActivity.this);
								sp_delete_file.setSelection(0);
							} catch (Exception e) {
								e.printStackTrace();
								AlertDialogUtil.showDialog(e, PrinterConfigurationActivity.this);
							}
						}

					})
					.setNegativeButton("Cancle",new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							sp_delete_file.setSelection(0);
						}
					})
					.show();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}
	/// \endcode

	/// \if English
	/// \defgroup FORMAT_FLASH Format the flash
	/// \elseif Chinese
	/// \defgroup FORMAT_FLASH 格式化Flash
	/// \endif
	/// \code
	private class FormatFlashListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			try {
				BarPrinter printer = application.getPrinter();
				ILabelConfig labelConfig = printer.labelConfig();
				labelConfig.formatFlash();
				Thread.sleep(8000);
				ConnectivityActivity.updateStoredCustomFontArray(application, printer, PrinterConfigurationActivity.this);
				ConnectivityActivity.updateStoredFormatArray(application, printer, PrinterConfigurationActivity.this);
				ConnectivityActivity.updateStoredImageArray(application, printer, PrinterConfigurationActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrinterConfigurationActivity.this);
			}
		}
	}
	/// \endcode
	private   <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
