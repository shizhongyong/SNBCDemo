package com.snbc.demo;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

public class DownloadImageActivity extends Activity {

	private TextView tv_back_store_image;
	private Spinner sp_store_image_to_printer;
	private ArrayAdapter<String> imageAdapter;
	private EditText et_image_name;
	private RadioGroup rg_image_store_postion;
	private RadioButton rb_image_store_postion;
	private Button btn_image_download;
	private SNBCApplication application;
	private int flashSize = 0, ramSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_to_printer);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		initView();
	}

	private void initView() {
		tv_back_store_image = (TextView) findViewById(R.id.tv_back_store_image_to_printer);
		sp_store_image_to_printer = (Spinner) findViewById(R.id.sp_image_file);

		List<String> imageList = new ArrayList<String>();
		try {
			application = (SNBCApplication) getApplication();
			BarPrinter printer = null;
			InstructionType mType = null;
			printer = application.getPrinter();
			mType = printer.labelQuery().getPrinterLanguage();
			if (mType == InstructionType.BPLC) {
				AlertDialogUtil.showDialog(
						"Sorry, BPLC instruction can't support this function.",
						DownloadImageActivity.this);
			} else {
				imageList = Arrays.asList(application.getOsImageFileArray());
				imageAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, imageList);
				imageAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_store_image_to_printer.setAdapter(imageAdapter);
			}
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, DownloadImageActivity.this);
		}
		sp_store_image_to_printer.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				String selectedItem = sp_store_image_to_printer.getItemAtPosition(sp_store_image_to_printer.getSelectedItemPosition()).toString();
				et_image_name.setText(selectedItem.substring(0, selectedItem.lastIndexOf(".")));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		et_image_name = (EditText) findViewById(R.id.et_image_name);
		rg_image_store_postion = (RadioGroup) findViewById(R.id.rg_image_store_position);
		btn_image_download = (Button) findViewById(R.id.btn_download_image);
		tv_back_store_image.setOnClickListener(new BackListener());
		btn_image_download.setOnClickListener(new DownloadListener());

	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(DownloadImageActivity.this,MainActivity.class);
			startActivity(intent);
			DownloadImageActivity.this.finish();
		}

	}
	/// \if English
	/// \defgroup DOWNLOAD_IMAGE_TO_PRINTER Download image to printer
	/// \elseif Chinese
	/// \defgroup DOWNLOAD_IMAGE_TO_PRINTER 将图像下载到打印机
	/// \endif
	/// \code
	private class DownloadListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			BarPrinter printer = null;
			InstructionType mType = null;
			try {
				printer = ((SNBCApplication)getApplication()).getPrinter();
				mType = printer.labelQuery().getPrinterLanguage();
				switch (mType) {
					case BPLZ:
					case BPLE:
					case BPLT:
					case BPLC:
					case BPLA:
						downloadImage(printer);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, DownloadImageActivity.this);
			}
		}

		@SuppressLint("DefaultLocale")
		private void downloadImage(BarPrinter printer) {
			try {
				int selected = rg_image_store_postion.getCheckedRadioButtonId();
				rb_image_store_postion = (RadioButton) findViewById(selected);
				String storePlace = rb_image_store_postion.getText().toString().trim();
				if (TextUtils.isEmpty(storePlace) || TextUtils.isEmpty(et_image_name.getText().toString())) {
					return ;
				}
				String selectedItem = null ;
				String downloadPath = null;
				ILabelImageAndFont labelImageAndFont = printer.labelImageAndFont();
				selectedItem = sp_store_image_to_printer.getItemAtPosition(sp_store_image_to_printer.getSelectedItemPosition()).toString();
				if (storePlace.equals(getResources().getString(R.string.ram))) {
					downloadPath = application.getRamDiskSymbol()+et_image_name.getText().toString();
				}
				else if(storePlace.equals(getResources().getString(R.string.flash))){
					downloadPath = application.getFlashDiskSymbol()+et_image_name.getText().toString();
				}
				if(application.getPrinter().labelQuery().getPrinterLanguage()!=InstructionType.BPLA){
					Map<String,Integer> memoryMap = application.getPrinter().labelQuery().getRemainingMemory();
					Set<Entry<String, Integer>>  memorySet = memoryMap.entrySet();
					for (Entry<String, Integer> entry : memorySet) {
						if (entry.getKey().toUpperCase(Locale.getDefault()).contains(("RAM"))) {
							ramSize = entry.getValue();
						}
						if (entry.getKey().toUpperCase(Locale.getDefault()).contains("FLASH")) {
							flashSize = entry.getValue();
						}
					}
					boolean isHasEnoughSpace = false;
					int fileSize = getAssets().open(selectedItem).available();

					InstructionType mType = printer.labelQuery().getPrinterLanguage();
					if(mType == InstructionType.BPLZ || mType == InstructionType.BPLT){
						if (storePlace.equals(getResources().getString(R.string.ram))) {
							isHasEnoughSpace = ramSize > fileSize;
						}else{
							isHasEnoughSpace = flashSize > fileSize;
						}
					}else{
						isHasEnoughSpace = flashSize > fileSize;
					}
					if(!isHasEnoughSpace){
						AlertDialogUtil.showDialog("The printer has not enough space to download file.", DownloadImageActivity.this);
						return ;
					}
				}
				InputStream fis = getAssets().open(selectedItem);
				int fileLen = fis.available();
				byte[] data = new byte[fileLen];

				while (fis.read(data) != -1);
				fis.close();
				if (selectedItem.toUpperCase().contains(".BMP")) {
					if(data[0] == 0x42 && data[1] == 0x4D && data[27] == 0x00 && data[28] == 0x01 ){
						labelImageAndFont.downloadBMP(data, downloadPath);
					}else{
						AlertDialogUtil.showDialog("Not support", DownloadImageActivity.this);
						return ;
					}
				}
				else if(selectedItem.toUpperCase().contains(".PCX")){
					labelImageAndFont.downloadPCX(data, downloadPath);
				}else{
					AlertDialogUtil.showDialog("Not support", DownloadImageActivity.this);
					return ;
				}
				Toast.makeText(DownloadImageActivity.this, getResources().getString(R.string.hint_download_success), Toast.LENGTH_SHORT).show();
				Thread.sleep(2000);
				ConnectivityActivity.updateStoredImageArray(application, printer, DownloadImageActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, DownloadImageActivity.this);
			}
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
