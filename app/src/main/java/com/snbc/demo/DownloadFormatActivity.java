package com.snbc.demo;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelFormat;
import com.snbc.sdk.barcode.enumeration.InstructionType;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class DownloadFormatActivity extends Activity {
	private  TextView tv_back_download_format;
	private  Spinner sp_download_format;
	private  Button btn_download_format;
	private  List<String> formatList;
	private ArrayAdapter<String> formatAdapter;
	private  SNBCApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_format_to_printer);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		tv_back_download_format = (TextView) findViewById(R.id.tv_back_store_format_to_printer);
		sp_download_format = (Spinner) findViewById(R.id.sp_format_file);
		btn_download_format = (Button) findViewById(R.id.btn_download_format);

		application = (SNBCApplication)getApplication();
		if(application.getPrinter().labelQuery().getPrinterLanguage() == InstructionType.BPLA){
			AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", DownloadFormatActivity.this);
		}
		try {
			formatList = Arrays.asList(application.getOsFormatFileArray());
			formatAdapter = new ArrayAdapter<String>(DownloadFormatActivity.this, android.R.layout.simple_spinner_item,formatList);
			formatAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			sp_download_format.setAdapter(formatAdapter);
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, DownloadFormatActivity.this);
		}

		tv_back_download_format.setOnClickListener(new BackListener());
		btn_download_format.setOnClickListener(new DownloadListener());
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(DownloadFormatActivity.this,MainActivity.class);
			startActivity(intent);
			DownloadFormatActivity.this.finish();
		}
	}
	/// \if English
	/// \defgroup DOWNLOAD_FORMAT_TO_PRINTER Download format to printer
	/// \elseif Chinese
	/// \defgroup DOWNLOAD_FORMAT_TO_PRINTER 将Format文件下载到打印机
	/// \endif
	/// \code
	private class DownloadListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			try {
				BarPrinter printer = application.getPrinter();
				String selectedItem = sp_download_format.getItemAtPosition(sp_download_format.getSelectedItemPosition()).toString();
				if (TextUtils.isEmpty(selectedItem)) {
					return ;
				}
				InstructionType mType = printer.labelQuery().getPrinterLanguage();
				switch (mType) {
					case BPLZ:
					case BPLE:
					case BPLC:
						downloadFormat(printer,selectedItem);
						break;
					case BPLT:
					case BPLA:
						AlertDialogUtil.showDialog("Not support",DownloadFormatActivity.this);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, DownloadFormatActivity.this);
			}
		}
	}

	private void downloadFormat(BarPrinter printer, String selectedItem) {
		try {
			ILabelFormat labelFormat = printer.labelFormat();

			InputStream fis = getAssets().open(selectedItem);
			int fileLen = fis.available();
			byte[] data = new byte[fileLen];

			while (fis.read(data) != -1);
			fis.close();

			labelFormat.downloadFormat(data);

			Thread.sleep(2000);
			ConnectivityActivity.updateStoredFormatArray(application, printer, DownloadFormatActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, DownloadFormatActivity.this);
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
