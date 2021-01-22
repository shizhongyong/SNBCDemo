package com.snbc.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
import com.snbc.sdk.barcode.enumeration.InstructionType;

@SuppressWarnings("deprecation")
public class PrintImageActivity extends TabActivity {
	private TextView tv_back_print_image;
	private TabHost tabHost;
	private TabSpec ts_image;
	private TabSpec ts_image_stored;
	private Spinner sp_print_image;
	private Spinner sp_print_image_stored;
	private Button btn_print_image;
	private Button btn_print_image_stored;
	private List<String> imageList = new ArrayList<String>();
	private List<String> imageStoredList = new ArrayList<String>();
	private ArrayAdapter<String> imageAdapter;
	private ArrayAdapter<String> imageStoredAdapter;
	private SNBCApplication application;
	private BarPrinter printer = null;
	private InstructionType mType = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_image);
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
		tv_back_print_image = (TextView) findViewById(R.id.tv_back_print_image);

		tabHost = getTabHost();
		ts_image = tabHost.newTabSpec("image");
		ts_image_stored = tabHost.newTabSpec("image stored");
		ts_image.setIndicator(getResources().getText(R.string.image));
		ts_image.setContent(R.id.widget_layout_image);
		sp_print_image = (Spinner) findViewById(R.id.sp_print_image);
		btn_print_image = (Button) findViewById(R.id.btn_print_image);
		application = (SNBCApplication)getApplication();
		imageList = Arrays.asList(application.getOsImageFileForPrintArray());
		imageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,imageList);
		imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_print_image.setAdapter(imageAdapter);

		ts_image_stored.setIndicator(getResources().getText(R.string.image_stored_on_printer));
		ts_image_stored.setContent(R.id.widget_layout_image_stored_on_printer);
		sp_print_image_stored = (Spinner) findViewById(R.id.sp_print_image_stored_on_printer);
		btn_print_image_stored = (Button) findViewById(R.id.btn_print_image_stored_on_printer);
		printer = application.getPrinter();
		mType = printer.labelQuery().getPrinterLanguage();
		try {
			if(mType == InstructionType.BPLC){

			}else{
				imageStoredList = Arrays.asList(application.getStoredImageArray());
				imageStoredAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,imageStoredList);
				imageStoredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_print_image_stored.setAdapter(imageStoredAdapter);

			}
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, PrintImageActivity.this);
		}
		tabHost.addTab(ts_image);
		tabHost.addTab(ts_image_stored);

		tv_back_print_image.setOnClickListener(new BackListener());
		btn_print_image.setOnClickListener(new ImageStoreListener());
		btn_print_image_stored.setOnClickListener(new PrintImageStoredListener());
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(PrintImageActivity.this,MainActivity.class);
			startActivity(intent);
		}

	}

	private class ImageStoreListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			try {

				BarPrinter printer = application.getPrinter();
				doPrintOSImage(printer);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintImageActivity.this);
			}

		}
		/// \if English
		/// \defgroup PRINT_IMAGE Print image
		/// \elseif Chinese
		/// \defgroup PRINT_IMAGE 打印图像
		/// \endif
		/// \code
		private void doPrintOSImage(BarPrinter printer) {
			String selectedItem = sp_print_image.getItemAtPosition(sp_print_image.getSelectedItemPosition()).toString();
			try {
				ILabelEdit labelEdit = printer.labelEdit();
				labelEdit.setColumn(1, 8);
				labelEdit.setLabelSize(640, 800);
				if(printer.labelQuery().getPrinterLanguage()== InstructionType.BPLT || printer.labelQuery().getPrinterLanguage() == InstructionType.BPLE){
					labelEdit.clearPrintBuffer();
				}
				labelEdit.printImage(0, 0, BitmapFactory.decodeStream(getAssets().open(selectedItem)));
				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintImageActivity.this);
			}
		}
		/// \endcode
	}
	private class PrintImageStoredListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (mType == InstructionType.BPLC) {
				AlertDialogUtil.showDialog("Sorry, BPLC instruction can't support this function.",
						PrintImageActivity.this);
			} else {
				try {
					if (imageStoredList.size() == 0) {
						return;
					}
					BarPrinter printer = application.getPrinter();
					doPrintStoredImage(printer);
				} catch (Exception e) {
					e.printStackTrace();
					AlertDialogUtil.showDialog(e, PrintImageActivity.this);
				}
			}
		}
		/// \if English
//		/ \defgroup PRINT_DOWNLOADED_IMAGE Print the downloaded image
//		/ \elseif Chinese
//		/ \defgroup PRINT_DOWNLOADED_IMAGE 打印用户下载的图像
//		/ \endif
//		/ \code
		private void doPrintStoredImage(BarPrinter printer) {
			String selectedItem = sp_print_image_stored.getItemAtPosition(sp_print_image_stored.getSelectedItemPosition()).toString();
			ILabelEdit labelEdit = printer.labelEdit();
			try {
				labelEdit.setColumn(1, 8);
				labelEdit.setLabelSize(640, 800);
				if(printer.labelQuery().getPrinterLanguage()== InstructionType.BPLT || printer.labelQuery().getPrinterLanguage() == InstructionType.BPLE){
					labelEdit.clearPrintBuffer();
				}
				labelEdit.printStoredImage(0, 0, selectedItem);
				printer.labelControl().print(1, 1);

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintImageActivity.this);
			}
		}
		/// \endcode

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



}
