package com.snbc.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelImageAndFont;
import com.snbc.sdk.barcode.enumeration.InstructionType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DownloadFontActivity extends Activity {
	private TextView tv_back_store_font;
	private Spinner sp_store_font_to_printer;
	private List<String> fontList = new ArrayList<String>();
	private ArrayAdapter<String> fontAdapter;
	private EditText et_font_name;
	private RadioGroup rg_font_store_postion;
	private RadioButton rb_font_store_postion;
	private Button btn_font_download;
	private SNBCApplication application;
	int flashSize = 0,ramSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_font_to_printer);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		initView();
	}
	//init view
	@SuppressLint("DefaultLocale")
	private void initView() {
		tv_back_store_font = (TextView) findViewById(R.id.tv_back_store_font_to_printer);
		sp_store_font_to_printer = (Spinner) findViewById(R.id.sp_font_file);

		application =(SNBCApplication)getApplication();
		if(application.getPrinter().labelQuery().getPrinterLanguage() == InstructionType.BPLA){
			AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", DownloadFontActivity.this);
		}
		try {
			fontList = Arrays.asList(application.getOsFontFileArray());
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, DownloadFontActivity.this);
		}
		et_font_name = (EditText) findViewById(R.id.et_font_name);
		rg_font_store_postion = (RadioGroup) findViewById(R.id.rg_font_store_position);
		btn_font_download = (Button) findViewById(R.id.btn_download_font);
		fontAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,fontList);
		fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_store_font_to_printer.setAdapter(fontAdapter);
		sp_store_font_to_printer.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if(fontList.size()>0){
					String selectedItem = sp_store_font_to_printer.getItemAtPosition(sp_store_font_to_printer.getSelectedItemPosition()).toString();
					if (!TextUtils.isEmpty(selectedItem)) {
						et_font_name.setText(selectedItem.substring(0, selectedItem.lastIndexOf(".")));
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});


		tv_back_store_font.setOnClickListener(new BackListener());
		btn_font_download.setOnClickListener(new DownloadListener());

	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(DownloadFontActivity.this,MainActivity.class);
			startActivity(intent);
			DownloadFontActivity.this.finish();
		}

	}
	/// \if English
	/// \defgroup DOWNLOAD_FONT_TO_PRINTER Download font to printer
	/// \elseif Chinese
	/// \defgroup DOWNLOAD_FONT_TO_PRINTER 将字库下载到打印机
	/// \endif
	/// \code
	private class DownloadListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			try {
				BarPrinter printer = application.getPrinter();
				InstructionType mType = printer.labelQuery().getPrinterLanguage();
				switch (mType) {
					case BPLZ:
					case BPLT:
					case BPLC:
						doDownloadFont(printer);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, DownloadFontActivity.this);
			}

		}
	}
	private void doDownloadFont(BarPrinter printer) {
		try {
			Map<String,Integer> map =	printer.labelQuery().getRemainingMemory();
			Set<Entry<String,Integer>> set = map.entrySet();
			for (Iterator<Entry<String, Integer>> iterator = set.iterator(); iterator.hasNext();) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
				if (entry.getKey().toUpperCase(Locale.getDefault()).contains("RAM")) {
					ramSize = entry.getValue();
				}
				if (entry.getKey().toUpperCase(Locale.getDefault()).contains("FLASH")) {
					flashSize = entry.getValue();
				}
			}

			int selected = rg_font_store_postion.getCheckedRadioButtonId();
			rb_font_store_postion = (RadioButton) findViewById(selected);
			StringBuilder downloadPath = new StringBuilder() ;
			String selectedItem = sp_store_font_to_printer.getItemAtPosition(sp_store_font_to_printer.getSelectedItemPosition()).toString();
			int fileSize = getAssets().open(selectedItem).available();
			boolean isHasEnoughSpace = false;
			if (rb_font_store_postion.getText().toString().equals(getResources().getString(R.string.ram))) {
				downloadPath.append(application.getRamDiskSymbol());
			}else{
				downloadPath.append(application.getFlashDiskSymbol());
			}
			InstructionType language = printer.labelQuery().getPrinterLanguage();
			if (language.equals(InstructionType.BPLZ) || language.equals(InstructionType.BPLT) ) {
				if(rb_font_store_postion.getText().toString().equals(getResources().getString(R.string.ram))){
					isHasEnoughSpace = ramSize > fileSize;
				}else{
					isHasEnoughSpace = flashSize>fileSize;
				}
			}
			else{
				isHasEnoughSpace = flashSize>fileSize;
			}
			if(!isHasEnoughSpace){
				AlertDialogUtil.showDialog("has no enough space", DownloadFontActivity.this);
				return ;
			}
			ILabelImageAndFont labelImageAndFont = printer.labelImageAndFont();
			InputStream fis = getAssets().open(selectedItem);
			int fileLen = fis.available();
			byte[] data = new byte[fileLen];

			while (fis.read(data) != -1);
			fis.close();

			labelImageAndFont.downloadTTF(data, downloadPath.append(et_font_name.getText().toString()).append(".ttf").toString());
			Toast.makeText(DownloadFontActivity.this, getResources().getString(R.string.hint_download_success), Toast.LENGTH_SHORT).show();
			ConnectivityActivity.updateStoredCustomFontArray(application, printer, DownloadFontActivity.this);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, DownloadFontActivity.this);
		}
	}
	/// \endcode
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
